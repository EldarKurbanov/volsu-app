package ru.iss.vanil.volsu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class AppDatabaseHelper extends SQLiteOpenHelper {
    AppDatabaseHelper(@Nullable Context context) {
        super(context, AppR.constantsDatabaseSQLite.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table semester ("
                + "id integer primary key autoincrement,"
                + "subject text,"
                + "point integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
