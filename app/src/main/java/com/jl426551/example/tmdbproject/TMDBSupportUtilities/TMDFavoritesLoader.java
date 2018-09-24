package com.jl426551.example.tmdbproject.TMDBSupportUtilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

import com.jl426551.example.tmdbproject.DataBaseUtilities.MovieDBContract;
import com.jl426551.example.tmdbproject.DataStructures.Movie;
import com.jl426551.example.tmdbproject.DataStructures.SimpleMovie;

import java.io.IOException;
import java.util.ArrayList;

public class TMDFavoritesLoader extends AsyncTaskLoader<ArrayList<SimpleMovie>> {

    //Support values for The Movie Database.
    final String preFix = "https://api.themoviedb.org/3/movie/";
    final String key = "?api_key=" + DataExtractionUtilities.TMDB_KEY;
    final String locale = "&language=en-US";

    private String urlQuery;
    private ArrayList<SimpleMovie> cachedList;

    private String[] projection = {MovieDBContract.MovieEntry._ID, MovieDBContract.MovieEntry.COLUMN_TITLE};
    private CursorLoader myFavoritesLoader = new CursorLoader(getContext(), MovieDBContract.MovieEntry.CONTENT_URI, projection, null, null, null);
    private Cursor myFavoritesData;

    public TMDFavoritesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {

        if (cachedList != null)
            deliverResult(cachedList);
        else
            forceLoad();
    }

    @Override
    public ArrayList<SimpleMovie> loadInBackground() {

        ArrayList<SimpleMovie> returnList = new ArrayList<SimpleMovie>();
        myFavoritesData = myFavoritesLoader.loadInBackground();

        if (myFavoritesData != null) {

            int colIndex = myFavoritesData.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_TITLE);
            Cursor cIterator = myFavoritesData;

            for (int i = 0; i < myFavoritesData.getCount(); i++) {
                cIterator.moveToPosition(i);

                urlQuery = preFix + cIterator.getInt(colIndex) + key + locale;
                String networkResponse;

                try {
                    networkResponse = NetworkUtilities.makeHttpRequest(urlQuery);

                    /* Extracts details for a single movie and adds it to the list.*/
                    returnList.add(DataExtractionUtilities.extractSingleMovieDetails(networkResponse, cIterator.getInt(colIndex), Movie.FAVORITE_TRUE));
                } catch (IOException e) {

                    Log.e("Favorites Loader", e.toString());
                }

            }
        }

        return returnList;
    }

    @Override
    public void deliverResult(ArrayList<SimpleMovie> data) {
        cachedList = data;
        super.deliverResult(data);
    }
}
