package com.rizwan.moviesapp.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.rizwan.moviesapp.apis.model.MoviesInfo;

@Database(entities = {MoviesInfo.class}, version = DatabaseConstant.DB_VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoMovies daoMovies();
}
