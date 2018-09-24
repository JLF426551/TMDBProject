package com.jl426551.example.tmdbproject;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jl426551.example.tmdbproject.Adapters.ReviewAdapter;
import com.jl426551.example.tmdbproject.Adapters.TrailerAdapter;
import com.jl426551.example.tmdbproject.DataBaseUtilities.MovieDBContract;
import com.jl426551.example.tmdbproject.DataStructures.Movie;
import com.jl426551.example.tmdbproject.DataStructures.MovieDetails;
import com.jl426551.example.tmdbproject.DataStructures.SimpleMovie;
import com.jl426551.example.tmdbproject.TMDBSupportUtilities.MovieDetailsLoader;
import com.squareup.picasso.Picasso;

public class DetailsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetails> {

    final private int REVIEW_SELECTION = 71;
    final private int TRAILER_SELECTION = 73;

    long movieID;
    MovieDetails information;

    ListView informationView;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;

    //Instance variables for values displayed to user.
    String movieTitle;
    String movieSynopsis;
    String movieReleaseDate;
    String moviePosterSource;
    double movieVoteAverage;
    int movieRuntime;

    //Instance variables of views
    TextView titleView;
    TextView synopsisView;
    TextView avgView;
    TextView yearView;
    TextView lengthView;
    ImageView posterView;
    Button favoriteButton;

    //Holds the current 'favorite' status of the movie.
    int temporaryFavoriteStatus;

    final static int MOVIE_DATA_LOADER = 560;

    View.OnClickListener favoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveFavorite();
        }
    };

    AdapterView.OnItemClickListener trailerListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Movie.Trailer currentTrailer = information.getTrailers().get(position);
            String currentTrailerKey = currentTrailer.getKey();

            String YouTubeURL = "http://www.youtube.com/watch?v=" + currentTrailerKey;
            Uri website = Uri.parse(YouTubeURL);

            Intent intent = new Intent(Intent.ACTION_VIEW, website);

            Log.v("TA", "intent created");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        setTitle(getString(R.string.title_movie_details));

        //Initiates views.
        titleView = (TextView) findViewById(R.id.tv_title);
        synopsisView = (TextView) findViewById(R.id.tv_synopsis);
        posterView = (ImageView) findViewById(R.id.iv_poster);
        avgView = (TextView) findViewById(R.id.tv_average);
        yearView = (TextView) findViewById(R.id.tv_year);
        lengthView = (TextView) findViewById(R.id.tv_length);

        favoriteButton = (Button) findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(favoriteListener);

        movieTitle = "";
        movieSynopsis = "";
        movieReleaseDate = "";
        movieVoteAverage = 0.0;
        movieRuntime = 0;

        //Obtain data passed through intent.
        Intent passedIntent = getIntent();

        //Reads the SimpleMovie details
        if (passedIntent != null) {

            SimpleMovie passedMovie = passedIntent.getParcelableExtra(getString(R.string.extra_movie));
            moviePosterSource = passedMovie.getPosterSource();
            movieID = passedMovie.getIdentifier();

            //If there is a Favorite Status extra, reads it. Defaults to 'FAVORITE_UNKNOWN'
            if(passedIntent.hasExtra(getString(R.string.extra_favorite)))
                temporaryFavoriteStatus = passedIntent.getIntExtra(getString(R.string.extra_favorite), Movie.FAVORITE_UNKNOWN);

        }

        //Sends request for Loader.
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().restartLoader(MOVIE_DATA_LOADER, null, this).forceLoad();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the xml file.
        getMenuInflater().inflate(R.menu.list_selection_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.trailer_option: setAdapter(TRAILER_SELECTION);
                break;
            case R.id.review_option: setAdapter(REVIEW_SELECTION);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public Loader<MovieDetails> onCreateLoader(int id, Bundle args) {
        return new MovieDetailsLoader(this, movieID);
    }

    /*Saves the information returned from MovieDetailsLoader to information.
    Then calls the retrieval of particular values and updates views.
    */
    @Override
    public void onLoadFinished(Loader<MovieDetails> loader, MovieDetails data) {

        information = data;
        readMovieValues();
        updateNewViews();
    }

    @Override
    public void onLoaderReset(Loader<MovieDetails> loader) {

    }

    //Reads the values off MovieDetails object.
    public void readMovieValues()
    {
        movieTitle = information.getMovie().getTitle();
        movieSynopsis = information.getMovie().getSynopsis();
        movieReleaseDate = information.getMovie().getReleaseDate();
        movieVoteAverage = information.getMovie().getVoteAverage();
        movieRuntime = information.getMovie().getRunTime();
    }

    //Updates the views to the user. Formats those necessary.
    public void updateNewViews() {

        //Removes month and day from release date, to only display year.
        movieReleaseDate = movieReleaseDate.substring(0, 4);
        //Formats average to display as value out of a scale of 10.
        String movieVoteAverageString = movieVoteAverage + "/10";

        String yearViewText = movieReleaseDate;
        String runTimeText = movieRuntime + " " + getString(R.string.minutes);
        //Set data Views and display.
        titleView.setText(movieTitle);
        synopsisView.setText(movieSynopsis);
        avgView.setText(movieVoteAverageString);
        yearView.setText(yearViewText);

        lengthView.setText(runTimeText);

        Picasso.get().load(moviePosterSource)
                .resize(259, 388)
                .into(posterView);

        informationView = (ListView) findViewById(R.id.information_list);

        trailerAdapter = new TrailerAdapter(this, information.getTrailers());
        reviewAdapter = new ReviewAdapter(this, information.getReviews());

        //Defaults to show the trailers on activity launch.
        setAdapter(TRAILER_SELECTION);
    }

    //Overrides the ListView with either the trailers or the reviews depending on selection by user.
    private void setAdapter(int selection)
    {
        switch(selection)
        {
            //Review list. Because there is no action, onItemClickListener is null.
            case REVIEW_SELECTION:
                informationView.setAdapter(reviewAdapter);
                informationView.setOnItemClickListener(null);
                break;
            case TRAILER_SELECTION:
                informationView.setAdapter(trailerAdapter);
                informationView.setOnItemClickListener(trailerListener);
        }
    }
    //Saves movie as a favorite to the database. Called when the user clicks 'Save As Favorite'
    private void saveFavorite() {

        //Favorite status of the movie is unknown
        if (temporaryFavoriteStatus == Movie.FAVORITE_UNKNOWN) {

            //If movie is not saved, adds movie to list. Updates status as Favorite.
            if (!isFavoriteInDatabase()) {
                ContentValues values = new ContentValues();
                values.put(MovieDBContract.MovieEntry.COLUMN_TITLE, movieID);

                Uri resultUri = getContentResolver().insert(MovieDBContract.MovieEntry.CONTENT_URI, values);

                //Verifies insertion result. -1 indicates value insertion failed.
                if (resultUri == null) {
                    Toast.makeText(this, getBaseContext().getString(R.string.create_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getBaseContext().getString(R.string.create_created), Toast.LENGTH_SHORT).show();

                }
            }
            //Search determined there was a match in database. Displays message to user.
            else Toast.makeText(this, getBaseContext().getString(R.string.favorite_exists), Toast.LENGTH_SHORT).show();
        }
        //Movie is either Favorite as passed through intent, or, this activity has updates its
        //value. Displays message to user that movie is already a favorite.
        else {
            Toast.makeText(this, getBaseContext().getString(R.string.favorite_exists), Toast.LENGTH_SHORT).show();
        }
        //Else do nothing, movie is already a favorite.
    }

    //Checks to see if the movie is already saved to favorites.
    private boolean isFavoriteInDatabase() {
        //Gathers favorite value for movie index.
        String[] projection = {MovieDBContract.MovieEntry._ID, MovieDBContract.MovieEntry.COLUMN_TITLE};
        Cursor cursorData = new CursorLoader(this, MovieDBContract.MovieEntry.CONTENT_URI, projection, null, null, null).loadInBackground();

        int colIndex = cursorData.getColumnIndex(MovieDBContract.MovieEntry.COLUMN_TITLE);

        for (int i = 0; i < cursorData.getCount(); i++) {
            cursorData.moveToPosition(i);

            //If found, returns true. Also sets temporary favorite status to 1 to prevent further searches
            //inside the same activity.
            if (cursorData.getInt(colIndex) == movieID) {

                temporaryFavoriteStatus = Movie.FAVORITE_TRUE;
                return true;
            }
        }

        //if reached, no match is found.
        return false;
    }

}