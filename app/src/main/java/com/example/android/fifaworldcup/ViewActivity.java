package com.example.android.fifaworldcup;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.fifaworldcup.data.FifaContract;

import java.io.FileNotFoundException;

public class ViewActivity extends AppCompatActivity implements
        android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_VIEW_LOADER = 0;
    private Uri mCurrentFixtureUri;
    private TextView mname1;


    private TextView mname2;

    private TextView mdate;
    private TextView mvenue;
    private TextView mtime;
    private ImageView micon1;
    private ImageView micon2;
    private String muri1;
    private String muri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Intent intent = getIntent();
        mCurrentFixtureUri = intent.getData();
        setTitle("VIEW FIXTURE");
        getLoaderManager().initLoader(EXISTING_VIEW_LOADER, null, ViewActivity.this);
        mname1 = (TextView) findViewById(R.id.team1name);
        mname2 = (TextView) findViewById(R.id.team2name);
        mdate = (TextView) findViewById(R.id.date);
        mvenue = (TextView) findViewById(R.id.venue);
        mtime = (TextView) findViewById(R.id.time);
        micon1 = (ImageView) findViewById(R.id.team1flag);
        micon2 = (ImageView) findViewById(R.id.team2flag);


    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                FifaContract.FifaEntry._ID,
                FifaContract.FifaEntry.COLUMN_TEAM1_NAME,
                FifaContract.FifaEntry.COLUMN_TEAM2_NAME,
                FifaContract.FifaEntry.COLUMN_DATE,
                FifaContract.FifaEntry.COLUMN_VENUE,
                FifaContract.FifaEntry.COLUMN_TIME,
                FifaContract.FifaEntry.COLUMN_TEAM1_ICON,
                FifaContract.FifaEntry.COLUMN_TEAM2_ICON
        };

        return new CursorLoader(this,   // Parent activity context
                mCurrentFixtureUri,         // Query the content URI for the current fixture
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int name1ColumnIndex = cursor.getColumnIndex(FifaContract.FifaEntry.COLUMN_TEAM1_NAME);
            int name2ColumnIndex = cursor.getColumnIndex(FifaContract.FifaEntry.COLUMN_TEAM2_NAME);
            int dateColumnIndex = cursor.getColumnIndex(FifaContract.FifaEntry.COLUMN_DATE);
            int venueColumnIndex = cursor.getColumnIndex(FifaContract.FifaEntry.COLUMN_VENUE);
            int timeColumnIndex = cursor.getColumnIndex(FifaContract.FifaEntry.COLUMN_TIME);
            int icon1ColumnIndex = cursor.getColumnIndex(FifaContract.FifaEntry.COLUMN_TEAM1_ICON);
            int icon2ColumnIndex = cursor.getColumnIndex(FifaContract.FifaEntry.COLUMN_TEAM2_ICON);

            final String name1 = cursor.getString(name1ColumnIndex);
            final String name2 = cursor.getString(name2ColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String venue = cursor.getString(venueColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String icon1 = cursor.getString(icon1ColumnIndex);
            String icon2 = cursor.getString(icon2ColumnIndex);

            mname1.setText(name1);
            mname2.setText(name2);
            mdate.setText(date);
            mvenue.setText(venue);
            mtime.setText(time);

            try {
                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(Uri.parse(icon1)));
                micon1.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {

                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(Uri.parse(icon2)));
                micon2.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            micon1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewActivity.this, MatchesActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("name", name1);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            micon2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewActivity.this, MatchesActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("name", name2);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });


        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        return;
    }


}
