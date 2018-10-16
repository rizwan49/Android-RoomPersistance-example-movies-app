package com.rizwan.moviesapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rizwan.moviesapp.FontUtils;
import com.rizwan.moviesapp.R;
import com.rizwan.moviesapp.Utils;
import com.rizwan.moviesapp.adapters.MoviesListAdapter;
import com.rizwan.moviesapp.apis.MoviesApiService;
import com.rizwan.moviesapp.apis.model.MoviesInfo;
import com.rizwan.moviesapp.apis.model.MoviesModel;
import com.rizwan.moviesapp.model.MainScreenPresenterImpl;
import com.rizwan.moviesapp.presenter.MainScreenPresenter;
import com.rizwan.moviesapp.view.ActivityView;

import java.util.ArrayList;

import retrofit2.Response;

import static android.graphics.Typeface.BOLD;
import static com.rizwan.moviesapp.apis.ResponseCodeConstants.INTERNET_CONNECTION;
import static com.rizwan.moviesapp.apis.ResponseCodeConstants.SERVER_ERROR;


/**
 * 1. loadMoviesList() method, which will increase page number and do call;
 * 2. MVP pattern used for validate and make a call using interface;
 */

public class MoviesListActivity extends AppCompatActivity implements ActivityView, View.OnClickListener, MoviesListAdapter.ListItemOnClickListener {

    private View rootView, mErrorView, resultView;
    private MainScreenPresenter presenter;
    private View progressBarView;
    private Button retry;

    private boolean loading;
    private int totalItemCount, lastVisibleItem, visibleThreshold = 5, page, totalPage = 0, DEFAULT_SPAN_COUNT = 2;

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private MoviesListAdapter adapter;
    private String TAG = this.getClass().getName();
    private String selectedUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        init();
        setupErrorView();
        setupDataView();
        setupListeners();
        resetAndCallAnApi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                selectedUrl = MoviesApiService.URL_POPULAR;
                break;

            case R.id.menu_top_rated:
                selectedUrl = MoviesApiService.URL_TOP_RATED;
                break;
        }

        resetAndCallAnApi();
        return true;
    }

    private void resetAndCallAnApi() {
        page = 0;
        loading = true;
        if (adapter != null)
            adapter.clearAllItems();
        loadMoviesList();
    }


    private void loadMoviesList() {
        Utils.showViews(progressBarView);
        presenter.validateAndProceed(this, selectedUrl, ++page);
    }

    private void setupListeners() {
        retry.setOnClickListener(this);
    }

    private void init() {
        presenter = new MainScreenPresenterImpl(this);
        rootView = findViewById(R.id.rootView);
        progressBarView = findViewById(R.id.progress_bar);
        mErrorView = findViewById(R.id.error_view);
        resultView = findViewById(R.id.resultView);
        selectedUrl = MoviesApiService.URL_POPULAR;
    }


    private void setupDataView() {
        recyclerView = resultView.findViewById(R.id.recyclerViewList);

        adapter = new MoviesListAdapter(new ArrayList<MoviesInfo>(), this);
        layoutManager = new GridLayoutManager(this, DEFAULT_SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, " scrolling:" + dy + " dx:" + dx + " chcount:" + layoutManager.getChildCount());
                if (dy <= 0) {
                    return;
                }
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (page <= totalPage && loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    loading = false;
                    loadMoviesList();
                    Log.d(TAG, " loading...");

                }
                if (page > totalPage) {
                    if (!recyclerView.hasFixedSize())
                        recyclerView.setHasFixedSize(true);
                }
            }

        });
    }

    private void setupViewAdapter(MoviesModel info) {
        adapter.addAllItem(info.getUpcomingMoviesList());
        loading = true;
        Utils.hideViews(progressBarView);
    }

    private void setupErrorView() {
        snackbar = Snackbar.make(rootView, R.string.server_error, Snackbar.LENGTH_INDEFINITE);
        retry = mErrorView.findViewById(R.id.buttonRetry);
        retry.setTypeface(FontUtils.getLight(), BOLD);
    }

    Snackbar snackbar;

    @Override
    public void error(int type) {
        dismissSnackBar();
        switch (type) {
            case SERVER_ERROR:
                Utils.hideViews(progressBarView, resultView);
                Utils.showViews(mErrorView);
                snackbar.show();
                break;
            case INTERNET_CONNECTION:
                Utils.hideViews(progressBarView, resultView);
                Utils.showViews(mErrorView);
                snackbar = Snackbar.make(rootView, R.string.network_error, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                break;
        }
    }

    private void dismissSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @Override
    public void getServerResponse(Response<MoviesModel> response) {
        Log.d("mainActivity", "data getting: ");
        Utils.hideViews(progressBarView, mErrorView);
        Utils.showViews(resultView);
        page = response.body().getPage();
        totalPage = response.body().getTotalPages();
        setupViewAdapter(response.body());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRetry:
                Utils.hideViews(mErrorView, resultView);
                Utils.showViews(progressBarView);
                dismissSnackBar();
                loadMoviesList();
                break;
        }
    }

    /***
     * this method invoked from recyclerView adapter
     * @param selectedObject adapter item object;
     */
    @Override
    public void onListItemClick(MoviesInfo selectedObject) {
        if (selectedObject != null) {
            Log.d(TAG, "selected:" + selectedObject.getId() + " Title:" + selectedObject.getTitle());
        }
    }
}
