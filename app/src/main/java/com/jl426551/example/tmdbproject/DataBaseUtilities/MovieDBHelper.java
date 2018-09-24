package com.jl426551.example.tmdbproject.DataBaseUtilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviecollection.db";
    private static final int DB_VERSION = 3;

    final String CLASS_TAG = "DB Helper";

    public MovieDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.v(CLASS_TAG,"onCreate");
        String SQL_CREATE_FIXED = "CREATE TABLE collection ( IDENTIFIER REAL );";

        /*
        The database will be created with the following parameters:

        Title, Synopsis, Release Date, Image Source: Saved as text
        Average saved as Real
        Favorite saved as integer: 0 default, 1 indicates movie has been marked as favorite.
         */
        String SQL_DB_CREATOR = "CREATE TABLE " + MovieDBContract.MovieEntry.TABLE_NAME + "(" +
                MovieDBContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieDBContract.MovieEntry.COLUMN_TITLE + " REAL " +
                ");";

        //Creates column
        sqLiteDatabase.execSQL(SQL_DB_CREATOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

}