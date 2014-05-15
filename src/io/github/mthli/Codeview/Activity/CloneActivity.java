package io.github.mthli.Codeview.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class CloneActivity extends Activity {
    private String username;
    private String password;
    public String uri;

    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);

        username = getUsername();
        password = getPassword();
        uri = getUri();

        CloneCommand clone = Git.cloneRepository();
        clone.setTimeout(30);
        clone.setURI(uri);
        clone.setDirectory();
        UsernamePasswordCredentialsProvider access =
                new UsernamePasswordCredentialsProvider(
                        username,
                        password
                );
        clone.setCredentialsProvider(access);
        try {
            try {
                clone.call();
            } catch (VerifyError v) {

            } catch (NetworkOnMainThreadException n) {

            }
        } catch (GitAPIException g) {

        }
    }

    protected String getUsername() {
        return;
    }

    protected String getPassword() {
        return;
    }

    protected String getUri() {
        return;
    }
}
