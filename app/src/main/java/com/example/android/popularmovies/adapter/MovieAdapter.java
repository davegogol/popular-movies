package com.example.android.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.Collection;


public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Context activityContext;

    public MovieAdapter(Activity context) {
        super(context, 0);
        activityContext = context;
    }
    @Override
    public void addAll(Collection<? extends Movie> collection) {
        super.addAll(collection);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "position: #" + position);
        Movie movie = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_grid_item, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_img);
        Picasso.
                with(activityContext).
                load("http://image.tmdb.org/t/p/w150"+ movie.getPosterPath()).
                into(imageView);
        return convertView;
    }
}
