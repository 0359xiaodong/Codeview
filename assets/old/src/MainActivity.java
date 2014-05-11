package io.github.mthli.Codeview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends Activity
        implements ActionBar.OnNavigationListener {

    ListView view;
    MainListViewItemAdapter adapter;
    List<MainListViewItem> item;

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

        /* *******************************************

        swipe_list_item = new ArrayList<MainListViewItem>();
        swipe_list_item_adapter = new MainListItemAdapter(
                this,
                R.layout.list_view_item_main,
                swipe_list_item
        );

        for(int i=0;i<10;i++)
        {
            swipe_list_item.add(new MainListViewItem(getResources().getDrawable(R.drawable.ic_filetype_folder) ,"Swipe Item" + i, "sdadadada"));
        }

        swipe_list_view = (SwipeListView) findViewById(R.id.list_view_main);
        swipe_list_view.setAdapter(swipe_list_item_adapter);swipe_list_view.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onDismiss(int[] reverse_sorted_positions) {
                for (int position : reverse_sorted_positions) {
                    swipe_list_item.remove(position);
                }
                swipe_list_item_adapter.notifyDataSetChanged();
            }
        });

        swipe_list_item_adapter.notifyDataSetChanged();

        /* ******************************************** */

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
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(int i, long j) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
