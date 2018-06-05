package com.example.android.fifaworldcup;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.fifaworldcup.data.FifaContract;


public class FifaCursorAdapter extends CursorAdapter {


    public FifaCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name1TextView = (TextView) view.findViewById(R.id.name1);
        TextView name2TextView = (TextView) view.findViewById(R.id.name2);

        int name1ColumnIndex = cursor.getColumnIndex(FifaContract.FifaEntry.COLUMN_TEAM1_NAME);
        int name2ColumnIndex = cursor.getColumnIndex(FifaContract.FifaEntry.COLUMN_TEAM2_NAME);

        String team1 = cursor.getString(name1ColumnIndex);
        String team2 = cursor.getString(name2ColumnIndex);

        name1TextView.setText(team1);
        name2TextView.setText(team2);
    }
}

