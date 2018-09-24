package com.jl426551.example.tmdbproject.DataStructures;

import android.os.Parcel;
import android.os.Parcelable;

/*
Object class which holds basic information of a Movie. Only includes
details of film.
 */
public class Movie implements Parcelable {

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

    public final static int FAVORITE_TRUE = 1;
    public final static int FAVORITE_UNKNOWN = 0;

    private String title;
    private String releaseDate;
    private String posterSource;
    private double voteAverage;
    private String synopsis;
    private long identifier;
    private int runTime;

    /*This will act as a holder for application database. Only saved as true by Movie object retrieved
    from directly from Database. Does not update when queued from TheMovieDatabase as part of query.
    */
    private int isFavorite;

    //Public constructor with no Favorite status.
    public Movie(String title, String date, String imageSource, double avg, int runtime, String summary, long identifier) {
        this.title = title;
        releaseDate = date;
        posterSource = imageSource;
        voteAverage = avg;
        runTime = runtime;
        synopsis = summary;
        this.identifier = identifier;
        isFavorite = Movie.FAVORITE_UNKNOWN;
    }

    //Public constructor which passes known Favorite status.
    public Movie(String title, String date, String imageSource, double avg, int runtime, String summary, long identifier, int favorite) {
        this.title = title;
        releaseDate = date;
        posterSource = imageSource;
        voteAverage = avg;
        runTime = runtime;
        synopsis = summary;
        this.identifier = identifier;

        /* This check prevents the isFavorite to be saved to invalid values.
        Defaults to Movie.FAVORITE_UNKNOWN. */
        if(favorite != Movie.FAVORITE_TRUE)
            isFavorite = Movie.FAVORITE_UNKNOWN;
        else isFavorite = Movie.FAVORITE_TRUE;
    }

    public Movie(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        posterSource = in.readString();
        voteAverage = in.readDouble();
        runTime = in.readInt();
        synopsis = in.readString();
        identifier = in.readLong();
        isFavorite = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterSource() {
        return posterSource;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public long getIdentifier(){
        return identifier;
    }

    public int getIsFavorite(){
        return isFavorite;
    }

    public int getRunTime(){
        return runTime;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(posterSource);
        parcel.writeDouble(voteAverage);
        parcel.writeInt(runTime);
        parcel.writeString(synopsis);
        parcel.writeLong(identifier);
        parcel.writeInt(isFavorite);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /*
    Class holds information on a movie trailer.
    */
    public static class Trailer {

        String trailerkey;
        String title;

        public Trailer(String key, String title)
        {
            trailerkey = key;
            this.title = title;
        }

        public String getTitle()
        {
            return title;
        }

        public String getKey(){
            return trailerkey;
        }


    }
}