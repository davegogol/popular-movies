package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for movie data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "movie.db";

    /**
     * Constructor.
     * @param context
     */
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITES_MOVIES_TABLE =
                "CREATE TABLE " + MovieContract.FavouriteEntry.TABLE_NAME + " (" +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_ID   + " INTEGER PRIMARY KEY," +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_NAME + " TEXT ," +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER + " TEXT );";
        db.execSQL(SQL_CREATE_FAVOURITES_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavouriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
