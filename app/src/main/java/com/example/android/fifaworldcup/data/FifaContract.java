
package com.example.android.fifaworldcup.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class FifaContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.fifaworldcup";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FIXTURE = "fixtures";



    private FifaContract() {}

    public static final class FifaEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FIXTURE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FIXTURE;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FIXTURE;


        public final static String TABLE_NAME = "fixtures";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_TEAM1_NAME ="team1name";

        public final static String COLUMN_TEAM2_NAME ="team2name";

        public final static String COLUMN_DATE = "date";

        public final static String COLUMN_VENUE = "venue";

        public final static String COLUMN_TIME = "time";

        public final static String COLUMN_TEAM1_ICON = "team1icon";

        public final static String COLUMN_TEAM2_ICON = "team2icon";

       /* public static boolean isValidGender(int gender) {
                     if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                                return true;
                            }
                     return false;
                    }*/

    }

}
