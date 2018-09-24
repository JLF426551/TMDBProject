package com.jl426551.example.tmdbproject.DataBaseUtilities;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDBContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.leonmovieproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String DB_PATH = "collection";

    public static final class MovieEntry implements BaseColumns {

        //Database columns assigned to table.
        public final static String TABLE_NAME = "collection";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TITLE = "IDENTIFIER";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, DB_PATH);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + DB_PATH;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + DB_PATH;

    }
}
