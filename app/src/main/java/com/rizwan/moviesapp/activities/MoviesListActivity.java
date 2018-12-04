package com.rizwan.moviesapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rizwan.moviesapp.R;
import com.rizwan.moviesapp.Utils;
import com.rizwan.moviesapp.adapters.MoviesListAdapter;
import com.rizwan.moviesapp.apis.MessageEvent;
import com.rizwan.moviesapp.apis.MoviesApiService;
import com.rizwan.moviesapp.apis.model.MoviesInfo;
import com.rizwan.moviesapp.apis.model.MoviesModel;
import com.rizwan.moviesapp.mvp.mainactivity.ActivityView;
import com.rizwan.moviesapp.mvp.mainactivity.MainScreenPresenter;
import com.rizwan.moviesapp.mvp.mainactivity.MainScreenPresenterImpl;
import com.rizwan.moviesapp.viewmodel.MainActivityViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Response;

import static com.rizwan.moviesapp.apis.ResponseCodeConstants.INTERNET_CONNECTION;
import static com.rizwan.moviesapp.apis.ResponseCodeConstants.SERVER_ERROR;


/**
 * This activity shows the list of movie's poster;
 * 1. setup init and title
 * 2. setup error view which will appear whenever something will go wrong during network call or internet issue;
 * 3. as scrolling recyclerView it's calculating and calling again an api based on new PAGE_NUMBER and set result to existing list
 * 4. MVP pattern used for validate and make a call using interface;
 * 5. saved instance state
 * 6. clicking on poster redirecting to detail screen;
 * 7. added TransitionsAnimation;
 * 8. EventBus integrated;
 */

public class MoviesListActivity extends AppCompatActivity implements ActivityView, View.OnClickListener, MoviesListAdapter.ListItemOnClickListener {

    private static final String IS_LOADING = "isLoading";
    private static final String TOTAL_ITEM_COUNT = "totalCount";
    private static final String LAST_VISIBLE_ITEM = "lastVisibleItem";
    private static final String CURRENT_PAGE_NUMBER = "currentPageNumber";
    private static final String TOTAL_PAGE_COUNT = "totalPageCount";
    private static final int SETTING_REQUEST_CODE = 0;
    private static final String ERROR_VIEW_VISIBILITY = "errorViewVisibility";
    private static final String DATA_VIEW_VISIBILITY = "dataViewVisibility";

    private View rootView, mErrorView, resultView;
    private MainScreenPresenter presenter;
    private View progressBarView;
    private Button retry;

    private boolean loading;
    private int totalItemCount;
    private int lastVisibleItem;
    private final int visibleThreshold = 5;
    private int page = 0;
    private int totalPage = 0;
    private final int DEFAULT_SPAN_COUNT = 2;

    private GridLayoutManager layoutManager;
    private MoviesListAdapter adapter;
    private final String TAG = this.getClass().getName();
    private String selectedUrl;

    private Snackbar snackbar;
    private final String LIST = "list";
    private ArrayList<MoviesInfo> list;
    private MainActivityViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setTitle(R.string.pop_movies);
        init();
        setupDataView(savedInstanceState);
        setupListeners();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewModel.isFavMenuSelected())
            outState.putParcelableArrayList(LIST, viewModel.getFavList());
        else
            outState.putParcelableArrayList(LIST, list);

        outState.putBoolean(IS_LOADING, loading);
        outState.putInt(TOTAL_ITEM_COUNT, totalItemCount);
        outState.putInt(LAST_VISIBLE_ITEM, lastVisibleItem);
        outState.putInt(CURRENT_PAGE_NUMBER, page);
        outState.putInt(TOTAL_PAGE_COUNT, totalPage);
        outState.putInt(ERROR_VIEW_VISIBILITY, mErrorView.getVisibility());
        outState.putInt(DATA_VIEW_VISIBILITY, resultView.getVisibility());
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
                resetAndCallAnApi();
                break;

            case R.id.menu_top_rated:
                selectedUrl = MoviesApiService.URL_TOP_RATED;
                resetAndCallAnApi();
                break;

            case R.id.menu_favorites:
                if (!viewModel.isFavMenuSelected()) {
                    viewModel.setFav(true);
                    resetInfo();
                    viewModel.getList().observe(this, favObserver);
                }
                break;
        }

        return true;
    }

    final Observer<List<MoviesInfo>> favObserver = new Observer<List<MoviesInfo>>() {
        @Override
        public void onChanged(@Nullable final List<MoviesInfo> list) {
            // Update the UI, in this case, a TextView.
            Log.d(TAG, "observer working");

            viewModel.setArrayList(list);
            if (viewModel.isFavMenuSelected()) {
                resetInfo();
                setupViewAdapter(list);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(viewModel.isFavMenuSelected())
            viewModel.getList().observe(this,favObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event != null) {
            Log.d(TAG, "EventBus:" + event.getType());
            errorView(event.getType());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void resetAndCallAnApi() {
        viewModel.setFav(false);
        resetInfo();
        loadMoviesList();
    }

    private void resetInfo() {
        page = 0;
        loading = true;
        if (adapter != null) {
            adapter.clearAllItems();
        }
        if (list != null)
            list.clear();
    }


    private void loadMoviesList() {
        Utils.showViews(progressBarView);
        presenter.validateAndProceed(selectedUrl, ++page);
    }

    private void setupListeners() {
        retry.setOnClickListener(this);
    }

    private void init() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        presenter = new MainScreenPresenterImpl(this);
        rootView = findViewById(R.id.rootView);
        progressBarView = findViewById(R.id.progress_bar);
        mErrorView = findViewById(R.id.error_view);
        resultView = findViewById(R.id.resultView);
        selectedUrl = MoviesApiService.URL_POPULAR;
        retry = mErrorView.findViewById(R.id.buttonRetry);

        viewModel.getList().observe(this, favObserver);
    }


    /***
     * Checking savedInstanceState values and based on that proceeding ahead ;
     * @param savedInstanceState if this object is null then initializing the list
     *                          and proceed ahead or else proceed with the old savedState;
     */
    private void setupDataView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            list = new ArrayList<>();
            loadMoviesList();
            Log.d(TAG, "initiate saved");
        } else if (!viewModel.isFavMenuSelected()) {
            Log.d(TAG, "restore saved");
            list = savedInstanceState.getParcelableArrayList(LIST);
            loading = savedInstanceState.getBoolean(IS_LOADING);
            totalItemCount = savedInstanceState.getInt(TOTAL_ITEM_COUNT);
            lastVisibleItem = savedInstanceState.getInt(LAST_VISIBLE_ITEM);
            page = savedInstanceState.getInt(CURRENT_PAGE_NUMBER);
            totalPage = savedInstanceState.getInt(TOTAL_PAGE_COUNT);
            resultView.setVisibility(savedInstanceState.getInt(DATA_VIEW_VISIBILITY));
            mErrorView.setVisibility(savedInstanceState.getInt(ERROR_VIEW_VISIBILITY));
        } else {
            list = viewModel.getFavList();
            resultView.setVisibility(savedInstanceState.getInt(DATA_VIEW_VISIBILITY));
            mErrorView.setVisibility(savedInstanceState.getInt(ERROR_VIEW_VISIBILITY));
        }

        RecyclerView recyclerView = resultView.findViewById(R.id.recyclerViewList);
        adapter = new MoviesListAdapter(list, this);
        layoutManager = new GridLayoutManager(this, DEFAULT_SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0 || viewModel.isFavMenuSelected()) {
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

    private void setupViewAdapter(List<MoviesInfo> info) {
        adapter.addAllItem(info);
        loading = true;
        Utils.hideViews(progressBarView);
    }

    private void setupErrorNetworkView() {
        snackbar = Snackbar.make(rootView, R.string.network_error, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorButton));
        snackbar.setAction(R.string.open_setting, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                PackageManager packageManager = getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, SETTING_REQUEST_CODE);
                }
            }
        });
    }


    @Override
    public void errorView(int type) {
        switch (type) {
            case INTERNET_CONNECTION:
                Utils.hideViews(progressBarView, resultView);
                Utils.showViews(mErrorView);
                setupErrorNetworkView();
                snackbar.show();
                break;

            case SERVER_ERROR:
            default:
                Utils.hideViews(progressBarView, resultView);
                Utils.showViews(mErrorView);
                snackbar = Snackbar.make(rootView, R.string.server_error, Snackbar.LENGTH_INDEFINITE);
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

        page = Objects.requireNonNull(response.body()).getPage();
        totalPage = Objects.requireNonNull(response.body()).getTotalPages();
        if (response.body() != null)
            setupViewAdapter(response.body().getMoviesList());
        dismissSnackBar();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRetry:
                dismissSnackBar();
                Utils.hideViews(mErrorView, resultView);
                Utils.showViews(progressBarView);
                loadMoviesList();
                break;
        }
    }

    /***
     * this method invoked from recyclerView adapter
     * @param selectedObject adapter item object;
     */
    @Override
    public void onListItemClick(MoviesInfo selectedObject, View view) {
        if (selectedObject != null) {
            Log.d(TAG, "selected:" + selectedObject.getId() + " Title:" + selectedObject.getTitle());
            DetailActivity.startDetailActivity(this, selectedObject, (ImageView) view);
        }
    }
}
