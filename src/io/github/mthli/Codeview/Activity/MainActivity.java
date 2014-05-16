package io.github.mthli.Codeview.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import io.github.mthli.Codeview.FileChooser.FileChooserActivity;
import io.github.mthli.Codeview.R;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainActivity
        extends Activity
        implements ActionBar.OnNavigationListener {

    private ListView view;
    private SearchView search_view;
    private MainListViewItemAdapter adapter;
    private List<MainListViewItem> item;

    private ProgressDialog pd_cloning;

    private String uri;

    private SQLiteDatabase database;
    private


    final int FILE_CHOOSER = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_main);

        ActionBar action_bar = getActionBar();
        action_bar.setDisplayShowTitleEnabled(false);
        action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        action_bar.setListNavigationCallbacks(
                new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                getString(R.string.action_bar_drop_down_repo),
                                getString(R.string.action_bar_drop_down_mark)
                        }
                ),
                this
        );

        /*
         * We need to use new method to add list,
         * this method just show demo
         */
        item = new ArrayList<MainListViewItem>();
        adapter = new MainListViewItemAdapter(
                this,
                R.layout.list_view_item_main,
                item
        );
        for (int i = 0; i < 10; i++) {
            item.add(
                    new MainListViewItem(
                            getResources().getDrawable(R.drawable.ic_filetype_folder),
                            "Title " + i,
                            "Content " + i,
                            "date",
                            new ImageButton(this) //
                    )
            );
        }
        view = (ListView) findViewById(R.id.list_view_main);
        view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {
                String temp = "/data/data/io.github.mthli.Codeview/";
                Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                intent.putExtra("folder_name", "DSNA");
                intent.putExtra("folder_path", temp);
                startActivityForResult(intent, FILE_CHOOSER);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(int i, long j) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        search_view = (SearchView) menu.findItem(R.id.menu_clone_or_search).getActionView();

        SearchView.OnQueryTextListener sv_listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String text) {
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String text) {
                if (text.length() != 0) {
                    /*
                     * ProgressDialog,
                     * we use new thread to clone
                     */
                    if ((!text.startsWith("https://")) && (!text.startsWith("http://"))) {
                        Toast.makeText(
                                MainActivity.this,
                                getString(R.string.uri_error_https),
                                Toast.LENGTH_SHORT
                        ).show();
                    } else if ((!text.endsWith(".git"))) {
                        Toast.makeText(
                                MainActivity.this,
                                getString(R.string.uri_error_git),
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        /* ProgressDialog */
                        pd_cloning = new ProgressDialog(MainActivity.this);
                        pd_cloning.setMessage(getString(R.string.clone_pd));
                        pd_cloning.setCancelable(false);
                        pd_cloning.show();
                        /* Start clone thread */
                        uri = text;
                        HandlerThread thread = new HandlerThread("cloneThread");
                        thread.start();
                        Handler handle = new Handler(thread.getLooper());
                        handle.post(cloneThread);
                    }
                }
                return true;
            }
        };
        search_view.setOnQueryTextListener(sv_listener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent intent_about = new Intent(this, AboutActivity.class);
                startActivity(intent_about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    Runnable cloneThread = new Runnable() {
        @Override
        public void run() {
            String str[] = uri.split("/");
            String title = str[str.length - 1].substring(
                    0,
                    str[str.length - 1].lastIndexOf(".")
            );
            String content = uri;
            String folder_path =
                    MainActivity.this.getFilesDir() +
                    File.separator +
                    title;
            Calendar now = Calendar.getInstance();
            String date =
                    now.get(Calendar.YEAR) +
                    "-" +
                    now.get(Calendar.MONTH) +
                    "-" +
                    now.get(Calendar.DATE);

            CloneCommand clone = Git.cloneRepository();
            clone.setTimeout(30);
            clone.setURI(uri);
            clone.setDirectory(new File(folder_path));
            UsernamePasswordCredentialsProvider access =
                    new UsernamePasswordCredentialsProvider(
                            getString(R.string.clone_username),
                            getString(R.string.clone_password)
                    );
            clone.setCredentialsProvider(access);



            try {
                try {
                    FileUtils.deleteDirectory(new File(folder_path));
                } catch (IOException i) {
                    /* Do something */
                    pd_cloning.dismiss();
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.unknown_error),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                clone.call();

                /* Need SQLite to add new repo info */



                pd_cloning.dismiss();
                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.clone_successful),
                        Toast.LENGTH_SHORT
                ).show();
            } catch (GitAPIException g) {
                /* Do somthing */
                pd_cloning.dismiss();
                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.git_error_clone),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    };
}
