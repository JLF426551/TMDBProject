package com.jl426551.example.tmdbproject.DataStructures;

import java.util.ArrayList;

public class MovieDetails {

    private ArrayList<String> reviews;
    private ArrayList<Movie.Trailer> trailers;
    private Movie details;

    public MovieDetails(ArrayList<String> reviewData, ArrayList<Movie.Trailer> trailerData, Movie movie) {
        reviews = reviewData;
        trailers = trailerData;
        details = movie;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public ArrayList<Movie.Trailer> getTrailers() {
        return trailers;
    }

    //Returns the Movie object.
    public Movie getMovie() {
        return details;
    }
}
