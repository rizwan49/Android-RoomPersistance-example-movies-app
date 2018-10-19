package com.rizwan.moviesapp.mvp.mainactivity;

/**
 * Created by abdul on 14/10/18.
 */

public interface MainScreenPresenter {
    void doCallOrErrorHandle(int code, int page);

    void validateAndProceed(String selectedUrl, int page);
}
