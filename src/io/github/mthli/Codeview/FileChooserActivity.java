package io.github.mthli.Codeview;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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
        current_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        listFill(current_folder);
    }

    private void listFill(File f) {
        File[] folders = null;
        if (file_filter != null) {
            folders = f.listFiles(file_filter);
        } else {
            folders = f.listFiles();
        }

        ActionBar action_bar = getActionBar();
        action_bar.setTitle(f.getName());
        action_bar.setSubtitle(f.getAbsolutePath());
        getActionBar().setDisplayShowHomeEnabled(true);

        List<FileListViewItem> dirs = new ArrayList<FileListViewItem>();
        List<FileListViewItem> files = new ArrayList<FileListViewItem>();
        try {
            for (File file : folders) {
                if (file.isDirectory() && !file.isHidden()) {
                    dirs.add(
                            new FileListViewItem(
                                    file.getName(),
                                    file.getPath(),
                                    "date",
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
                                        file.getPath(),
                                        "date",
                                        file.getAbsolutePath(),
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
        if (!f.getName().equalsIgnoreCase(
                Environment.getExternalStorageDirectory().getName()
        )) {
            dirs.add(
                    0,
                    new FileListViewItem(
                            "..",
                            f.getParent(),
                            "date",
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
            listFill(current_folder);
        } else {
            file_selected = new File(item.getPath());
            Intent intent = new Intent();
            intent.putExtra(
                    FileConstants.SELECTED,
                    file_selected.getAbsoluteFile()
            );
            setResult(Activity.RESULT_OK, intent);
            Log.i("FILE CHOOSER", "result ok");
            finish();
            ;
        }
    }
}
