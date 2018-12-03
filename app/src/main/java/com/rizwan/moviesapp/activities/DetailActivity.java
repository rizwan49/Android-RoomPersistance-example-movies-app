package com.rizwan.moviesapp.activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rizwan.moviesapp.R;
import com.rizwan.moviesapp.Utils;
import com.rizwan.moviesapp.adapters.DetailInfoAdapter;
import com.rizwan.moviesapp.apis.RestClient;
import com.rizwan.moviesapp.apis.model.MoviesInfo;
import com.rizwan.moviesapp.apis.model.detail.VideosAndReviewsModel;
import com.rizwan.moviesapp.apis.model.detail.review.ReviewFieldModel;
import com.rizwan.moviesapp.viewmodel.DetailViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.rizwan.moviesapp.apis.MoviesApiService.IMAGE_PATH;
import static com.rizwan.moviesapp.apis.MoviesApiService._SCHEME;

/***
 * 1. getting information from the previous activity and check all the required conditions;
 * 2. setup information to desired views;
 *
 */

/***
 *
 * 1. db setup
 * 2. add into db using ViewModel;
 * 3. in main screen add menu
 * 4. select fav option fetch from db
 * 5. load it on recyclerView of mainScreen
 */
public class DetailActivity extends AppCompatActivity implements DetailInfoAdapter.ListItemOnClickListener {

    private static final String EXTRA_DATA = "movies_info";
    public static final String REVIEWS = "Reviews";
    private static final String VIDEOS_HEADER = "Trailers";
    private static final String YOU_TUBE_PATH = "http://www.youtube.com/watch?v=";
    private MoviesInfo moviesInfo;
    LinearLayoutManager layoutManager;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_description)
    TextView tvSummary;
    @BindView(R.id.recyclerViewList)
    RecyclerView recyclerView;
    @BindView(R.id.img_poster)
    ImageView imageViewPoster;
    @BindView(R.id.rootView)
    View rootView;
    @BindView(R.id.btn_favorite)
    Button btnFavorite;

    private String TAG = DetailActivity.class.getName();
    private VideosAndReviewsModel videosAndReviewsModels;
    private DetailInfoAdapter adapter;
    private DetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        moviesInfo = intent.getParcelableExtra(EXTRA_DATA);
        if (moviesInfo == null) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        setTitle(R.string.movie_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();
        populateUI();

    }

    final android.arch.lifecycle.Observer<MoviesInfo> favObserver = new android.arch.lifecycle.Observer<MoviesInfo>() {
        @Override
        public void onChanged(@Nullable final MoviesInfo movie) {
            // Update the UI, in this case, a TextView.
            Log.d(TAG, "updating..");
            setupFavBtn(movie);
        }
    };

    private void populateUI() {
        tvName.setText(moviesInfo.getTitle());
        tvReleaseDate.setText(moviesInfo.getReleaseDate());
        tvRating.setText(getString(R.string.average_rate, moviesInfo.getVoteAverage()));
        Log.d(TAG, "ID:" + moviesInfo.getId());

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(_SCHEME)
                .appendEncodedPath(IMAGE_PATH)
                .appendEncodedPath(moviesInfo.getPosterPath()).build();
        Utils.loadImage(this, imageViewPoster, builder.build(), R.drawable.ic_place_holder, R.drawable.ic_broken_image);

        tvSummary.setText(moviesInfo.getOverview());

        getVideosAndReviews();
    }

    boolean isAdded;

    private void setupFavBtn(MoviesInfo info) {
        if (info != null) {
            isAdded = true;
            btnFavorite.setText(R.string.remove_from_fav);
        } else {
            isAdded = false;
            btnFavorite.setText(R.string.mark_as_favorite);
        }
    }

    private void init() {
        adapterInit();
    }

    private void adapterInit() {

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.getMovieById(moviesInfo.getId()).observe(this, favObserver);
        adapter = new DetailInfoAdapter(viewModel.getList(), this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @OnClick(R.id.btn_favorite)
    public void onClick() {
        if (!isAdded) {
            viewModel.insertIntoDb(moviesInfo);
            Snackbar.make(rootView, R.string.movies_added_into_collection, Snackbar.LENGTH_LONG).show();
        } else {
            viewModel.removeMovie(moviesInfo);
            Snackbar.make(rootView, R.string.removed_from_list, Snackbar.LENGTH_LONG).show();
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    public static void startDetailActivity(Context context, MoviesInfo selectedData, ImageView bannerView) {
        Intent detailIntent = new Intent(context, DetailActivity.class);
        detailIntent.putExtra(EXTRA_DATA, selectedData);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity) context, bannerView, context.getString(R.string.transition_banner_id));
        context.startActivity(detailIntent, options.toBundle());
    }

    public void getVideosAndReviews() {
        if (viewModel.getList() != null) return;

        final CompositeDisposable disposable = new CompositeDisposable();
        RestClient.getApiService(this).getVideos(moviesInfo.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<VideosAndReviewsModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Response<VideosAndReviewsModel> response) {
                        Log.d(TAG, response.message());
                        if (response.code() == 200) {
                            videosAndReviewsModels = response.body();
                            ReviewFieldModel headerItem = new ReviewFieldModel();
                            headerItem.setType(VIDEOS_HEADER);
                            videosAndReviewsModels.getResults().add(0, headerItem);
                            getReview();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        disposable.clear();
                    }
                });
    }

    public void getReview() {
        if (viewModel.getList() != null) return;

        final CompositeDisposable disposable = new CompositeDisposable();
        RestClient.getApiService(this).getReviews(moviesInfo.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<VideosAndReviewsModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(Response<VideosAndReviewsModel> response) {
                        Log.d(TAG, response.message());
                        if (response.code() == 200) {
                            ReviewFieldModel temp = new ReviewFieldModel();
                            temp.setType(REVIEWS);
                            videosAndReviewsModels.getResults().add(videosAndReviewsModels.getResults().size(), temp);
                            videosAndReviewsModels.getResults().addAll(response.body().getResults());
                            viewModel.setList(videosAndReviewsModels.getResults());
                            adapter.addAll(videosAndReviewsModels.getResults());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        disposable.clear();
                    }
                });
    }

    @Override
    public void onListItemClick(ReviewFieldModel selectedItem) {
        playVideo(selectedItem.getKey());
    }

    public void playVideo(String key) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        // Check if the youtube app exists on the device
        if (intent.resolveActivity(getPackageManager()) == null) {
            // If the youtube app doesn't exist, then use the browser
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(YOU_TUBE_PATH + key));
        }

        startActivity(intent);
    }
}
