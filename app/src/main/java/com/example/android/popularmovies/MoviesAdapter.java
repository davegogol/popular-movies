package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private String[] moviesData;

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    public MoviesAdapter(String[] moviesData) {
        this.moviesData = moviesData;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForGridItem, viewGroup,
                shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return moviesData.length;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView gridItemMovieView;
        public MovieViewHolder(View itemView) {
            super(itemView);
            gridItemMovieView = (ImageView)
                    itemView.findViewById(R.id.tv_item_movie);
        }
        void bind(int gridIndex) {
            Picasso.
                    with(gridItemMovieView.getContext()).
                    load("http://image.tmdb.org/t/p/w500/"+ moviesData[gridIndex] +".jpg").into(gridItemMovieView);
            gridItemMovieView.setAdjustViewBounds(true);
        }
    }
}
