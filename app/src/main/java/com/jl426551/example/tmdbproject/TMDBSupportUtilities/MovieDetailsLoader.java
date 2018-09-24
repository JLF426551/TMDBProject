package com.jl426551.example.tmdbproject.TMDBSupportUtilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.jl426551.example.tmdbproject.DataStructures.Movie;
import com.jl426551.example.tmdbproject.DataStructures.MovieDetails;

import java.util.ArrayList;

public class MovieDetailsLoader extends AsyncTaskLoader<MovieDetails> {

    private long movieID;
    private ArrayList<String> reviewReturnList;
    private ArrayList<Movie.Trailer> trailerReturnList;
    private Movie details;

    //Constructor. Identifier is used in network queue.
    public MovieDetailsLoader(Context context, long movieIdentifier) {
        super(context);
        movieID = movieIdentifier;
    }

    @Override
    public MovieDetails loadInBackground() {

        reviewReturnList = new TMDReviewLoader(getContext(), movieID).loadInBackground();
        trailerReturnList = new TMDVideoLoader(getContext(), movieID).loadInBackground();
        details = new TMDBMovieLoader(getContext(), movieID).loadInBackground();

        return new MovieDetails(reviewReturnList, trailerReturnList, details);
    }
}
