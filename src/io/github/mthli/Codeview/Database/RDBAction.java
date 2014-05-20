package io.github.mthli.Codeview.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RDBAction {
    private RDBHelper rdbHelper;
    private SQLiteDatabase database;

    public RDBAction(Context context) {
        rdbHelper = new RDBHelper(context);
    }

    public void openDatabase(boolean rw) throws SQLException {
        if (rw) {
            database = rdbHelper.getWritableDatabase();
        } else {
            database = rdbHelper.getReadableDatabase();
        }
    }

    public void closeDatabase() {
        rdbHelper.close();
    }

    public boolean checkRepo(String content) {
        Cursor cursor = database.query(
                Repo.TABLE,
                new String[] {Repo.CONTENT},
                Repo.CONTENT + "=?",
                new String[] {content},
                null,
                null,
                null
        );
        if (cursor != null) {
            boolean result = false;
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
            return result;
        }
        return false;
    }

    public void newRepo(Repo repo) {
        ContentValues values = new ContentValues();
        values.put(Repo.TITLE, repo.getTitle());
        values.put(Repo.CONTENT, repo.getContent());
        values.put(Repo.PATH, repo.getPath());
        database.insert(Repo.TABLE, null, values);
    }

    public void updateRepo(Repo repo) {
        // ContentValues values = new ContentValues();
        /* Do nothing */
    }

    public void deleteRepo(Repo repo) {
        database.delete(
                Repo.TABLE,
                Repo.CONTENT + "=?",
                new String[] {repo.getContent()}
        );
    }

    public List<Repo> listRepos() {
        List<Repo> repos = new ArrayList<Repo>();
        Cursor cursor = database.query(
                Repo.TABLE,
                new String[]{
                        Repo.TITLE,
                        Repo.CONTENT,
                        Repo.PATH
                },
                null,
                null,
                null,
                null,
                Repo.CONTENT
        );
        if(cursor == null) {
            return repos;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Repo repo = readRepo(cursor);
            repos.add(repo);
            cursor.moveToNext();
        }
        cursor.close();
        return repos;
    }

    private Repo readRepo(Cursor cursor) {
        Repo repo = new Repo();
        repo.setTitle(cursor.getString(0));
        repo.setContent(cursor.getString(1));
        repo.setPath(cursor.getString(2));
        return repo;
    }
}
