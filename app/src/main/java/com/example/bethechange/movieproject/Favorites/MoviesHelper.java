package com.example.bethechange.movieproject.Favorites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bethechange.movieproject.Model.MovieClass;

/**
 * Created by Be The Change on 1/26/2016.
 */
public class MoviesHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=2;
    final static String DATABASE_NAME="movies.db";

    public MoviesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE="CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ("+
        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MovieContract.MovieEntry.COLUMN_DATE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POSTER + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_RATE +" REAL " +");";
        final String SQL_CREATE_TRAILER_TABLE="CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                MovieContract.TrailerEntry._ID+" TEXT PRIMARY KEY, "+
                MovieContract.TrailerEntry.COLUMN_TRAILER_NAME+" TEXT, "+
                MovieContract.TrailerEntry.COLUMN_LINK+" TEXT, "+
                MovieContract.TrailerEntry.COLUMN_MOVIE_KEY+" INTEGER NOT NULL, "+
                "FOREIGN KEY("+MovieContract.TrailerEntry.COLUMN_MOVIE_KEY+") REFERENCES "+MovieContract.MovieEntry.TABLE_NAME+'('+
                MovieContract.MovieEntry._ID+"));";
        final String SQL_CREATE_REVIEW_TABLE="CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " ("+
                MovieContract.ReviewEntry._ID+" TEXT PRIMARY KEY, "+
                MovieContract.ReviewEntry.COLUMN_AUTHOR+" TEXT, "+
                MovieContract.ReviewEntry.COLUMN_CONTENT+" TEXT, "+
                MovieContract.ReviewEntry.COLUMN_MOVIE_KEY+" INTEGER NOT NULL, "+
                "FOREIGN KEY("+MovieContract.ReviewEntry.COLUMN_MOVIE_KEY+") REFERENCES "+MovieContract.MovieEntry.TABLE_NAME+'('+
                MovieContract.MovieEntry._ID+"));";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void InsertMovie(MovieClass movieClass){

    }
    public void InsertReviews(MovieClass.Review[]reviews){


    }
    public void InsertTrailers(MovieClass.VideoInfo[]trailers){



    }
}
