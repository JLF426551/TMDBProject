package com.jl426551.example.tmdbproject.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jl426551.example.tmdbproject.R;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<String> {

    public ReviewAdapter(Activity context, ArrayList<String> list)
    {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_review_layout, parent, false);
        }

        String currentAttraction = getItem(position);

        TextView nameView = (TextView) listItemView.findViewById(R.id.tv_review);
        nameView.setText(currentAttraction);

        return listItemView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
