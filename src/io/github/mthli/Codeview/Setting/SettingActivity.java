package io.github.mthli.Codeview.Setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import io.github.mthli.Codeview.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends Activity {
    final private int AD_FONTSIZE = 0;
    final private int AD_HIGHLIGHT = 1;

    private int fontsize;
    private int fontsize_num;
    private String highlight;
    private int highlight_num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayList<String> title = new ArrayList<String>();
        title.add(getString(R.string.setting_title_fontsize));
        title.add(getString(R.string.setting_title_highlight));

        ArrayList<String> content = new ArrayList<String>();
        content.add(getString(R.string.setting_content_fontsize));
        content.add(getString(R.string.setting_content_highlight));

        List<Map<String, String>> lists = new ArrayList<Map<String, String>>();

        for (int i = 0; i < 2; i++) {
            Map<String, String> list = new HashMap<String, String>();
            list.put("title", title.get(i));
            list.put("content", content.get(i));
            lists.add(list);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                lists,
                R.layout.setting_item,
                new String[]{"title", "content"},
                new int[]{R.id.setting_item_title, R.id.setting_item_content}
        );

        ListView listView = (ListView) findViewById(R.id.setting);
        listView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == AD_FONTSIZE) {
                    fontsize_num = sharedPreferences.getInt("fontsize_num", 0);
                    final AlertDialog.Builder builder_fontsize = new AlertDialog.Builder(SettingActivity.this)
                            .setSingleChoiceItems(
                                    R.array.setting_array_fontsize,
                                    fontsize_num,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putInt("fontsize_num", which);
                                            editor.putInt("fontsize", which + 12);
                                            editor.commit();
                                        }
                                    }
                            ).setNegativeButton(
                                    getString(R.string.setting_ad_close),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /* Do nothing */
                                        }
                                    }
                            );
                    builder_fontsize.show();
                } else {
                    highlight_num = sharedPreferences.getInt("highlight_num", 0);
                    final AlertDialog.Builder builder_highlight = new AlertDialog.Builder(SettingActivity.this)
                            .setSingleChoiceItems(
                                    R.array.setting_array_highlight,
                                    highlight_num,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putInt("highlight_num", which);
                                            String str = getResources().getStringArray(R.array.setting_array_highlight)[which];
                                            str = str + ".css";
                                            editor.putString("highlight", str);
                                            editor.commit();
                                        }
                                    }
                            ).setNegativeButton(
                                    getString(R.string.setting_ad_close),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /* Do nothing */
                                        }
                                    }
                            );
                    builder_highlight.show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
