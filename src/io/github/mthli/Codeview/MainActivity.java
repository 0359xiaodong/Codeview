package io.github.mthli.Codeview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ActionBar.OnNavigationListener {

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
                    try {
                        Process p = Runtime.getRuntime().exec("ping -c 3 -w 500 www.baidu.com");
                        try {
                            int status = p.waitFor();
                            if (status == 0) {
                                System.out.println("yes");
                            } else {
                                System.out.println("no");
                            }
                        } catch (InterruptedException i) {
                            System.out.println("sdfsdscfa");
                        }
                    } catch (IOException i) {
                        System.out.println("errorrrr");
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
