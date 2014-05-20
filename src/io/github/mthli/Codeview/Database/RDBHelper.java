package io.github.mthli.Codeview.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "repos.db";
    private static final int DATABASE_VERSION = 1;

    public RDBHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Repo.CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* Nothing */
    }
}
