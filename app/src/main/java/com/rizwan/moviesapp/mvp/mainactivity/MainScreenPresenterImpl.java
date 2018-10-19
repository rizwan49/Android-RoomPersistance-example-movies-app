package com.rizwan.moviesapp.mvp.mainactivity;

import android.text.TextUtils;
import android.util.Log;

import com.rizwan.moviesapp.apis.MessageEvent;
import com.rizwan.moviesapp.apis.NoConnectivityException;
import com.rizwan.moviesapp.apis.ResponseCodeConstants;
import com.rizwan.moviesapp.apis.RestClient;
import com.rizwan.moviesapp.apis.model.MoviesModel;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.rizwan.moviesapp.apis.ResponseCodeConstants.INTERNET_CONNECTION;
import static com.rizwan.moviesapp.apis.ResponseCodeConstants.SOMETHING_WENT_WRONG;
import static com.rizwan.moviesapp.apis.ResponseCodeConstants.SUCCESS;

/**
 * Created by abdul on 14/10/18.
 */

public class MainScreenPresenterImpl implements MainScreenPresenter {
    private final ActivityView mainActivityView;
    private String selectedUrl;
    private final String TAG = MainScreenPresenterImpl.class.getName();

    public MainScreenPresenterImpl(ActivityView mainScreenView) {
        this.mainActivityView = mainScreenView;
    }

    @Override
    public void validateAndProceed(String selectedUrl, int page) {
        this.selectedUrl = selectedUrl;
        Log.d(TAG, "sorting by:" + selectedUrl);
        if (!TextUtils.isEmpty(selectedUrl))
            doCallOrErrorHandle(ResponseCodeConstants.VALID, page);
        else
            doCallOrErrorHandle(ResponseCodeConstants.SOMETHING_WENT_WRONG, page);
    }

    @Override
    public void doCallOrErrorHandle(int code, int page) {
        Log.d(TAG, "validation code :" + code);
        switch (code) {
            case ResponseCodeConstants.VALID:
                doRestApiCall(page);
                break;
            default:
                mainActivityView.errorView(code);
        }
    }


    /***
     * this method used to call an api, passing page number
     * @param page pass required pageNumber to get the list of movie's information;
     */
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
                        if (response.code() == SUCCESS)
                            mainActivityView.getServerResponse(response);

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NoConnectivityException)
                            EventBus.getDefault().post(new MessageEvent(INTERNET_CONNECTION));
                        else
                            EventBus.getDefault().post(new MessageEvent(SOMETHING_WENT_WRONG));
                    }

                    @Override
                    public void onComplete() {
                        disposable.clear();
                    }
                });
    }
}
