package io.github.mthli.Codeview.Main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import io.github.mthli.Codeview.Database.RDBAction;
import io.github.mthli.Codeview.Database.Repo;
import io.github.mthli.Codeview.FileChooser.FileChooserActivity;
import io.github.mthli.Codeview.Other.AboutActivity;
import io.github.mthli.Codeview.R;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ActionBar.OnNavigationListener {
    private ListView listView;
    private SearchView searchView;
    private MainAdapter mainAdapter;
    private List<MainItem> mainItems;
    private ProgressDialog progressDialog;
    private String url;

    final private int CM_MARK = 0;
    final private int CM_COMMIT = 1;
    final private int CM_UPDATE = 2;
    final private int CM_DELETE = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(
                new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                getString(R.string.drop_down_repo),
                                getString(R.string.drop_down_mark)
                        }
                ),
                this
        );

        mainItems = new ArrayList<MainItem>();
        refreshList();
        mainAdapter = new MainAdapter(
                this,
                R.layout.main_item,
                mainItems
        );
        listView = (ListView) findViewById(R.id.main);
        listView.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* 传递正确的数据给FileChooser */
                RDBAction rdbAction = new RDBAction(MainActivity.this);
                try {
                    rdbAction.openDatabase(false);
                    List<Repo> repos = rdbAction.listRepos();
                    Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                    intent.putExtra("folder_name", repos.get(position).getTitle());
                    intent.putExtra("folder_path", repos.get(position).getPath());
                    startActivity(intent);
                } catch (SQLException s) {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.database_error_open),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                rdbAction.closeDatabase();
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, CM_MARK, 0, getString(R.string.cm_mark));
                menu.add(0, CM_COMMIT, 0, getString(R.string.cm_commit));
                menu.add(0, CM_UPDATE, 0, getString(R.string.cm_update));
                menu.add(0, CM_DELETE, 0, getString(R.string.cm_delete));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        RDBAction rdbAction = new RDBAction(MainActivity.this);
        switch (menuItem.getItemId()) {
            case CM_MARK:
                /* Do something */
                break;
            case CM_COMMIT:
                /* Do something */
                break;
            case CM_UPDATE:
                /* Do something */
                try {
                    rdbAction.openDatabase(true);
                } catch (SQLException s) {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.database_error_open),
                            Toast.LENGTH_SHORT
                    ).show();
                    rdbAction.closeDatabase();
                    break;
                }
                List<Repo> repos_0 = rdbAction.listRepos();
                String content = repos_0.get(info.position).getContent();
                url = content;
                /* 创建一个ProgressDialog用于提示 */
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage(getString(R.string.clone_pd_update));
                progressDialog.setCancelable(false);
                progressDialog.show();
                HandlerThread cloneThread_0 = new HandlerThread("cloneThread_0");
                cloneThread_0.start();
                Handler cloneHandler = new Handler(cloneThread_0.getLooper());
                cloneHandler.post(cloneThread);
                rdbAction.closeDatabase();
                refreshList();
                mainAdapter.notifyDataSetChanged();
                break;
            case CM_DELETE:
                /* Do something */
                try {
                    rdbAction.openDatabase(true);
                } catch (SQLException s) {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.database_error_open),
                            Toast.LENGTH_SHORT
                    ).show();
                    rdbAction.closeDatabase();
                    break;
                }
                List<Repo> repos_1 = rdbAction.listRepos();
                String path = repos_1.get(info.position).getPath();
                FileUtils.deleteQuietly(new File(path));
                rdbAction.deleteRepo(repos_1.get(info.position));
                mainItems.remove(info.position);
                rdbAction.closeDatabase();
                refreshList();
                mainAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.main_menu_search).getActionView();
        searchView.setQueryHint(getString(R.string.menu_search));

        SearchView.OnQueryTextListener svListener = new SearchView.OnQueryTextListener() {
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
                                getString(R.string.url_error_https),
                                Toast.LENGTH_SHORT
                        ).show();
                    } else if ((!text.endsWith(".git"))) {
                        Toast.makeText(
                                MainActivity.this,
                                getString(R.string.url_error_git),
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        /* Do something */
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        /* 创建一个ProgressDialog用于提示 */
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage(getString(R.string.clone_pd_new));
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        /* 开启一个新的线程用于clone */
                        url = text;
                        HandlerThread cloneThread_1 = new HandlerThread("cloneThread_1");
                        cloneThread_1.start();
                        Handler cloneHandler = new Handler(cloneThread_1.getLooper());
                        cloneHandler.post(cloneThread);
                        /* refresh */
                        refreshList();
                        mainAdapter.notifyDataSetChanged();
                    }
                }
                return true;
            }
        };
        searchView.setOnQueryTextListener(svListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_about:
                Intent intent_about = new Intent(this, AboutActivity.class);
                startActivity(intent_about);
                return true;
            /* Do something */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Refresh ListView */
    public void refreshList() {
        mainItems.clear();
        RDBAction RDBAction = new RDBAction(MainActivity.this);
        try {
            RDBAction.openDatabase(false);
            List<Repo> repos = RDBAction.listRepos();
            for (int i = 0; i < repos.size(); i++) {
                mainItems.add(
                        new MainItem(
                                getResources().getDrawable(R.drawable.ic_filetype_folder),
                                repos.get(i).getTitle(),
                                repos.get(i).getContent()
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
        RDBAction.closeDatabase();
    }

    /* 开启一个新线程用于git clone */
    Runnable cloneThread = new Runnable() {
        @Override
        public void run() {
            /* 从输入的uri中截取需要的信息 */
            String str[] = url.split("/");
            /* 获取Repo的名称（设置为ListViewItem的标题） */
            String title = str[str.length - 1].substring(
                    0,
                    str[str.length - 1].lastIndexOf(".")
            );
            /* 获取Repo的讯息（从哪里来） */
            String content = url;
            /* 设置存放Repo的路径 */
            String folder_path = MainActivity.this.getFilesDir()
                    + File.separator
                    + title;

            /* 设置数据库需要的数据 */
            Repo repo = new Repo();
            repo.setTitle(title);
            repo.setContent(content);
            repo.setPath(folder_path);

            /* git clone的初始化设置 */
            CloneCommand clone = Git.cloneRepository();
            clone.setTimeout(30);
            clone.setURI(url);
            clone.setDirectory(new File(folder_path));
            UsernamePasswordCredentialsProvider access =
                    new UsernamePasswordCredentialsProvider(
                            getString(R.string.clone_username),
                            getString(R.string.clone_password)
                    );
            clone.setCredentialsProvider(access);

            /* 开始git clone */
            boolean isSuccessful = true;
            RDBAction RDBAction = new RDBAction(MainActivity.this);
            try {
                /* 打开数据库 */
                RDBAction.openDatabase(true);
                /* 检查Repo是否重复（根据来源） */
                try {
                    try {
                        FileUtils.deleteDirectory(new File(folder_path));
                    } catch (IOException i) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                MainActivity.this,
                                getString(R.string.unknown_error),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                    clone.call();
                } catch (GitAPIException g) {
                    progressDialog.dismiss();
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.git_error_clone),
                            Toast.LENGTH_SHORT
                    ).show();
                    isSuccessful = false;
                } catch (JGitInternalException j) {
                    progressDialog.dismiss();
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.git_error_clone),
                            Toast.LENGTH_SHORT
                    ).show();
                    isSuccessful = false;
                }
                /* 如果Repo在数据库中重复，则更新信息 */
                if (isSuccessful) {
                    if (!RDBAction.checkRepo(content)) {
                        RDBAction.newRepo(repo);
                    }
                    refreshList();
                    progressDialog.dismiss();
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.clone_successful),
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
            RDBAction.closeDatabase();
        }
    };
}
