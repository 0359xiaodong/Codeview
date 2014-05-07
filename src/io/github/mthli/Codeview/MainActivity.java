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

        /* test */ /*
        ArrayList<String> title = new ArrayList<String>();
        title.add(getString(R.string.about_title_designer));
        title.add(getString(R.string.about_title_coder));
        title.add(getString(R.string.about_title_version));
        title.add(getString(R.string.about_title_homepage));
        title.add(getString(R.string.about_title_license));

        ArrayList<String> content = new ArrayList<String>();
        content.add(getString(R.string.about_content_designer));
        content.add(getString(R.string.about_content_coder));
        content.add(getString(R.string.about_content_version));
        content.add(getString(R.string.about_content_homepage));
        content.add(getString(R.string.about_content_license));

        List<Map<String, String>> list_view_items = new ArrayList<Map<String, String>>();

        for (int i = 0; i < 5; i++) {
            Map<String, String> list_view_item = new HashMap<String, String>();
            list_view_item.put("title", title.get(i));
            list_view_item.put("content", content.get(i));
            list_view_items.add(list_view_item);
        }

        SimpleAdapter simple_adapter = new SimpleAdapter(
                this,
                list_view_items,
                R.layout.list_view_item_main,
                new String[] {"title", "content"},
                new int[] {R.id.list_view_main_front_text_title, R.id.list_view_main_front_text_content}
        ); */

        swipe_list_view = (SwipeListView) findViewById(R.id.list_view_main);
        /* Your method {} */
        // swipe_list_view.setSwipeListViewListener(new BaseSwipeListViewListener(){});

        // swipe_list_view.setAdapter(simple_adapter);
        //simple_adapter.notifyDataSetChanged();
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
