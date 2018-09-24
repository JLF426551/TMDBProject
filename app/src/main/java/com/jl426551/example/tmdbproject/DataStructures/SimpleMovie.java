package com.jl426551.example.tmdbproject.DataStructures;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleMovie implements Parcelable {

    public final static Parcelable.Creator<SimpleMovie> CREATOR = new Parcelable.Creator<SimpleMovie>() {
        @Override
        public SimpleMovie createFromParcel(Parcel parcel) {
            return new SimpleMovie(parcel);
        }

        @Override
        public SimpleMovie[] newArray(int i) {
            return new SimpleMovie[i];
        }
    };

    private long identifier;
    private String posterSource;
    int isFavorite;

    public SimpleMovie(long id, String source)
    {
        identifier = id;
        posterSource = source;
        isFavorite = Movie.FAVORITE_UNKNOWN;
    }

    public SimpleMovie(Parcel in)
    {
        posterSource = in.readString();
        identifier = in.readLong();
        isFavorite = in.readInt();
    }

    public SimpleMovie(long id, String source, int favorite)
    {
        identifier = id;
        posterSource = source;
        isFavorite = favorite;
    }

    public String getPosterSource()
    {
        return posterSource;
    }

    public long getIdentifier()
    {
        return identifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(posterSource);
        parcel.writeLong(identifier);
        parcel.writeInt(isFavorite);
    }
}
