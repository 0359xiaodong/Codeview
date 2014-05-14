package io.github.mthli.Codeview.Git.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import io.github.mthli.Codeview.Git.Repo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private DAOHelper helper;
    private SQLiteDatabase database;

    public DAO(Context context) {
        helper = new DAOHelper(context);
    }

    public void open(boolean rw) throws SQLException {
        if(rw) {
            database = helper.getWritableDatabase();
        } else {
            database = helper.getReadableDatabase();
        }
    }

    public void close() {
        helper.close();
    }

    public boolean repoExist(String folder) {
        Cursor cursor = database.query(
                Repo.TABLE,
                new String[] {Repo.ID},
                Repo.FOLDER + "=?",
                new String[] {folder},
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

    public void addRepo(Repo repo) {
        ContentValues values = new ContentValues();

        values.put(Repo.FOLDER, repo.getFolder());
        values.put(Repo.NAME, repo.getName());
        values.put(Repo.ADDRESS, repo.getAddress());
        values.put(Repo.STATE, repo.getState().name());
        database.insert(Repo.TABLE, null, values);
    }

    public List<Repo> listAll() {
        List<Repo> result = new ArrayList<Repo>();

        Cursor cursor = database.query(
                Repo.TABLE,
                new String[] {
                        Repo.ID,
                        Repo.FOLDER,
                        Repo.NAME,
                        Repo.ADDRESS,
                        Repo.STATE,
                        Repo.ERROR
                },
                null,
                null,
                null,
                null,
                Repo.NAME
        );
        if (cursor == null) {
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
        repo.setFolder(cursor.getString(1));
        repo.setName(cursor.getString(2));
        repo.setAddress(cursor.getString(3));
        repo.setState(Repo.State.valueOf(cursor.getString(4)));
        repo.setError(cursor.getString(5));

        return repo;
    }

    public void update(Repo repo) {
        ContentValues values = new ContentValues();

        values.put(Repo.STATE, repo.getState().name());
        values.put(Repo.ERROR, repo.getError());
        database.update(
                Repo.TABLE,
                values,
                Repo.FOLDER + "=?",
                new String[] {
                        repo.getFolder()
                }
        );
    }

    public void delete(String folder) {
        database.delete(
                Repo.TABLE,
                Repo.FOLDER + "=?",
                new String[] {
                        folder
                }
        );
    }
}
