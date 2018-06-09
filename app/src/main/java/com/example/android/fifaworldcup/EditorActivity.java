package com.example.android.fifaworldcup;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.fifaworldcup.data.FifaContract.FifaEntry;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EXISTING_FIXTURE_LOADER = 0;


    private Uri mCurrentFixtureUri;


    private EditText mname1EditText;


    private EditText mname2EditText;

    private EditText mdateEditText;
    private EditText mvenueEditText;
    private EditText mtimeEditText;
    private ImageView micon1;
    private ImageView micon2;
    private String muri1;
    private String muri2;

    private boolean fall, pic1, pic2;

    private boolean mFixtureHasChanged = false;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mFixtureHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        Intent intent = getIntent();
        mCurrentFixtureUri = intent.getData();


        if (mCurrentFixtureUri == null) {
            setTitle("INSERT FIXTURE");
            invalidateOptionsMenu();

        } else {
            setTitle("EDIT FIXTURE");


            getLoaderManager().initLoader(EXISTING_FIXTURE_LOADER, null, this);
        }

        mname1EditText = (EditText) findViewById(R.id.team1name);
        mname2EditText = (EditText) findViewById(R.id.team2name);
        mdateEditText = (EditText) findViewById(R.id.date);
        mvenueEditText = (EditText) findViewById(R.id.venue);
        mtimeEditText = (EditText) findViewById(R.id.time);
        micon1 = (ImageView) findViewById(R.id.team1flag);
        micon2 = (ImageView) findViewById(R.id.team2flag);

        mname1EditText.setOnTouchListener(mTouchListener);
        mname2EditText.setOnTouchListener(mTouchListener);
        mdateEditText.setOnTouchListener(mTouchListener);
        mvenueEditText.setOnTouchListener(mTouchListener);
        mtimeEditText.setOnTouchListener(mTouchListener);
        micon1.setOnTouchListener(mTouchListener);
        micon2.setOnTouchListener(mTouchListener);

        micon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFixtureHasChanged = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                builder.setMessage("CHOOSE AN ACTION!");
                builder.setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 10);
                    }
                });
                builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takepicture, 11);
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        micon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFixtureHasChanged = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                builder.setMessage("CHOOSE AN ACTION!");
                builder.setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 20);
                    }
                });
                builder.setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takepicture, 21);
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    pic1 = true;
                    Uri selectedImage = imageReturnedIntent.getData();
                    micon1.setImageURI(selectedImage);
                    muri1 = selectedImage.toString();

                }
                break;
            case 11:
                if (resultCode == RESULT_OK) {
                    pic1 = true;
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    photo = crop(photo);
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    micon1.setImageURI(tempUri);
                    muri1 = tempUri.toString();

                }

                break;
            case 20:
                if (resultCode == RESULT_OK) {
                    pic2 = true;
                    Uri selectedImage = imageReturnedIntent.getData();
                    micon2.setImageURI(selectedImage);
                    muri2 = selectedImage.toString();
                }
                break;
            case 21:
                if (resultCode == RESULT_OK) {
                    pic2 = true;
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    photo = crop(photo);
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    micon2.setImageURI(tempUri);
                    muri2 = tempUri.toString();
                }

                break;
        }
    }

    public Bitmap crop(Bitmap photo) {
        Bitmap yourBitmap = photo;
        Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, 1250, 1250, true);
        return resized;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    private void saveFixture() {
        String name1String = mname1EditText.getText().toString().trim();
        String name2String = mname2EditText.getText().toString().trim();
        String dateString = mdateEditText.getText().toString().trim();
        String venueString = mvenueEditText.getText().toString().trim();
        String timeString = mtimeEditText.getText().toString().trim();

        if (name1String.isEmpty() || name2String.isEmpty() || dateString.isEmpty() || venueString.isEmpty() || timeString.isEmpty()) {
            fall = false;
            Toast.makeText(this, "ENTER PROPER DETAILS",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (mCurrentFixtureUri == null)
                if (pic1 == false || pic2 == false) {
                    fall = false;
                    Toast.makeText(this, "ENTER PROPER DETAILS",
                            Toast.LENGTH_SHORT).show();
                } else {
                    fall = true;


                    ContentValues values = new ContentValues();
                    values.put(FifaEntry.COLUMN_TEAM1_NAME, name1String);
                    values.put(FifaEntry.COLUMN_TEAM2_NAME, name2String);
                    values.put(FifaEntry.COLUMN_DATE, dateString);
                    values.put(FifaEntry.COLUMN_VENUE, venueString);
                    values.put(FifaEntry.COLUMN_TIME, timeString);
                    values.put(FifaEntry.COLUMN_TEAM1_ICON, muri1);
                    values.put(FifaEntry.COLUMN_TEAM2_ICON, muri2);


                    if (mCurrentFixtureUri == null) {
                        Uri newUri = getContentResolver().insert(FifaEntry.CONTENT_URI, values);

                        if (newUri == null) {
                            Toast.makeText(this, "INSERT FAILED",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "INSERT SUCCESSFUL",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        int rowsAffected = getContentResolver().update(mCurrentFixtureUri, values, null, null);

                        if (rowsAffected == 0) {
                            Toast.makeText(this, "UPDATE FAILED",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "UPDATE SUCCESSFUL",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveFixture();
                if (fall)
                    finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mFixtureHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (!mFixtureHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                FifaEntry._ID,
                FifaEntry.COLUMN_TEAM1_NAME,
                FifaEntry.COLUMN_TEAM2_NAME,
                FifaEntry.COLUMN_DATE,
                FifaEntry.COLUMN_VENUE,
                FifaEntry.COLUMN_TIME,
                FifaEntry.COLUMN_TEAM1_ICON,
                FifaEntry.COLUMN_TEAM2_ICON
        };

        return new CursorLoader(this,   // Parent activity context
                mCurrentFixtureUri,         // Query the content URI for the current fixture
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }


        if (cursor.moveToFirst()) {
            int name1ColumnIndex = cursor.getColumnIndex(FifaEntry.COLUMN_TEAM1_NAME);
            int name2ColumnIndex = cursor.getColumnIndex(FifaEntry.COLUMN_TEAM2_NAME);
            int dateColumnIndex = cursor.getColumnIndex(FifaEntry.COLUMN_DATE);
            int venueColumnIndex = cursor.getColumnIndex(FifaEntry.COLUMN_VENUE);
            int timeColumnIndex = cursor.getColumnIndex(FifaEntry.COLUMN_TIME);
            int icon1ColumnIndex = cursor.getColumnIndex(FifaEntry.COLUMN_TEAM1_ICON);
            int icon2ColumnIndex = cursor.getColumnIndex(FifaEntry.COLUMN_TEAM2_ICON);

            // Extract out the value from the Cursor for the given column index
            final String name1 = cursor.getString(name1ColumnIndex);
            final String name2 = cursor.getString(name2ColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String venue = cursor.getString(venueColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            muri1 = cursor.getString(icon1ColumnIndex);
            muri2 = cursor.getString(icon2ColumnIndex);

            // Update the views on the screen with the values from the database
            mname1EditText.setText(name1);
            mname2EditText.setText(name2);
            mdateEditText.setText(date);
            mvenueEditText.setText(venue);
            mtimeEditText.setText(time);

            try {
                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(Uri.parse(muri1)));
                micon1.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {

                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(Uri.parse(muri2)));
                micon2.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mname1EditText.setText("");
        mname2EditText.setText("");
        mdateEditText.setText("");
        mvenueEditText.setText("");
        mtimeEditText.setText("");
        micon1.setImageResource(R.drawable.fifabg);
        micon2.setImageResource(R.drawable.fifabg);

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("DISCARD", discardButtonClickListener);
        builder.setNegativeButton("KEEP EDITING", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentFixtureUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this fixture");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteFixture();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deleteFixture() {
        if (mCurrentFixtureUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentFixtureUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Error with deleting fixture",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Fixture deleted",
                        Toast.LENGTH_SHORT).show();
            }
            finish();

        }
    }
}