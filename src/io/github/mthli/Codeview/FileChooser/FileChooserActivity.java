package io.github.mthli.Codeview.FileChooser;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import io.github.mthli.Codeview.Database.MDBAction;
import io.github.mthli.Codeview.Database.Mark;
import io.github.mthli.Codeview.R;
import io.github.mthli.Codeview.Setting.SettingActivity;
import io.github.mthli.Codeview.ShowMark.ShowMarkActivity;
import io.github.mthli.Codeview.WebView.CodeviewActivity;

import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileChooserActivity extends ListActivity {
    private File current;
    private File fileSelected;
    private FileAdapter fileAdapter;
    private FileFilter fileFilter;
    private ArrayList<String> extensions;
    private String folder_path;
    private String folder_name;

    /* Maybe add more */
    final private int CM_MARK = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getStringArrayList(Constants.EXTENSIONS) != null) {
                extensions = bundle.getStringArrayList(Constants.EXTENSIONS);
                fileFilter = new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return (
                                (pathname.isDirectory()) || (pathname.getName().contains(".") ? extensions.contains(pathname.getName().substring(pathname.getName().lastIndexOf("."))) : false)
                        );
                    }
                };
            }
        }
        folder_path = getIntent().getStringExtra("folder_path");
        folder_name = getIntent().getStringExtra("folder_name");
        current = new File(folder_path);
        listAll(current);

        FileChooserActivity.this.getListView().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, CM_MARK, 0, getString(R.string.cm_mark));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!current.getName().equalsIgnoreCase(folder_name)) {
                    current = new File(current.getParent());
                    listAll(current);
                } else {
                    finish();
                }
                return true;
            case R.id.fc_menu_showmark:
                Intent intent_mark = new Intent(this, ShowMarkActivity.class);
                startActivity(intent_mark);
                return true;
            case R.id.fc_menu_setting:
                Intent intent_setting = new Intent(this, SettingActivity.class);
                startActivity(intent_setting);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        MDBAction mdbAction = new MDBAction(FileChooserActivity.this);
        switch (menuItem.getItemId()) {
            case CM_MARK:
                try {
                    mdbAction.openDatabase(true);
                } catch (SQLException s) {
                    Toast.makeText(
                            FileChooserActivity.this,
                            getString(R.string.database_error_open),
                            Toast.LENGTH_SHORT
                    ).show();
                    mdbAction.closeDatabase();
                    break;
                }
                Mark mark = new Mark();
                mark.setTitle(fileAdapter.getItem(info.position).getTitle());
                mark.setContent(relativePath(fileAdapter.getItem(info.position).getPath()));
                mark.setPath(fileAdapter.getItem(info.position).getPath());
                if (!mdbAction.checkMark(fileAdapter.getItem(info.position).getPath())) {
                    mdbAction.newMark(mark);
                }
                Toast.makeText(
                        FileChooserActivity.this,
                        getString(R.string.mark_successful),
                        Toast.LENGTH_SHORT
                ).show();
                mdbAction.closeDatabase();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(menuItem);
    }

    @Override
    protected void onListItemClick(
            ListView listView,
            View view,
            int position,
            long id
    ) {
        super.onListItemClick(listView, view, position, id);

        FileItem item = fileAdapter.getItem(position);
        if (item.isFolder()) {
            current = new File(item.getPath());
            listAll(current);
        } else {
            /*
            String str[] = getResources().getStringArray(R.array.webview_support);
            boolean isSupport = false;
            for (int i = 0; i < str.length; i++) {
                if (item.getPath().endsWith(str[i])) {
                    isSupport = true;
                    break;
                }
            }
            if (isSupport) {
                fileSelected = new File(item.getPath());
                String title = fileSelected.getName();
                String sub_title = relativePath(fileSelected.getAbsolutePath());
                Intent intent = new Intent(FileChooserActivity.this, CodeviewActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("sub_title", sub_title);
                intent.putExtra("path", fileSelected.getAbsolutePath());
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("file://" + item.getPath());
                intent.setDataAndType(uri, "application/*");
                startActivity(intent);
            } */
            fileSelected = new File(item.getPath());
            String title = fileSelected.getName();
            String sub_title = relativePath(fileSelected.getAbsolutePath());
            Intent intent = new Intent(FileChooserActivity.this, CodeviewActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("sub_title", sub_title);
            intent.putExtra("path", fileSelected.getAbsolutePath());
            startActivity(intent);
        }
    }

    private void listAll(File f) {
        File[] folders = null;
        if (fileFilter != null) {
            folders = f.listFiles(fileFilter);
        } else {
            folders = f.listFiles();
        }

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(f.getName());
        actionBar.setSubtitle(relativePath(f.getAbsolutePath()));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        List<FileItem> dirs = new ArrayList<FileItem>();
        List<FileItem> files = new ArrayList<FileItem>();
        try {
            for (File file : folders) {
                if (file.isDirectory() && !file.isHidden()) {
                    dirs.add(
                            new FileItem(
                                    file.getName(),
                                    file.listFiles().length + " items",
                                    file.getPath(),
                                    Constants.FOLDER,
                                    true,
                                    false
                            )
                    );
                } else {
                    if (!file.isHidden()) {
                        files.add(
                                new FileItem(
                                        file.getName(),
                                        file.length() + " bits",
                                        file.getPath(),
                                        file.getPath(),
                                        false,
                                        false
                                )
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(dirs);
        Collections.sort(files);
        dirs.addAll(files);
        fileAdapter = new FileAdapter(
                FileChooserActivity.this,
                R.layout.fc,
                dirs
        );
        this.setListAdapter(fileAdapter);
        fileAdapter.notifyDataSetChanged();
    }

    private String relativePath(String path) {
        String[] str = path.split("/");
        String start = folder_name;
        for (int i = 0; i < str.length; i++) {
            if (str[i].equals(folder_name)) {
                for (i = i + 1 ; i < str.length; i++) {
                    start = start + File.separator + str[i];
                }
                break;
            }
        }
        return start;
    }
}
