package com.rizwan.moviesapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.rizwan.moviesapp.apis.model.MoviesInfo;

import java.util.List;

@Dao
public interface DaoMovies {
    String SELECT_PREFIX = "SELECT * FROM ";

    @Query(SELECT_PREFIX + DatabaseConstant.Tables.MOVIES)
    LiveData<List<MoviesInfo>> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MoviesInfo... sms);

    @Delete
    void delete(MoviesInfo sms);
}
