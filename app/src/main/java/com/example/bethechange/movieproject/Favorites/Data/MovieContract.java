package com.example.bethechange.movieproject.Favorites.Data;

import android.provider.BaseColumns;

/**
 * Created by Be The Change on 1/26/2016.
 */
public class MovieContract {
    public static final class MovieEntry implements BaseColumns{

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_VOTE_AVERAGE="vote_average";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_RATE = "popularity";




    }

    public static final class ReviewEntry implements BaseColumns{
        public static final String TABLE_NAME = "review";
        public static final String COLUMN_AUTHOR= "author";
        public static final String COLUMN_CONTENT = "review_content";
        public static final String COLUMN_MOVIE_KEY = "movie_id";
    }
    public static final class TrailerEntry implements BaseColumns{
        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_TRAILER_KEY = "trailer_key";
        public static final String COLUMN_TRAILER_NAME = "title";
        public static final String COLUMN_LINK= "link";
        public static final String COLUMN_MOVIE_KEY = "movie_id";
    }
}
