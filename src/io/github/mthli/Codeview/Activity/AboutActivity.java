package io.github.mthli.Codeview.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import io.github.mthli.Codeview.R;

import java.util.*;

public class AboutActivity extends Activity {
    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.list_view_about);
        getActionBar().setDisplayHomeAsUpEnabled(true);

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
                R.layout.list_view_item_about,
                new String[] {"title", "content"},
                new int[] {R.id.list_view_item_about_title, R.id.list_view_item_about_content}
        );
        ListView list_view = (ListView) findViewById(R.id.list_view_about);
        list_view.setAdapter(simple_adapter);
    }
}