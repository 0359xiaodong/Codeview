package io.github.mthli.Codeview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.*;

/**
 * Created by matthew on 5/3/14.
 * This class create a activity to show some information about app.
 */
public class AboutActivity extends Activity{
    private String[] title = new String[] {
        "Designer",
        "Coder",
        "Version",
        "Homepage",
        "License"
    };
    private String[] content = new String[] {
        "ChansZen <chanszen@gmail.com>",
        "MatthewLee <matthewlee0725@gmail.com>",
        "1.0.0",
        "https://github.com/mthli/Codeview",
        "Mozilla Public License Version 2.0"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_listview);

        List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
        for (int i = 0; i < title.length; i++) {
            Map<String, String> listItem = new HashMap<String, String>();
            listItem.put("titles", title[i]);
            listItem.put("contents", content[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                listItems,
                R.layout.about_listviewitem,
                new String[] {"titles", "contents"},
                new int[] {R.id.about_listviewitem_title, R.id.about_listviewitem_content}
                );

        ListView listView = (ListView)findViewById(R.id.about_listview);
        listView.setAdapter(simpleAdapter);
    }

    /**
     * We also need a method to call browser
     */
}
