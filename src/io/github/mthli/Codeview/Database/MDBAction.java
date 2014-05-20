package io.github.mthli.Codeview.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MDBAction {
    private MDBHelper mdbHelper;
    private SQLiteDatabase database;

    public MDBAction(Context context) {
        mdbHelper = new MDBHelper(context);
    }

    public void openDatabase(boolean rw) throws SQLException {
        if (rw) {
            database = mdbHelper.getWritableDatabase();
        } else {
            database = mdbHelper.getReadableDatabase();
        }
    }

    public void closeDatabase() {
        mdbHelper.close();
    }

    public boolean checkMark(String content) {
        Cursor cursor = database.query(
                Mark.TABLE,
                new String[] {Mark.CONTENT},
                Mark.CONTENT + "=?",
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

    public void newMark(Mark mark) {
        ContentValues values = new ContentValues();
        values.put(Mark.TITLE, mark.getTitle());
        values.put(Mark.CONTENT, mark.getContent());
        database.insert(Mark.TABLE, null, values);
    }

    public void updateMark(Mark mark) {
        // ContentValues values = new ContentValues();
        /* Do nothing */
    }

    public void unMark(Mark mark) {
        database.delete(
                Mark.TABLE,
                Mark.CONTENT + "=?",
                new String[] {mark.getContent()}
        );
    }

    public List<Mark> listMarks() {
        List<Mark> marks = new ArrayList<Mark>();
        Cursor cursor = database.query(
                Mark.TABLE,
                new String[]{
                        Mark.TITLE,
                        Mark.CONTENT
                },
                null,
                null,
                null,
                null,
                Mark.CONTENT
        );
        if(cursor == null) {
            return marks;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Mark mark = readMark(cursor);
            marks.add(mark);
            cursor.moveToNext();
        }
        cursor.close();
        return marks;
    }

    private Mark readMark(Cursor cursor) {
        Mark mark = new Mark();
        mark.setTitle(cursor.getString(0));
        mark.setContent(cursor.getString(1));
        return mark;
    }
}
