package com.jl426551.example.tmdbproject.TMDBSupportUtilities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.jl426551.example.tmdbproject.DataStructures.Movie;

import java.io.IOException;

public class TMDBMovieLoader extends AsyncTaskLoader<Movie> {

    private final String movieSelection = "https://api.themoviedb.org/3/movie/";
    private final String localeSelection = "&language=en-US&page=1";
    private final String API_KEY = "?api_key=" + DataExtractionUtilities.TMDB_KEY;

    final String TAG = "Details Loader";
    String query;
    long movieID;

    Movie cachedResult;

    //Loader will take a second parameter @param call which will depend on which list is requested.
    public TMDBMovieLoader(Context context, long identifier){
        super(context);
        movieID = identifier;

    }

    @Override
    protected void onStartLoading() {

        if (cachedResult != null)
            deliverResult(cachedResult);
        else
            forceLoad();
    }

    @Override
    public Movie loadInBackground() {

        Log.v(TAG, "load in background called");
        query = movieSelection + movieID + API_KEY + localeSelection;

        Log.v(TAG, query);

        Movie tempMovie = null;
        String networkResponse;

        try {
            networkResponse = NetworkUtilities.makeHttpRequest(query);
            tempMovie = DataExtractionUtilities.getMovie(networkResponse, movieID);
        } catch (IOException e) {

            Log.e(TAG, "network exception received");
        }

        Log.v(TAG, "liB, returning list");

        return tempMovie;
    }

    /* Saves data once the Loader returns value, prevents sending a second, duplicate, request through
    the network. e.g. If the user changes activities within, application, returning to the activity
    which called the Loader will not cause the activity to restart the Loader.*/
    @Override
    public void deliverResult(Movie data) {
        cachedResult = data;
        super.deliverResult(data);
    }
}
