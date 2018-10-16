package com.rizwan.moviesapp.presenter;

import android.content.Context;

/**
 * Created by abdul on 14/10/18.
 */

public interface MainScreenPresenter {
    void doCallOrErrorHandle(int code, int page);

    void validateAndProceed(Context mContext, int page);
}
