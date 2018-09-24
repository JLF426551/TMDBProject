package com.jl426551.example.tmdbproject.TMDBSupportUtilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.jl426551.example.tmdbproject.DataStructures.Movie;

import java.io.IOException;
import java.util.ArrayList;

public class TMDVideoLoader extends AsyncTaskLoader<ArrayList<Movie.Trailer>> {

    private final String movieSelection = "https://api.themoviedb.org/3/movie/";
    private final String videoSelection = "/videos";
    private final String localeSelection = "&language=en-US&page=1";

    private final String TAG = "TMDV Loader";
    private final String API_KEY = "?api_key=" + DataExtractionUtilities.TMDB_KEY;

    private long movieID;

    String query;
    ArrayList<Movie.Trailer> cachedList;

    public TMDVideoLoader(Context context, long identifier){
        super(context);
        movieID = identifier;
    }

    @Override
    protected void onStartLoading() {

        if (cachedList != null)
            deliverResult(cachedList);
        else
            forceLoad();
    }

    @Override
    public ArrayList<Movie.Trailer> loadInBackground() {

        //Formats query for The Movie Database
        query = movieSelection + movieID + videoSelection + API_KEY + localeSelection;

        ArrayList<Movie.Trailer> returnList = new ArrayList<Movie.Trailer>();
        String networkResponse;

        try {
            networkResponse = NetworkUtilities.makeHttpRequest(query);
            returnList = DataExtractionUtilities.createYouTubeTrailerList(networkResponse);
        } catch (IOException e) {

            Log.e(TAG, "network exception received");
        }

        return returnList;
    }

    /* Saves data once the Loader returns value, prevents sending a second, duplicate, request through
    the network. e.g. If the user changes activities within, application, returning to the activity
    which called the Loader will not cause the activity to restart the Loader.*/

    @Override
    public void deliverResult(ArrayList<Movie.Trailer> data) {
        cachedList = data;
        super.deliverResult(data);
    }
}