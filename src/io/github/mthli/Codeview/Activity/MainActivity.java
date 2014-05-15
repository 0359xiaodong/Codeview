package io.github.mthli.Codeview.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import io.github.mthli.Codeview.FileChooser.FileChooserActivity;
import io.github.mthli.Codeview.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends Activity
        implements ActionBar.OnNavigationListener {

    ListView view;
    SearchView search_view;
    MainListViewItemAdapter adapter;
    List<MainListViewItem> item;

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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                startActivityForResult(intent, FILE_CHOOSER);
            }
        });

        /* Just for use netvork in main activity */
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle(getString(R.string.uri_error_title))
                                .setMessage(getString(R.string.uri_error_https));
                        builder.setPositiveButton(
                                getString(R.string.uri_button),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /* Nothing */
                                    }
                                }
                        ).create().show();
                    } else if ((!text.endsWith(".git"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle(getString(R.string.uri_error_title))
                                .setMessage(getString(R.string.uri_error_git));
                        builder.setPositiveButton(
                                getString(R.string.uri_button),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /* Nothing */
                                    }
                                }
                        ).create().show();
                    } else {

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
}
