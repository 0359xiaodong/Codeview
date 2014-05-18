package io.github.mthli.Codeview.Main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import io.github.mthli.Codeview.Database.DBAction;
import io.github.mthli.Codeview.Database.Repo;
import io.github.mthli.Codeview.FileChooser.FileChooserActivity;
import io.github.mthli.Codeview.Other.AboutActivity;
import io.github.mthli.Codeview.R;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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

    private Handler clone_handle;
    private Handler list_handle;


    final int FILE_CHOOSER = 1;
    final int CM_COMMIT = 0;
    final int CM_UPDATE = 1;
    final int CM_DELETE = 2;

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

        /* ********************************************************** */
        /* 开启一个新线程用于填充ListView */
        final HandlerThread thread = new HandlerThread("listThread");
        thread.start();
        list_handle = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    thread.getLooper().quit();
                    adapter.notifyDataSetChanged();
                }
            }
        };
        list_handle.post(listThread);
        /* ********************************************************** */

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
                /* 传递正确的数据给FileChooser */
                DBAction db_action = new DBAction(MainActivity.this);
                try {
                    db_action.openDB(false);
                    List<Repo> repos = db_action.listRepos();
                    Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                    intent.putExtra("folder_name", repos.get(position).getTitle());
                    intent.putExtra("folder_path", repos.get(position).getPath());
                    intent.putExtra("folder_date", repos.get(position).getDate());
                    startActivityForResult(intent, FILE_CHOOSER);
                } catch (SQLException s) {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.database_error_open),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                db_action.closeDB();
            }
        });

        view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, CM_COMMIT, 0, getString(R.string.cm_main_commit));
                menu.add(0, CM_UPDATE, 0, getString(R.string.cm_main_update));
                menu.add(0, CM_DELETE, 0, getString(R.string.cm_main_delete));
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem menu_item) {
        /* Notice, maybe error */
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menu_item.getMenuInfo();
        System.out.println(info.position);
        DBAction db_action = new DBAction(MainActivity.this);
        try {
            db_action.openDB(true);
        } catch (SQLException s) {
            Toast.makeText(
                    MainActivity.this,
                    getString(R.string.database_error_open),
                    Toast.LENGTH_SHORT
            ).show();
            db_action.closeDB();
            return false;
        }
        List<Repo> repos = db_action.listRepos();
        switch (menu_item.getItemId()) {
            case CM_COMMIT:
                /* Do something */
                break;
            case CM_UPDATE:
                pd_cloning = new ProgressDialog(MainActivity.this);
                pd_cloning.setMessage(getString(R.string.clone_pd));
                pd_cloning.setCancelable(false);
                pd_cloning.show();
                /* 开启一个新的线程用于clone */ /* Msg */
                uri = repos.get(info.position).getContent();

                /* ****************************************************************** */
                final HandlerThread update_thread = new HandlerThread("cloneThread");
                update_thread.start();
                Handler handle = new Handler(update_thread.getLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 1) {
                            update_thread.getLooper().quit();
                            adapter.notifyDataSetChanged();
                        }
                    }
                };
                handle.post(cloneThread);
                /* ****************************************************************** */

                break;
            case CM_DELETE: /* Msg */
                String path = repos.get(info.position).getPath();
                FileUtils.deleteQuietly(new File(path));
                db_action.deleteRepo(repos.get(info.position));

                /* ****************************************************************** */
                final HandlerThread delete_thread = new HandlerThread("listThread");
                delete_thread.start();
                Handler handler = new Handler(delete_thread.getLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 1) {
                            delete_thread.quit();
                            adapter.notifyDataSetChanged();
                        }
                    }
                };
                handler.post(listThread);
                /* ****************************************************************** */

                break;
            default:
                break;
        }
        db_action.closeDB();
        return super.onContextItemSelected(menu_item);
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
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        search_view.clearFocus();
                        /* 创建一个ProgressDialog用于提示 */
                        pd_cloning = new ProgressDialog(MainActivity.this);
                        pd_cloning.setMessage(getString(R.string.clone_pd));
                        pd_cloning.setCancelable(false);
                        pd_cloning.show();
                        /* 开启一个新的线程用于clone */
                        uri = text;
                        /*
                        HandlerThread clone_thread = new HandlerThread("cloneThread");
                        clone_thread.start();
                        Handler clone_handle = new Handler(clone_thread.getLooper());
                        clone_handle.post(cloneThread);

                        HandlerThread refresh_thread = new HandlerThread("listThread");
                        refresh_thread.start();
                        Handler refresh_handle = new Handler(refresh_thread.getLooper());
                        refresh_handle.post(listThread); */

                        /* ***************************************************************** */
                        final HandlerThread clone_thread = new HandlerThread("cloneThread");
                        clone_thread.start();
                        clone_handle = new Handler(clone_thread.getLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.what == 1) {
                                    clone_thread.quit();
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        };
                        clone_handle.post(cloneThread);
                        /* ***************************************************************** */

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

    /* 开启一个新线程用于刷新ListView */
    Runnable listThread = new Runnable() {
        @Override
        public void run() {
            item.clear();
            DBAction db_action = new DBAction(MainActivity.this);
            try {
                db_action.openDB(false);
                List<Repo> repos = db_action.listRepos();
                for (int i = 0; i < repos.size(); i++) {
                    item.add(
                            new MainListViewItem(
                                    getResources().getDrawable(R.drawable.ic_filetype_folder),
                                    repos.get(i).getTitle(),
                                    repos.get(i).getContent(),
                                    repos.get(i).getDate(),
                                    /* And Need to set status */
                                    new ImageButton(MainActivity.this)
                            )
                    );
                }
            } catch (SQLException s) {
                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.database_error_open),
                        Toast.LENGTH_SHORT
                ).show();
            }
            db_action.closeDB();
            list_handle.sendEmptyMessage(1); //
        }
    };

    /* 开启一个新线程用于git clone */
    Runnable cloneThread = new Runnable() {
        @Override
        public void run() {
            /* 从输入的uri中截取需要的信息 */
            String str[] = uri.split("/");
            /* 获取Repo的名称（设置为ListViewItem的标题） */
            String title = str[str.length - 1].substring(
                    0,
                    str[str.length - 1].lastIndexOf(".")
            );
            /* 获取Repo的讯息（从哪里来） */
            String content = uri;
            /* 设置存放Repo的路径 */
            String folder_path = MainActivity.this.getFilesDir()
                    + File.separator
                    + title;
            /* 获取clone Repo时的时间 */
            Calendar now = Calendar.getInstance();
            String date = now.get(Calendar.YEAR)
                    + "-"
                    + (now.get(Calendar.MONTH) + 1)
                    + "-"
                    + now.get(Calendar.DATE);

            /* 设置数据库需要的数据 */
            Repo repo = new Repo();
            repo.setTitle(title);
            repo.setContent(content);
            repo.setDate(date);
            repo.setState(Repo.State.Unmark);
            repo.setPath(folder_path);

            /* git clone的初始化设置 */
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

            /* 开始git clone */
            DBAction db_action = new DBAction(MainActivity.this);
            try {
                /* 打开数据库 */
                db_action.openDB(true);
                /* 检查Repo是否重复（根据来源） */
                boolean er = db_action.existRepo(content);
                try {
                    try {
                        FileUtils.deleteDirectory(new File(folder_path));
                    } catch (IOException i) {
                        pd_cloning.dismiss();
                        Toast.makeText(
                                MainActivity.this,
                                getString(R.string.unknown_error),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                    clone.call();
                    /* 如果Repo在数据库中重复，则更新信息 */
                    if (er) {
                        db_action.updateRepo(repo);
                    /* 否则将新建的Repo加入到数据库中 */
                    } else {
                        db_action.newRepo(repo);
                    }
                    
                    /* 及时刷新ListView */
                    /* ********************************************************** */
                    final HandlerThread thread = new HandlerThread("listThread");
                    thread.start();
                    list_handle = new Handler(thread.getLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 1) {
                                thread.quit();
                            }
                        }
                    };
                    list_handle.post(listThread);
                    /* ********************************************************** */

                    pd_cloning.dismiss();
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.clone_successful),
                            Toast.LENGTH_SHORT
                    ).show();
                } catch (GitAPIException g) {
                    pd_cloning.dismiss();
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.git_error_clone),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } catch (SQLException e) {
                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.database_error_open),
                        Toast.LENGTH_SHORT
                ).show();
            }
            db_action.closeDB();
            clone_handle.sendEmptyMessage(1); //
        }
    };
}