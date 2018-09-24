package com.jl426551.example.tmdbproject.TMDBSupportUtilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class TMDReviewLoader extends AsyncTaskLoader<ArrayList<String>> {

    private final String movieSelection = "https://api.themoviedb.org/3/movie/";
    private final String reviewSelection = "/reviews";
    private final String localeSelection = "&language=en-US&page=1";

    final String TAG = "TMDR Loader";
    private final String API_KEY = "?api_key=" + DataExtractionUtilities.TMDB_KEY;

    //Identifier for the particular movie. Passed through the constructor.
    private long movieID;

    String query;

    ArrayList<String> cachedList;

    public TMDReviewLoader(Context context, long identifier){
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
    public ArrayList<String> loadInBackground() {

        query = movieSelection + movieID + reviewSelection + API_KEY + localeSelection;

        Log.v(TAG, query);

        ArrayList<String> returnList = new ArrayList<String>();
        String networkResponse;

        try {
            networkResponse = NetworkUtilities.makeHttpRequest(query);
            returnList = DataExtractionUtilities.getReviewsList(networkResponse);
        } catch (IOException e) {

            Log.e(TAG, "network exception received");
        }

        return returnList;
    }

    /* Saves data once the Loader returns value, prevents sending a second, duplicate, request through
    the network. e.g. If the user changes activities within, application, returning to the activity
    which called the Loader will not cause the activity to restart the Loader.*/

    @Override
    public void deliverResult(ArrayList<String> data) {
        cachedList = data;
        super.deliverResult(data);
    }
}