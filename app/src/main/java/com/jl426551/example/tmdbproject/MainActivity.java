package com.jl426551.example.tmdbproject;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jl426551.example.tmdbproject.Adapters.SimpleMovieAdapter;
import com.jl426551.example.tmdbproject.DataStructures.Movie;
import com.jl426551.example.tmdbproject.DataStructures.SimpleMovie;
import com.jl426551.example.tmdbproject.TMDBSupportUtilities.TMDFavoritesLoader;
import com.jl426551.example.tmdbproject.TMDBSupportUtilities.TMDListLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SimpleMovieAdapter.OnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<SimpleMovie>> {

    final String TAG = "MainActivity";
    final static int TMDB_LOADER = 100;
    final static int FAVORITES_LOADER = 200;

    //This is the number of columns on the display.
    final int SPAN_COUNT = 4;

    SimpleMovieAdapter adapter;
    RecyclerView recyclerView;
    GridLayoutManager manager;

    private int currentQuerySelection;
    private ArrayList<SimpleMovie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        manager = new GridLayoutManager(this, SPAN_COUNT);
        adapter = new SimpleMovieAdapter(this);


        //Checks if there is a saved bundle from which to restore data.
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.movies))) {

            movieList = savedInstanceState.getParcelableArrayList(getString(R.string.movies));

            String tempT = savedInstanceState.getString(getString(R.string.title));
            setTitle(tempT);

            currentQuerySelection = savedInstanceState.getInt(getString(R.string.extra_query));

            adapter = new SimpleMovieAdapter(this, movieList);
        }

        //No bundle found, starts activity to default query and list.
        else {
            setPageTitle();
            sendQueryRequest();
        }

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    //onClick attached to each item in list. Opens activity which shows movie details.
    @Override
    public void onClick(SimpleMovie currentFilm) {
        Context context = this;

        Class destinationClass = DetailsListActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        //Adds Movie data to Intent.
        intentToStartDetailActivity.putExtra(getString(R.string.extra_movie), currentFilm);

        //Adds favorite status only if the current view is 'Show Favorites.'
        if(currentQuerySelection == FAVORITES_LOADER)
        {
            intentToStartDetailActivity.putExtra(getString(R.string.extra_favorite), Movie.FAVORITE_TRUE);
        }

        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the xml file.
        getMenuInflater().inflate(R.menu.set_order_menu, menu);
        return true;
    }

    //Sends request to Loader with particular The Movie Database queries.
    //Calls on updateQueryInformation to make sure a duplicate request is not sent.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popularity_sort_option:
                if (updateQueryInformation(TMDListLoader.POPULARITY))
                    sendQueryRequest();
                return true;
            case R.id.rated_sort_option:
                if (updateQueryInformation(TMDListLoader.TOP_RATED))
                    sendQueryRequest();
                return true;
            case R.id.upcoming_sort_option:
                if (updateQueryInformation(TMDListLoader.UPCOMING))
                    sendQueryRequest();
                return true;
            case R.id.now_sort_option:
                if (updateQueryInformation(TMDListLoader.NOW_PLAYING))
                    sendQueryRequest();
                return true;
            case R.id.favorites_sort_option:
                if (updateQueryInformation(FAVORITES_LOADER))
                    sendFavoritesRequest();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<SimpleMovie>> onCreateLoader(int currentQuery, Bundle bundle) {

        if (currentQuery != FAVORITES_LOADER)
            return new TMDListLoader(this, currentQuerySelection);

        else return new TMDFavoritesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<SimpleMovie>> loader, ArrayList<SimpleMovie> list) {

        movieList = list;
        adapter = new SimpleMovieAdapter(this, list);

        /* Checks on results returned by Loader.
        If there are results, the data is displayed to the user.
         */
        if (list != null && !list.isEmpty()) {
            //Adds the SimpleMovie ArrayList returned from Loader to adapter.
            adapter = new SimpleMovieAdapter(this, list);
        }

        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onLoaderReset(Loader<ArrayList<SimpleMovie>> loader) {

    }

    //Sends all requests to background loader which retrieve data from TheMovieDatabase directly.
    private void sendQueryRequest() {
        /*The following section checks for internet connection.*/
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            //Active connection found. Search request executes.
            getLoaderManager().restartLoader(TMDB_LOADER, null, this).forceLoad();
        } else Log.v(TAG, "Network connection not found");
    }

    //A request sent to the saved database to obtain the list of movies.
    private void sendFavoritesRequest() {

        /*The following section checks for internet connection.*/
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //Active connection found. Search request executes.
        if (networkInfo != null && networkInfo.isConnected()){

            getLoaderManager().restartLoader(FAVORITES_LOADER, null, this);
        } else Log.v(TAG, "Network connection not found");
    }

    //Updates query-associated variables. Updates the query for Loader and page title.
    //Returns 'false' if the requested query is the query already being displayed.
    private boolean updateQueryInformation(int q) {

        if (currentQuerySelection == q) {
            return false;
        } else {
            currentQuerySelection = q;
            setPageTitle();
            return true;
        }
    }

    //Updates the title to match the query being displayed.
    private void setPageTitle() {

        switch (currentQuerySelection) {
            case TMDListLoader.NOW_PLAYING:
                setTitle(getString(R.string.title_now));
                break;
            case TMDListLoader.TOP_RATED:
                setTitle(getString(R.string.title_highest));
                break;
            case TMDListLoader.UPCOMING:
                setTitle(getString(R.string.title_upcoming));
                break;
            case TMDListLoader.POPULARITY:
                setTitle(getString(R.string.title_popular));
                break;
            case FAVORITES_LOADER:
                setTitle(getString(R.string.title_favorites));
                break;
            default:
                setTitle(getString(R.string.app_name));
        }
    }

    //Saves the current SimpleMovie list to allow data persistance.
    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (movieList != null) {
            outState.putParcelableArrayList(getString(R.string.movies), movieList);
            outState.putString(getString(R.string.title), getTitle().toString());
            outState.putInt(getString(R.string.extra_query), currentQuerySelection);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}