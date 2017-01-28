package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.domain.Movie;
import java.util.List;

/**
 * Movies Adapter, custom adapter to bind movies data to the movies recycler view.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> moviesData;
    private MoviesAdapterOnClickHandler moviesAdapterOnClickHandler;

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    /**
     * Constructor.
     * @param moviesAdapterOnClickHandler OnClickHandler.
     */
    public MoviesAdapter(MoviesAdapterOnClickHandler moviesAdapterOnClickHandler ) {
        this.moviesAdapterOnClickHandler = moviesAdapterOnClickHandler;
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
        if (null == moviesData) return 0;
        return moviesData.size();
    }

    /**
     * Movie View holder, holds movies information for the movie recycler
     * view.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView gridItemMovieView;
        /**
         * Constructor
         *
         * @param itemView single movie item view
         */
        MovieViewHolder(View itemView) {
            super(itemView);
            gridItemMovieView = (TextView)
                    itemView.findViewById(R.id.tv_item_movie);
            gridItemMovieView.setOnClickListener(this);
        }

        private void bind(int gridIndex) {
            gridItemMovieView.setText(moviesData.get(gridIndex).getName());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String movieTitle = moviesData.get(adapterPosition).getName();
            moviesAdapterOnClickHandler.onClick(movieTitle);
        }
    }
    /**
     * Sets movies data.
     *
     * @param moviesData Movies data
     */
    public void setMoviesData(List<Movie> moviesData) {
        this.moviesData = moviesData;
        notifyDataSetChanged();
    }

    /**
     * Interface to handle the recycler view items clicks.
     */
    public interface MoviesAdapterOnClickHandler{
        /**
         * Triggers action on click event.
         * @param movieTitle Movie title
         */
        void onClick(String movieTitle);
    }
}
