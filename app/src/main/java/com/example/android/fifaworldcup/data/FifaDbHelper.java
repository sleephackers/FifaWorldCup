package com.example.android.fifaworldcup.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.fifaworldcup.data.FifaContract.FifaEntry;

public class FifaDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FifaDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "matches.db";

    private static final int DATABASE_VERSION = 1;

    public FifaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FIXTURES_TABLE = "CREATE TABLE " + FifaEntry.TABLE_NAME + " ("
                + FifaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FifaEntry.COLUMN_TEAM1_NAME + " TEXT NOT NULL, "
                + FifaEntry.COLUMN_TEAM2_NAME + " TEXT NOT NULL, "
                + FifaEntry.COLUMN_DATE + " DATE NOT NULL, "
                + FifaEntry.COLUMN_VENUE + " TEXT NOT NULL, "
                + FifaEntry.COLUMN_TIME + " TIME NOT NULL, "
                + FifaEntry.COLUMN_TEAM1_ICON + " TEXT NOT NULL, "
                + FifaEntry.COLUMN_TEAM2_ICON + " TEXT NOT NULL );";

        db.execSQL(SQL_CREATE_FIXTURES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}