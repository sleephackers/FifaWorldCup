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
package com.example.android.fifaworldcup;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.example.android.fifaworldcup.data.FifaContract.FifaEntry;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int FIFA_LOADER=0;
    FifaCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button fab=(Button)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView fixtureListView = (ListView) findViewById(R.id.fifaListView);

         mCursorAdapter=new FifaCursorAdapter(this,null);
        fixtureListView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(FIFA_LOADER,null,this);

        fixtureListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentFixtureUri = ContentUris.withAppendedId(FifaEntry.CONTENT_URI, id);
                intent.setData(currentFixtureUri);
                startActivity(intent);

            }
        });


    }


 @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        //Define a projection of columns we care abt
        String[] projection={FifaEntry._ID,FifaEntry.COLUMN_TEAM1_NAME,FifaEntry.COLUMN_TEAM2_NAME};

        return new CursorLoader(this,FifaEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mCursorAdapter.swapCursor(null);

    }

}