package com.example.android.fifaworldcup;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.fifaworldcup.data.FifaContract;

public class MatchesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MATCH_LOADER = 0;
    FifaCursorAdapter CursorAdapter;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        ListView matchesListView = (ListView) findViewById(R.id.MatchListView);
        CursorAdapter = new FifaCursorAdapter(this, null);
        matchesListView.setAdapter(CursorAdapter);
        getLoaderManager().initLoader(MATCH_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {FifaContract.FifaEntry._ID, FifaContract.FifaEntry.COLUMN_TEAM1_NAME, FifaContract.FifaEntry.COLUMN_TEAM2_NAME};
        String selection = FifaContract.FifaEntry.COLUMN_TEAM1_NAME + "=? OR " + FifaContract.FifaEntry.COLUMN_TEAM2_NAME + "=?";
        String[] selectionArgs = new String[]{name, name};
        return new CursorLoader(this, FifaContract.FifaEntry.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        CursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        CursorAdapter.swapCursor(null);

    }
}
