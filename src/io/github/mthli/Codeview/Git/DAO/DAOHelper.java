package io.github.mthli.Codeview.Git.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import io.github.mthli.Codeview.Git.Repo;

public class DAOHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "repo.db";
    private static final int DATABASE_VERSION = 1;

    public DAOHelper(Context context) {
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
    public void onUpgrade(
            SQLiteDatabase db,
            int old_version,
            int new_version
    ) {
        /* Nothing */
    }
}
