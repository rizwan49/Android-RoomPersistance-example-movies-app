package com.rizwan.moviesapp.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rizwan.moviesapp.Utils;
import com.rizwan.moviesapp.apis.ResponseCodeConstants;
import com.rizwan.moviesapp.apis.RestClient;
import com.rizwan.moviesapp.apis.model.MoviesModel;
import com.rizwan.moviesapp.presenter.MainScreenPresenter;
import com.rizwan.moviesapp.view.ActivityView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.rizwan.moviesapp.apis.ResponseCodeConstants.OK;
import static com.rizwan.moviesapp.apis.ResponseCodeConstants.SERVER_ERROR;

/**
 * Created by abdul on 14/10/18.
 */

public class MainScreenPresenterImpl implements MainScreenPresenter {
    ActivityView mainActivityView;
    String selectedUrl;

    public MainScreenPresenterImpl(ActivityView mainScreenView) {
        this.mainActivityView = mainScreenView;
    }

    @Override
    public void doCallOrErrorHandle(int code, int page) {
        if (code == ResponseCodeConstants.VALID) {
            doRestApiCall(page);
        } else {
            Log.d("presenter", "error:" + code);
            mainActivityView.error(code);
        }
    }

    @Override
    public void validateAndProceed(Context mContext, String selectedUrl, int page) {
        this.selectedUrl = selectedUrl;
        if (Utils.isNetworkAvailable(mContext) && !TextUtils.isEmpty(selectedUrl))
            doCallOrErrorHandle(ResponseCodeConstants.VALID, page);
        else
            doCallOrErrorHandle(ResponseCodeConstants.INTERNET_CONNECTION, page);
    }

    private void doRestApiCall(int page) {
        final CompositeDisposable disposable = new CompositeDisposable();
        RestClient.getApiService().getMoviesList(selectedUrl, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MoviesModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Response<MoviesModel> response) {
                        if (response.code() == OK)
                            mainActivityView.getServerResponse(response);
                        else
                            mainActivityView.error(SERVER_ERROR);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainActivityView.error(SERVER_ERROR);
                    }

                    @Override
                    public void onComplete() {
                        disposable.clear();
                    }
                });
    }
}
