/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        String SQL_CREATE_FIXTURES_TABLE =  "CREATE TABLE " + FifaEntry.TABLE_NAME + " ("
                + FifaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FifaEntry.COLUMN_TEAM1_NAME + " TEXT NOT NULL, "
                + FifaEntry.COLUMN_TEAM2_NAME + " TEXT NOT NULL, "
                + FifaEntry.COLUMN_DATE + " DATE NOT NULL, "
                + FifaEntry.COLUMN_VENUE + " TEXT NOT NULL, "
                + FifaEntry.COLUMN_TIME + " TIME NOT NULL );";

        db.execSQL(SQL_CREATE_FIXTURES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}