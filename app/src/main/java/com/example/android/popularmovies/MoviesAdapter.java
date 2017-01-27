package com.example.android.popularmovies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.domain.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> moviesData;

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    public MoviesAdapter(List<Movie> moviesData) {
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
        return moviesData.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView gridItemMovieView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            gridItemMovieView = (TextView)
                    itemView.findViewById(R.id.tv_item_movie);
        }
        void bind(int gridIndex) {
            gridItemMovieView.setText(moviesData.get(gridIndex).getName());
        }
    }

    public void setMoviesData(List<Movie> moviesData) {
        this.moviesData = moviesData;
        notifyDataSetChanged();
    }
}
