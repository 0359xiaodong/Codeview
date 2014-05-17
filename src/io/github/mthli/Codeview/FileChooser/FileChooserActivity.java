package io.github.mthli.Codeview.FileChooser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import io.github.mthli.Codeview.R;
import io.github.mthli.Codeview.WebView.CodeviewActivity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileChooserActivity extends ListActivity {
    private File current_folder;
    private FileListViewItemAdapter adapter;
    private FileFilter file_filter;
    private File file_selected;
    private ArrayList<String> extensions;
    private String folder_path;
    private String folder_name;
    private String folder_date;

    @Override
    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getStringArrayList(FileConstants.EXTENSIONS) != null) {
                extensions = extras.getStringArrayList(FileConstants.EXTENSIONS);
                file_filter = new FileFilter() {
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
        folder_date = getIntent().getStringExtra("folder_date");
        current_folder = new File(folder_path);
        listAll(current_folder);
    }

    private void listAll(File f) {
        File[] folders = null;
        if (file_filter != null) {
            folders = f.listFiles(file_filter);
        } else {
            folders = f.listFiles();
        }

        ActionBar action_bar = getActionBar();
        action_bar.setTitle(f.getName());
        action_bar.setSubtitle(relativePath(f.getAbsolutePath()) + File.separator);
        getActionBar().setDisplayShowHomeEnabled(true);

        List<FileListViewItem> dirs = new ArrayList<FileListViewItem>();
        List<FileListViewItem> files = new ArrayList<FileListViewItem>();
        try {
            for (File file : folders) {
                if (file.isDirectory() && !file.isHidden()) {
                    dirs.add(
                            new FileListViewItem(
                                    file.getName(),
                                    file.listFiles().length + " items",
                                    file.getPath(),
                                    folder_date,
                                    FileConstants.FOLDER,
                                    true,
                                    false
                                    )
                    );
                } else {
                    if (!file.isHidden()) {
                        files.add(
                                new FileListViewItem(
                                        file.getName(),
                                        file.length() + " bits",
                                        file.getPath(),
                                        folder_date,
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
        if (!f.getName().equalsIgnoreCase(folder_name)) {
            dirs.add(
                    0,
                    new FileListViewItem(
                            "..",
                            relativePath(f.getParent()) + File.separator,
                            f.getParent(),
                            folder_date,
                            FileConstants.PARENT,
                            false,
                            true
                    )
            );
        }
        adapter = new FileListViewItemAdapter(
                FileChooserActivity.this,
                R.layout.list_view_fc,
                dirs
        );
        this.setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /* Need to change */
    @Override
    protected void onListItemClick(
            ListView l,
            View v,
            int position,
            long id
    ) {
        super.onListItemClick(l, v, position, id);

        FileListViewItem item = adapter.getItem(position);
        if (item.isFolder() || item.isParent()) {
            current_folder = new File(item.getPath());
            listAll(current_folder);
        } else {
            file_selected = new File(item.getPath());
            String title = file_selected.getName();
            String sub_title = relativePath(file_selected.getAbsolutePath());
            Intent intent = new Intent(FileChooserActivity.this, CodeviewActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("sub_title", sub_title);
            intent.putExtra("path", file_selected.getAbsolutePath());
            startActivity(intent);
        }
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
