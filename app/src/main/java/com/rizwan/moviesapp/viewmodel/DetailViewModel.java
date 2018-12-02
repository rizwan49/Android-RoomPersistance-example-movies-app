package com.rizwan.moviesapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.rizwan.moviesapp.apis.model.MoviesInfo;
import com.rizwan.moviesapp.apis.model.detail.review.ReviewFieldModel;
import com.rizwan.moviesapp.db.DatabaseHelper;

import java.util.List;

public class DetailViewModel extends ViewModel {

    private static final String TAG = DetailViewModel.class.getName();
    private List<ReviewFieldModel> list;

    public DetailViewModel() {
        DatabaseHelper.getInstance();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
        DatabaseHelper.dbClose();
    }

    public List<ReviewFieldModel> getList() {
        return list;
    }

    public void setList(List<ReviewFieldModel> list) {
        this.list = list;
    }

    public void insertIntoDb(final MoviesInfo info) {
        if (info == null) return;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper.getInstance().daoMovies().insertAll(info);
            }
        });
        t.start();
    }

}
