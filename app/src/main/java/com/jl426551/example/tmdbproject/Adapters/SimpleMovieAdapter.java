package com.jl426551.example.tmdbproject.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jl426551.example.tmdbproject.DataStructures.SimpleMovie;
import com.jl426551.example.tmdbproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimpleMovieAdapter extends RecyclerView.Adapter<SimpleMovieAdapter.MovieViewHolder> {

    //Variable sets to 0 by default. Updated by setList method.
    private int totalMovies = 0;

    private final OnClickHandler mClickHandler;

    private ArrayList<SimpleMovie> movies;

    // The interface that receives onClick messages.
    public interface OnClickHandler {
        void onClick(SimpleMovie filmInformation);
    }

    public SimpleMovieAdapter(OnClickHandler handler, ArrayList<SimpleMovie> list) {

        mClickHandler = handler;
        movies = list;
        totalMovies = list.size();
    }

    public SimpleMovieAdapter(OnClickHandler handler) {
        mClickHandler = handler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean attachToParent = false;

        View viewToProcess = inflater.inflate(R.layout.single_poster_layout, parent, attachToParent);

        MovieViewHolder holder = new MovieViewHolder(viewToProcess);

        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return totalMovies;
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterView;
        Context context;

        public MovieViewHolder(View viewParam) {
            super(viewParam);
            context = viewParam.getContext();

            posterView = (ImageView) viewParam.findViewById(R.id.img_poster);
            viewParam.setOnClickListener(this);
        }

        void bind(int currentItemPosition) {
            SimpleMovie currentMovie = movies.get(currentItemPosition);

            //Sets the movie to the source
            Picasso.get().load(currentMovie.getPosterSource()).into(posterView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            SimpleMovie currentMovie = movies.get(position);
            mClickHandler.onClick(currentMovie);
        }
    }
}
