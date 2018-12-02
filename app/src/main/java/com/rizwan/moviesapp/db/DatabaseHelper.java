package com.rizwan.moviesapp.db;

import android.arch.persistence.room.Room;

import com.rizwan.moviesapp.MoviesApplication;

/***
 * Singleton DatabaseHelper class
 */
public class DatabaseHelper {

    private static AppDatabase db;
    private final static String TAG = DatabaseHelper.class.getName();

    private DatabaseHelper() {
    }

    /***
     *
     * @return an instance of AppDatabase
     */
    public static AppDatabase getInstance() {
        if (db == null) {
            // To make thread safe
            synchronized (DatabaseHelper.class) {
                // check again as multiple threads
                if (db == null) {
                    db = Room.databaseBuilder(MoviesApplication.getContext(),
                            AppDatabase.class, DatabaseConstant.DATABASE_NAME).build();

                }
            }
        }
        return db;
    }

    public static void dbClose() {
        if (db != null && db.isOpen())
            db.close();
        db = null;
    }
}
