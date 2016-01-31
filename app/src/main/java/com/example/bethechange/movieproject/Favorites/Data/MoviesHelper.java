package com.example.bethechange.movieproject.Favorites.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bethechange.movieproject.Model.MovieClass;

import java.util.ArrayList;

/**
 * Created by Be The Change on 1/26/2016.
 */
public class MoviesHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=2;
    final static String DATABASE_NAME="movies.db";

    public MoviesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE="CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ("+
        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MovieContract.MovieEntry.COLUMN_DATE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POSTER + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_RATE +" REAL " +");";
        final String SQL_CREATE_TRAILER_TABLE="CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                MovieContract.TrailerEntry._ID+" INTEGER PRIMARY KEY   AUTOINCREMENT, "+
                MovieContract.TrailerEntry.COLUMN_TRAILER_NAME+" TEXT, "+
                MovieContract.TrailerEntry.COLUMN_TRAILER_KEY+" TEXT, "+
                MovieContract.TrailerEntry.COLUMN_LINK+" TEXT, "+
                MovieContract.TrailerEntry.COLUMN_MOVIE_KEY+" INTEGER NOT NULL, "+
                "FOREIGN KEY("+MovieContract.TrailerEntry.COLUMN_MOVIE_KEY+") REFERENCES "+MovieContract.MovieEntry.TABLE_NAME+'('+
                MovieContract.MovieEntry._ID+"));";
        final String SQL_CREATE_REVIEW_TABLE="CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " ("+
                MovieContract.ReviewEntry._ID+" INTEGER PRIMARY KEY   AUTOINCREMENT, "+
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
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
    public boolean InsertMovie(MovieClass movieClass){
        boolean ret=false;
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID,movieClass.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,movieClass.getVote_average());
        contentValues.put(MovieContract.MovieEntry.COLUMN_DATE,movieClass.getRelease_date());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,movieClass.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movieClass.getPoster_path());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATE, movieClass.getPopularity());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieClass.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movieClass.getVote_count());
        ret = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,contentValues)== -1 ? false:true;
        ret&=InsertReviews(movieClass.getReviews(),movieClass.getId());
        ret&=InsertTrailers(movieClass.getVideosInfo(),movieClass.getId());

       return ret;
    }
    public Boolean InsertReviews(ArrayList<MovieClass.Review> reviews,int  movieID){
        SQLiteDatabase db=getWritableDatabase();
        boolean res=true;
        for(int i=0;i<reviews.size();i++){
            ContentValues contentValues=new ContentValues();

            contentValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_KEY,movieID);
            contentValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,reviews.get(i).getName());
            contentValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT,reviews.get(i).getContent());
            res=db.insert(MovieContract.ReviewEntry.TABLE_NAME,null,contentValues)==-1 ? false:true;;
        }
        return res;
    }
    public boolean InsertTrailers(ArrayList<MovieClass.VideoInfo>trailers,int  movieID){
        SQLiteDatabase db=getWritableDatabase();
        boolean res=true;
        for(int i=0;i<trailers.size();i++){
            ContentValues contentValues=new ContentValues();

            contentValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_KEY,movieID);
            contentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY,trailers.get(i).getkey());
            contentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME,trailers.get(i).getName());
            contentValues.put(MovieContract.TrailerEntry.COLUMN_LINK,trailers.get(i).getFullLink());
            res=db.insert(MovieContract.TrailerEntry.TABLE_NAME,null,contentValues)==-1 ? false:true;;

        }
        return res;

    }
    public boolean DeleteMovie(int movieID){
        SQLiteDatabase db=getWritableDatabase();
        boolean res=true;
        db.delete(MovieContract.ReviewEntry.TABLE_NAME, MovieContract.ReviewEntry._ID+" = ? ",new String[]{String.valueOf(movieID)});
        db.delete(MovieContract.TrailerEntry.TABLE_NAME, MovieContract.TrailerEntry._ID+" = ? ",new String[]{String.valueOf(movieID)});
        res=db.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry._ID+" = ? ",new String[]{String.valueOf(movieID)})!=-1?true:false;

        return res;
    }
    public ArrayList<MovieClass.VideoInfo> loadTrailers(int movieId){
        ArrayList<MovieClass.VideoInfo>trailers=new ArrayList<>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor trailerCurs=db.rawQuery("SELECT * FROM "+MovieContract.TrailerEntry.TABLE_NAME+" WHERE "+MovieContract.TrailerEntry.COLUMN_MOVIE_KEY
                + " = ? ;", new String[]{String.valueOf(movieId)});
        while (trailerCurs.moveToNext()){

            String key=trailerCurs.getString(trailerCurs.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY));
            String name=trailerCurs.getString(trailerCurs.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME));
            String Link=trailerCurs.getString(trailerCurs.getColumnIndex(MovieContract.TrailerEntry.COLUMN_LINK));
            trailers.add(new MovieClass.VideoInfo(name, key));

        }
        return trailers;
    }
    public ArrayList<MovieClass.Review> loadReviews(int movieId){
        ArrayList<MovieClass.Review>reviews=new ArrayList<>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor reviewsCurs=db.rawQuery("SELECT * FROM " + MovieContract.ReviewEntry.TABLE_NAME + " WHERE " + MovieContract.ReviewEntry.COLUMN_MOVIE_KEY
                + " = ? ;", new String[]{String.valueOf(movieId)});
        while (reviewsCurs.moveToNext()){

            String author=reviewsCurs.getString(reviewsCurs.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR));
            String content=reviewsCurs.getString(reviewsCurs.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT));
            reviews.add(new MovieClass.Review(author,content));

        }
       return reviews;
    }
    public ArrayList<MovieClass> loadFavoritesMovies(){
        SQLiteDatabase db=getWritableDatabase();
        ArrayList<MovieClass>favs=new ArrayList<>();


        Cursor moviesCurs=db.rawQuery("SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME+';', null);


        while(moviesCurs.moveToNext()){
            MovieClass movieClass=new MovieClass();
            movieClass.setId(moviesCurs.getInt(moviesCurs.getColumnIndex(MovieContract.MovieEntry._ID)));

            movieClass.setVote_average(moviesCurs.getFloat(moviesCurs.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
            movieClass.setOverView(moviesCurs.getString(moviesCurs.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
            movieClass.setTitle(moviesCurs.getString(moviesCurs.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
            movieClass.setPoster_path(moviesCurs.getString(moviesCurs.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
            movieClass.setRelease_date(moviesCurs.getString(moviesCurs.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE)));
            movieClass.setPopularity(moviesCurs.getFloat(moviesCurs.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATE)));
            movieClass.setVote_count(moviesCurs.getInt(moviesCurs.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT)));
            movieClass.setReviews(loadReviews(movieClass.getId()));
            movieClass.setVideosInfo(loadTrailers(movieClass.getId()));
            favs.add(movieClass);
        }

        return favs;
    }
}
