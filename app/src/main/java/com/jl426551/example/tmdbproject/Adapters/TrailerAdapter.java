package com.jl426551.example.tmdbproject.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jl426551.example.tmdbproject.DataStructures.Movie;
import com.jl426551.example.tmdbproject.R;

import java.util.ArrayList;

public class TrailerAdapter extends ArrayAdapter<Movie.Trailer> {

    private int totalTrailers;

    @Override
    public int getCount() {
        return totalTrailers;
    }

    public TrailerAdapter(Activity context, ArrayList<Movie.Trailer> movies) {
        super(context, 0, movies);

        totalTrailers = movies.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_trailer_view, parent, false);
        }

        Movie.Trailer currentAttraction = getItem(position);

        TextView nameView = (TextView) listItemView.findViewById(R.id.trailer_title_view);

        nameView.setText(currentAttraction.getTitle());

        return listItemView;
    }

}