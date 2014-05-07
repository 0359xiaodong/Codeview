package io.github.mthli.Codeview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity
        extends Activity
        implements ActionBar.OnNavigationListener {

    SwipeListView swipe_list_view;
    SwipeListItemAdapter swipe_list_item_adapter;
    List<SwipeListItem> swipe_list_item;

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




        /* ******************************************* */

        swipe_list_view = (SwipeListView) findViewById(R.id.list_view_main);
        swipe_list_item = new ArrayList<SwipeListItem>();
        swipe_list_item_adapter = new SwipeListItemAdapter(
                this,
                R.layout.list_view_item_main,
                swipe_list_item
        );

        swipe_list_view.setAdapter(swipe_list_item_adapter);

        for(int i=0;i<10;i++)
        {
            swipe_list_item.add(new SwipeListItem("Swipe Item" + i, "sdadadada"));
        }

        swipe_list_item_adapter.notifyDataSetChanged();

        /* ******************************************** */




    }

    @Override
    public boolean onNavigationItemSelected(int i, long j) {
        System.out.println("jdish");
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
