package com.rizwan.moviesapp.db;

public interface DatabaseConstant {
    String DATABASE_NAME = "movies_db";
    int DB_VERSION = 1;

    interface Tables {
        String MOVIES = "MoviesInfo";
    }

    interface Fields {
        interface Movies {
            String ID = "id";
            String VOTE_AVERAGE = "average";
            String TITLE = "title";
            String POSTER_PATH = "poster_path";
            String OVERVIEW = "overview";
            String RELEASE_DATE = "release_date";
        }
    }
}
