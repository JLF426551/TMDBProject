package com.jl426551.example.tmdbproject.DataBaseUtilities;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.R.attr.id;
import static com.jl426551.example.tmdbproject.DataBaseUtilities.MovieDBContract.MovieEntry.TABLE_NAME;

public class MovieDBProvider extends ContentProvider {

    public static final String LOG_TAG = "MDBProvider";

    private MovieDBHelper helper;

    private static final int LIST = 100;
    private static final int ITEM = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieDBContract.CONTENT_AUTHORITY, MovieDBContract.DB_PATH, LIST);
        sUriMatcher.addURI(MovieDBContract.CONTENT_AUTHORITY, MovieDBContract.DB_PATH + "/#", ITEM);
    }

    public MovieDBProvider()
    {
        //Empty constructor
    }

    @Override
    public boolean onCreate() {

        helper = new MovieDBHelper(getContext());
        return false;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch(match)
        {
            case LIST:
                cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEM:
                selection = MovieDBContract.MovieEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf( ContentUris.parseId(uri))};

                cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown Uri" + uri);
        }


        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LIST:
                return MovieDBContract.MovieEntry.CONTENT_TYPE;
            case ITEM:
                return MovieDBContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        if(values.size() == 0 )
            return 0;

        SQLiteDatabase db = helper.getWritableDatabase();
        int updatedRows = db.update(MovieDBContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);

        if(updatedRows != 0 )
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Log.v("DBProvider", "insert");
        Log.v("DBP", uri.toString());

        final int match = sUriMatcher.match(uri);

        Log.v("DBP", "uRI match: " + match);

        switch (match) {
            case LIST:
                SQLiteDatabase db = helper.getWritableDatabase();
                long id = db.insert(MovieDBContract.MovieEntry.TABLE_NAME, null, values);
                break;
            case ITEM:
            default:
                throw new IllegalStateException("Insert not supported for " + uri);
        }

        //Sends notification to listeners that data has been updated in URI.
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Get writeable database
        SQLiteDatabase database = helper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LIST:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(MovieDBContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM:
                // Delete a single row given by the ID in the URI
                selection = MovieDBContract.MovieEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(MovieDBContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }
}