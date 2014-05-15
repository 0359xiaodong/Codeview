package io.github.mthli.Codeview.Git;

import io.github.mthli.Codeview.Git.Error.*;

import org.apache.commons.io.FileUtils;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.NotSupportedException;

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class GitHelper {
    private static final String TAG = "GitHelper";

    public static void clone(
            String url,
            String localPath,
            String user,
            String password
    ) throws GitError {
        CloneCommand clone = Git.cloneRepository();
        clone.setTimeout(30);
        clone.setURI(url);
        clone.setDirectory(new File(localPath));
        if ((user != null) && (password != null)) {
            UsernamePasswordCredentialsProvider access = new UsernamePasswordCredentialsProvider(
                    user,
                    password
            );
            clone.setCredentialsProvider(access);
        }

        try {
            FileUtils.deleteDirectory(new File(localPath));
            clone.call();
            return;
        } catch (InvalidRemoteException e) {
            throw new NotGitRepoError();
        } catch (TransportException e) {
            /* Auth fail or Connection fail */
        } catch (GitAPIException e) {
            /* Nothing */
        } catch (IOException e) {
        } catch (JGitInternalException e) {
            /* Connection fail or GitError */
            if (e.getCause() instanceof NotSupportedException) {
                throw new ConnectionError();
            } else {
                throw new GitError();
            }
        }
        throw new GitError();
    }

    public static void deleteRepo(String localPath) throws IOException {
        FileUtils.deleteDirectory(new File(localPath));
    }
}