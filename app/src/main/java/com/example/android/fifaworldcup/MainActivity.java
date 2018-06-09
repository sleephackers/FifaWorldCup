
package com.example.android.fifaworldcup;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.fifaworldcup.data.FifaContract.FifaEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int FIFA_LOADER = 0;
    FifaCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView fixtureListView = (ListView) findViewById(R.id.fifaListView);

        mCursorAdapter = new FifaCursorAdapter(this, null);
        fixtureListView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(FIFA_LOADER, null, this);

        fixtureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Uri currentFixtureUri = ContentUris.withAppendedId(FifaEntry.CONTENT_URI, id);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("CHOOSE AN ACTION!");
                builder.setPositiveButton("VIEW", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                        intent.setData(currentFixtureUri);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("EDIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                        intent.setData(currentFixtureUri);
                        startActivity(intent);

                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Define a projection of columns we care abt
        String[] projection = {FifaEntry._ID, FifaEntry.COLUMN_TEAM1_NAME, FifaEntry.COLUMN_TEAM2_NAME};

        return new CursorLoader(this, FifaEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

}