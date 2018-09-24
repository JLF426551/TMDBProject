package com.jl426551.example.tmdbproject.TMDBSupportUtilities;

import android.util.Log;

import com.jl426551.example.tmdbproject.DataStructures.Movie;
import com.jl426551.example.tmdbproject.DataStructures.SimpleMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataExtractionUtilities {

    //TODO: Update TMDB_KEY with your own TheMovieDatabase Key.
    //The public The Movie Database Key. Obtain yours here: https://www.themoviedb.org/
    // The Movie Database Developer's page: https://developers.themoviedb.org/3/getting-started/introduction
    public final static String TMDB_KEY = "";

    //Fixed prefix to read images from The Movie Database
    private final static String imageSourcePrefix = "http://image.tmdb.org/t/p/w185";

    //Creates an ArrayList of SimpleMovie objects from JSON response String passed in.
    public static ArrayList<SimpleMovie> createMovieList(String jsonresponse) {

        //Used to read response and add values to Movie objects being added to the list.
        String tempPosterSource;
        long tempIdentifier;

        //The list which will be populated and returned.
        ArrayList<SimpleMovie> list = new ArrayList<SimpleMovie>();

        try {

            JSONObject firstLevelObject = new JSONObject(jsonresponse);
            JSONArray results = firstLevelObject.getJSONArray("results");

            int numberOfResults = results.length();

            JSONObject tempObject;


            //Iterates the results JSONArray and creates the Story objects added to 'list.'
            for (int i = 0; i < numberOfResults; i++) {
                tempObject = results.getJSONObject(i);

                tempPosterSource = tempObject.getString("poster_path");
                tempIdentifier = tempObject.getLong("id");

                //Modifies the String to obtain an absolute URL.
                tempPosterSource = tempPosterSource.trim();
                tempPosterSource = imageSourcePrefix + tempPosterSource;

                //passes 0 for movie runtime as the list query does not return this value.
                list.add(new SimpleMovie(tempIdentifier, tempPosterSource));

            }

        } catch (JSONException e) {
            Log.v("Unable to parse JSON", "");
        }

        return list;
    }

    //Returns an ArrayList<String> of the keys for the trailers in YouTube for a single movie.
    public static ArrayList<Movie.Trailer> createYouTubeTrailerList(String jsonresponse) {

        String tempKey;
        String tempTitle;
        ArrayList<Movie.Trailer> trailers = new ArrayList<Movie.Trailer>();

        try {

            JSONObject firstLevelObject = new JSONObject(jsonresponse);
            JSONArray results = firstLevelObject.getJSONArray("results");

            int numberOfResults = results.length();

            JSONObject tempObject;

            //Iterates the results JSONArray and creates the Story objects added to 'list.'
            for (int i = 0; i < numberOfResults; i++) {
                tempObject = results.getJSONObject(i);

                tempKey = tempObject.getString("key");
                tempTitle = tempObject.getString("name");

                trailers.add(new Movie.Trailer(tempKey, tempTitle));
            }

        } catch (JSONException e) {
            Log.v("Unable to parse JSON", "");
        }

        return trailers;
    }

    //Returns an ArrayList<String> of the values for the reviews for a given movie.
    public static ArrayList<String> getReviewsList(String jsonresponse) {

        String tempReview;

        ArrayList<String> reviews = new ArrayList<String>();

        try {

            JSONObject firstLevelObject = new JSONObject(jsonresponse);
            JSONArray results = firstLevelObject.getJSONArray("results");

            int numberOfResults = results.length();

            JSONObject tempObject;

            //Iterates the results JSONArray and creates the Story objects added to 'list.'
            for (int i = 0; i < numberOfResults; i++) {
                tempObject = results.getJSONObject(i);
                tempReview = tempObject.getString("content");
                reviews.add(tempReview);
            }

        } catch (JSONException e) {
            Log.v("Unable to parse JSON", "");
        }

        return reviews;
    }

    /* This will extract details for a single movie. */
    public static Movie getMovie(String jsonresponse, long id) {
        //Used to read response and add values to Movie objects being added to the list.
        String tempTitle;
        String tempPosterSource;
        double tempVoteAverage;
        String tempSynopsis;
        String tempReleaseDate;
        int tempRunTime;

        Movie result = null;
        try {

            JSONObject tempObject = new JSONObject(jsonresponse);

            tempSynopsis = tempObject.getString("overview");
            tempPosterSource = tempObject.getString("poster_path");
            tempReleaseDate = tempObject.getString("release_date");
            tempRunTime = tempObject.getInt("runtime");
            tempTitle = tempObject.getString("title");
            tempVoteAverage = tempObject.getDouble("vote_average");

            //Modifies the String to obtain an absolute URL.
            tempPosterSource = tempPosterSource.trim();
            tempPosterSource = imageSourcePrefix + tempPosterSource;

            result = new Movie(tempTitle, tempReleaseDate, tempPosterSource, tempVoteAverage, tempRunTime, tempSynopsis, id);

        } catch (JSONException e) {
            Log.v("Unable to parse JSON", "");
        }

        return result;
    }

    /* Extracts SimpleMovie information for a single movie json result.
    Adds Favorite status as passed through method.
    */
    public static SimpleMovie extractSingleMovieDetails(String jsonresponse, long identifier, int favoriteStatus) {

        String tempPosterSource = "";

        try {

            JSONObject firstLevelObject = new JSONObject(jsonresponse);

            tempPosterSource = firstLevelObject.getString("poster_path");

            //Modifies the String to obtain an absolute URL.
            tempPosterSource = tempPosterSource.trim();
            tempPosterSource = imageSourcePrefix + tempPosterSource;

        } catch (JSONException e) {
            Log.v("Unable to parse JSON", "");
        }

        //Movie(String title, String date, String imageSource, double avg, int runTime, String summary, long identifier, int favoriteStatus)
        return new SimpleMovie(identifier, tempPosterSource, favoriteStatus);
    }

}
