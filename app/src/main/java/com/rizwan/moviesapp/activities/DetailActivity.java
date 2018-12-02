package com.rizwan.moviesapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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
public class DetailActivity extends AppCompatActivity implements DetailInfoAdapter.ListItemOnClickListener {

    private static final String EXTRA_DATA = "movies_info";
    public static final String REVIEWS = "Reviews";
    private static final String VIDEOS_HEADER = "Trailers";
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

    private String TAG = DetailActivity.class.getName();
    private VideosAndReviewsModel videosAndReviewsModels;
    private DetailInfoAdapter adapter;

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

    private void populateUI() {
        tvName.setText(moviesInfo.getTitle());
        tvReleaseDate.setText(moviesInfo.getReleaseDate());
        tvRating.setText(getString(R.string.average_rate, moviesInfo.getVoteAverage()));
        Log.d(TAG, "ID:" + moviesInfo.getId());

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(_SCHEME)
                .appendEncodedPath(IMAGE_PATH)
                .appendEncodedPath(moviesInfo.getPosterPath()).build();
        Utils.loadImage(this, imageViewPoster, builder.build(), R.drawable.ic_broken_image);

        tvSummary.setText(moviesInfo.getOverview());

        getVideosAndReviews();

    }

    private void init() {
        adapterInit();
    }

    private void adapterInit() {
        adapter = new DetailInfoAdapter(null, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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
    public void playVideo(String key){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        // Check if the youtube app exists on the device
        if (intent.resolveActivity(getPackageManager()) == null) {
            // If the youtube app doesn't exist, then use the browser
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key));
        }

        startActivity(intent);
    }
}
