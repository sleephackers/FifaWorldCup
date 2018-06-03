package com.example.android.fifaworldcup.data;

import android.content.ContentProvider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.android.fifaworldcup.data.FifaContract.FifaEntry;
import android.util.Log;

public class FifaProvider extends ContentProvider {

    private static final int FIXTURES = 100;

    private static final int FIXTURES_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI("com.example.android.fifaworldcup", "fixtures", FIXTURES);
        sUriMatcher.addURI("com.example.android.fifaworldcup", "fixtures/#", FIXTURES_ID);
    }


    public static final String LOG_TAG = FifaProvider.class.getSimpleName();
    private FifaDbHelper mDbHelper;


    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper = new FifaDbHelper(getContext());

        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case FIXTURES:

                cursor = database.query(FifaContract.FifaEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FIXTURES_ID:

                selection = FifaContract.FifaEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(FifaContract.FifaEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FIXTURES:
                return insertFixture(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertFixture(Uri uri, ContentValues values) {
        String name1 = values.getAsString(FifaEntry.COLUMN_TEAM1_NAME);
        if (name1 == null) {
            throw new IllegalArgumentException("Match requires a team name");
        }
        String name2 = values.getAsString(FifaEntry.COLUMN_TEAM1_NAME);
        if (name2 == null) {
            throw new IllegalArgumentException("Match requires a team name");
        }
        String date = values.getAsString(FifaEntry.COLUMN_DATE);
        if (date == null ) {
            throw new IllegalArgumentException("Match requires valid date");
        }
        String venue = values.getAsString(FifaEntry.COLUMN_VENUE);
        if (venue == null ) {
            throw new IllegalArgumentException("Match requires valid venue");
        }
        String time = values.getAsString(FifaEntry.COLUMN_TIME);
        if (venue == null ) {
            throw new IllegalArgumentException("Match requires valid venue");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(FifaContract.FifaEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FIXTURES:
                return updateFixture(uri, contentValues, selection, selectionArgs);
            case FIXTURES_ID:

                selection = FifaContract.FifaEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateFixture(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);

        }
    }

    private int updateFixture(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(FifaEntry.COLUMN_TEAM1_NAME)) {
                String name1 = values.getAsString(FifaEntry.COLUMN_TEAM1_NAME);
                if (name1 == null) {
                    throw new IllegalArgumentException("Match requires a team name");
                }

        }
        if (values.containsKey(FifaEntry.COLUMN_TEAM2_NAME)) {
            String name2 = values.getAsString(FifaEntry.COLUMN_TEAM2_NAME);
            if (name2 == null) {
                throw new IllegalArgumentException("Match requires a team name");
            }

        }


        if (values.containsKey(FifaEntry.COLUMN_DATE)) {
            String date = values.getAsString(FifaEntry.COLUMN_DATE);
            if (date == null ) {
                throw new IllegalArgumentException("Match requires valid date");
            }
        }


        if (values.containsKey(FifaEntry.COLUMN_VENUE)) {
            String venue = values.getAsString(FifaEntry.COLUMN_VENUE);
            if (venue == null ) {
                throw new IllegalArgumentException("Match requires valid venue");
            }
        }
        if (values.containsKey(FifaEntry.COLUMN_TIME)) {
            String time = values.getAsString(FifaEntry.COLUMN_TIME);
            if (time == null ) {
                throw new IllegalArgumentException("Match requires valid time");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(FifaEntry.TABLE_NAME, values, selection, selectionArgs);



        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int rowsDeleted;
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case FIXTURES:
                rowsDeleted = database.delete(FifaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FIXTURES_ID:
                selection = FifaEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(FifaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return rowsDeleted;

    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FIXTURES:
                return FifaEntry.CONTENT_LIST_TYPE;
            case FIXTURES_ID:
                return FifaEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
