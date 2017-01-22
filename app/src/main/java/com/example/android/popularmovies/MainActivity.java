package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.popularmovies.data.MoviesDataMock;

public class MainActivity extends AppCompatActivity {
    private static final int SPAN_COUNT = 3;
    private RecyclerView moviesGrid;
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesGrid = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, SPAN_COUNT);
        moviesGrid.setLayoutManager(gridLayoutManager);

        moviesGrid.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(MoviesDataMock.MOVIES);

        moviesGrid.setAdapter(moviesAdapter);
    }
}
