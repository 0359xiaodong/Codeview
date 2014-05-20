package io.github.mthli.Codeview.ShowMark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import io.github.mthli.Codeview.Database.MDBAction;
import io.github.mthli.Codeview.Database.Mark;
import io.github.mthli.Codeview.FileChooser.FileChooserActivity;
import io.github.mthli.Codeview.R;
import io.github.mthli.Codeview.WebView.CodeviewActivity;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowMarkActivity extends Activity {
    private ListView listView;
    private ShowMarkAdapter showMarkAdapter;
    private List<ShowMarkItem> showMarkItems;

    final private int CM_UNMARK = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showmark);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        showMarkItems = new ArrayList<ShowMarkItem>();
        /* Do something */
        refreshList();
        showMarkAdapter = new ShowMarkAdapter(
                this,
                R.layout.showmark_item,
                showMarkItems
        );
        listView = (ListView) findViewById(R.id.showmark);
        listView.setAdapter(showMarkAdapter);
        showMarkAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MDBAction mdbAction = new MDBAction(ShowMarkActivity.this);
                try {
                    mdbAction.openDatabase(false);
                    List<Mark> marks = mdbAction.listMarks();
                    File file = new File(marks.get(position).getPath());
                    if (file.isDirectory()) {
                        Intent intent_filechooser = new Intent(ShowMarkActivity.this, FileChooserActivity.class);
                        if ((marks.get(position).getContent().startsWith("http://") || marks.get(position).getContent().startsWith("https://"))
                                && marks.get(position).getContent().endsWith(".git")) {
                            String str[] = marks.get(position).getContent().split("/");
                            String name = str[str.length - 1].substring(0, str[str.length - 1].length() - 4);
                            System.out.println(name);
                            intent_filechooser.putExtra("folder_name", name);
                            intent_filechooser.putExtra("folder_path", marks.get(position).getPath());
                        } else {
                            String str[] = marks.get(position).getContent().split("/");
                            intent_filechooser.putExtra("folder_name", str[0]);
                            intent_filechooser.putExtra("folder_path", marks.get(position).getPath());
                        }
                        startActivity(intent_filechooser);
                    } else if (file.isFile()) {



                        /* Need do more */
                        Intent intent_codeview = new Intent(ShowMarkActivity.this, CodeviewActivity.class);
                        intent_codeview.putExtra("title", marks.get(position).getTitle());
                        intent_codeview.putExtra("sub_title", marks.get(position).getContent());
                        intent_codeview.putExtra("path", marks.get(position).getPath());
                        startActivity(intent_codeview);



                    }
                } catch (SQLException s) {
                    Toast.makeText(
                            ShowMarkActivity.this,
                            getString(R.string.database_error_open),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                mdbAction.closeDatabase();
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, CM_UNMARK, 0, getString(R.string.cm_unmark));
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        MDBAction mdbAction = new MDBAction(ShowMarkActivity.this);
        switch (menuItem.getItemId()) {
            case CM_UNMARK:
                try {
                    mdbAction.openDatabase(true);
                } catch (SQLException s) {
                    Toast.makeText(
                            ShowMarkActivity.this,
                            getString(R.string.database_error_open),
                            Toast.LENGTH_SHORT
                    ).show();
                    mdbAction.closeDatabase();
                    break;
                }
                List<Mark> marks = mdbAction.listMarks();
                String path = marks.get(info.position).getPath();
                mdbAction.unMark(path);
                showMarkItems.remove(info.position);
                mdbAction.closeDatabase();
                refreshList();
                showMarkAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* Refreah ListView */
    public void refreshList() {
        showMarkItems.clear();
        MDBAction mdbAction = new MDBAction(ShowMarkActivity.this);
        try {
            mdbAction.openDatabase(false);
            List<Mark> marks = mdbAction.listMarks();
            for (int i = 0; i < marks.size(); i++) {
                showMarkItems.add(
                        new ShowMarkItem(
                                marks.get(i).getTitle(),
                                marks.get(i).getContent(),
                                marks.get(i).getPath()
                        )
                );
            }
        } catch (SQLException s) {
            Toast.makeText(
                    ShowMarkActivity.this,
                    getString(R.string.database_error_open),
                    Toast.LENGTH_SHORT
            ).show();
        }
        mdbAction.closeDatabase();
    }
}
