package com.rizwan.moviesapp.mvp.mainactivity;


import com.rizwan.moviesapp.apis.model.MoviesModel;

import retrofit2.Response;

/**
 * Created by abdul on 14/09/18.
 */

public interface ActivityView {
    void error(int type);

    void getServerResponse(Response<MoviesModel> response);
}
