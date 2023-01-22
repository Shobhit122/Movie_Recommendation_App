package molu.example.tryingsearchactivity;

import android.provider.BaseColumns;

public final class MovieContract {
    private MovieContract() {
    }

    public static class Movie implements BaseColumns{
        public static final String TABLE_NAME = "movie_table";
        public static final String COLUMN_NAME_MOVIE = "movie_name";
        public static final String COLUMN_NAME_GENRE = "movie_genre";

        public static final String SQL_CREATE_ENTRIES = "" +
                "CREATE TABLE " + Movie.TABLE_NAME + " (" +
                Movie.COLUMN_NAME_MOVIE + " TEXT, " +
                Movie.COLUMN_NAME_GENRE + " TEXT)";


        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Movie.TABLE_NAME;
    }
}
