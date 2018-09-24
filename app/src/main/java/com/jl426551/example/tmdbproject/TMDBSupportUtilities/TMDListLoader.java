package com.jl426551.example.tmdbproject.TMDBSupportUtilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.jl426551.example.tmdbproject.DataStructures.SimpleMovie;

import java.io.IOException;
import java.util.ArrayList;

public class TMDListLoader extends AsyncTaskLoader<ArrayList<SimpleMovie>> {

    private final String TAG = "TMDListLoader";

    //Support values for The Movie Database.
    private final String preFix = "https://api.themoviedb.org/3";
    private final String API_KEY = "?api_key=" + DataExtractionUtilities.TMDB_KEY;
    private final String postFix = "&language=en-US&page=1";

    //Identifiers for queries, they correspond to the index values in variable
    //String[] queries.
    public final static int POPULARITY = 0;
    public final static int TOP_RATED = 1;
    public final static int NOW_PLAYING = 2;
    public final static int UPCOMING = 3;

    //Saved fixed strings for the four options of query to TheMovieDatabase
    private String[] queries = {"/movie/popular", "/movie/top_rated",
            "/movie/now_playing", "/movie/upcoming"};

    private String query;
    private int querySelection;

    ArrayList<SimpleMovie> cachedList;

    //Loader will take a second parameter @param call which will depend on which list is requested.
    public TMDListLoader(Context context, int querySelection) {
        super(context);
        this.querySelection = querySelection;
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

        //Check against invalid values. Defaults to zero.
        if (querySelection > 3 || querySelection < 0)
            querySelection = 0;

        //Formats URL to correctly match with The Movie Database.
        query = preFix + queries[querySelection] + API_KEY + postFix;

        String networkResponse;

        try {
            networkResponse = NetworkUtilities.makeHttpRequest(query);
            returnList = DataExtractionUtilities.createMovieList(networkResponse);
        } catch (IOException e) {
            Log.e(TAG, "network exception received");
        }

        return returnList;
    }

    /* Saves data once the Loader returns value, prevents sending a second, duplicate, request through
    the network. e.g. If the user changes activities within, application, returning to the activity
    which called the Loader will not cause the activity to restart the Loader.*/

    @Override
    public void deliverResult(ArrayList<SimpleMovie> data) {
        cachedList = data;
        super.deliverResult(data);
    }
}