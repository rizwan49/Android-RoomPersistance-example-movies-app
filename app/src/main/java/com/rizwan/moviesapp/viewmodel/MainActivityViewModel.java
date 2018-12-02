package com.rizwan.moviesapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.rizwan.moviesapp.apis.model.MoviesInfo;
import com.rizwan.moviesapp.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = DetailViewModel.class.getName();
    public LiveData<List<MoviesInfo>> liveDataList;
    private boolean favMenuSelected;
    private ArrayList<MoviesInfo> favList;

    public MainActivityViewModel() {
        DatabaseHelper.getInstance();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
        DatabaseHelper.dbClose();
    }

    public LiveData<List<MoviesInfo>> getList() {
        if (liveDataList == null) {
            liveDataList = DatabaseHelper.getInstance().daoMovies().getAllMovies();
        }
        return liveDataList;
    }

    public void setArrayList(List<MoviesInfo> list) {
        favList = new ArrayList<>();
        favList.addAll(list);
    }

    public ArrayList<MoviesInfo> getFavList() {
        return favList;
    }

    public void setFav(boolean fav) {
        this.favMenuSelected = fav;
    }

    public boolean isFavMenuSelected() {
        return favMenuSelected;
    }
}
