package io.github.mthli.Codeview.Git;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import io.github.mthli.Codeview.Git.DAO.DAO;
import io.github.mthli.Codeview.Git.DAO.Repo;
import io.github.mthli.Codeview.Git.Error.AuthFailError;
import io.github.mthli.Codeview.Git.Error.ConnectionError;
import io.github.mthli.Codeview.Git.Error.GitError;
import io.github.mthli.Codeview.Git.Error.NotGitRepoError;
import io.github.mthli.Codeview.R;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class GitService extends IntentService {
    static final String TAG = "GitService";

    public enum Command {
        Clone,
        Delete
    }

    public static final String BROADCAST_REFRESH = "io.github.mthli.Git.Codeview.GitService.REFRESH";
    public static final String COMMAND = "key_command";
    public static final String REPO = "key_repo";
    public static final String AUTH_PASSWD = "key_password";

    private DAO dao;
    private Handler handler;

    public GitService() {
        super("GitClone");
    }

    public void onCreate() {
        super.onCreate();
        dao = new DAO(this);

        try {
            dao.open(true);
        } catch (SQLException s) {
            System.out.println("dao open false");
        }

        handler = new Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Command cmd = (Command) intent.getSerializableExtra(COMMAND);
        switch (cmd) {
            case Clone:
                clone(intent);
                break;
            case Delete:
                delete(intent);
                break;
        }
    }

    private void clone(Intent intent) {
        final Repo repo = (Repo) intent.getSerializableExtra(REPO);
        String passwd = intent.getStringExtra(GitService.AUTH_PASSWD);
        String path = this.getFilesDir().getPath() + File.separator + repo.getFolder();

        try {
            GitHelper.clone(
                    repo.getAddress(),
                    path,
                    "mthli",
                    "5148232419"
            );
            repo.setState(Repo.State.Local);
            repo.setError("");
        } catch (ConnectionError e) {
            repo.setState(Repo.State.Error);
            repo.setError(getString(R.string.git_error_connect));
        } catch (NotGitRepoError e) {
            repo.setState(Repo.State.Error);
            String lcAddress = repo.getAddress().toLowerCase();
            if (!lcAddress.endsWith(".git")) {
                repo.setError(getString(R.string.git_error_not_git_guess));
            } else {
                repo.setError(getString(R.string.git_error_not_git));
            }
        } catch (AuthFailError e) {
            repo.setState(Repo.State.Error);
            repo.setError(getString(R.string.git_error_auth));
        } catch (GitError e) {
            repo.setState(Repo.State.Error);
            repo.setError(getString(R.string.git_error_generic));
        }
        dao.update(repo);
        notifyRepoList();
    }

    private void notifyRepoList() {
        Intent notify = new Intent();
        notify.setAction(BROADCAST_REFRESH);
        sendBroadcast(notify);
    }

    private void delete(Intent intent) {
        Repo repo = (Repo) intent.getSerializableExtra(REPO);
        String path = this.getFilesDir().getPath() + File.separator + repo.getFolder();

        try {
            GitHelper.deleteRepo(path);
        } catch (IOException e) {

        }
    }

    private void toast(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(
                        GitService.this,
                        message,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
