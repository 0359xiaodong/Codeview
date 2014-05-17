package io.github.mthli.Codeview.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBAction {
    private DBHelper helper;
    private SQLiteDatabase database;

    public DBAction(Context context) {
        helper = new DBHelper(context);
    }

    public void openDB(boolean rw) throws SQLException {
        if (rw) {
            database = helper.getWritableDatabase();
        } else {
            database = helper.getReadableDatabase();
        }
    }

    public void closeDB() {
        helper.close();
    }

    /* Bug in here */
    public boolean existRepo(String content) {
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
        values.put(Repo.DATE, repo.getDate());
        values.put(Repo.STATE, repo.getState().name());
        database.insert(Repo.TABLE, null, values);
    }

    public void updateRepo(Repo repo) {
        ContentValues values = new ContentValues();
        values.put(Repo.DATE, repo.getDate());
        values.put(Repo.STATE, repo.getState().name());
        database.update(
                Repo.TABLE,
                values,
                Repo.CONTENT + "=?",
                new String[] {repo.getContent()}
        );
    }

    public void deleteRepo(Repo repo) {
        database.delete(
                Repo.TABLE,
                Repo.CONTENT + "=?",
                new String[] {repo.getContent()}
        );
    }

    public List<Repo> listRepos() {
        List<Repo> result = new ArrayList<Repo>();
        Cursor cursor = database.query(
                Repo.TABLE,
                new String[]{
                        Repo.ID,
                        Repo.TITLE,
                        Repo.CONTENT,
                        Repo.DATE,
                        Repo.STATE
                },
                null,
                null,
                null,
                null,
                Repo.CONTENT
        );

        if(cursor == null) {
            return result;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Repo repo = readAsRepo(cursor);
            result.add(repo);
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    private Repo readAsRepo(Cursor cursor) {
        Repo repo = new Repo();
        repo.setId(cursor.getInt(0));
        repo.setTitle(cursor.getString(1));
        repo.setContent(cursor.getString(2));
        repo.setDate(cursor.getString(3));
        repo.setState(Repo.State.valueOf(cursor.getString(4)));
        return repo;
    }
}
