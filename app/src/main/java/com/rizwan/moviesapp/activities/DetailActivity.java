package com.rizwan.moviesapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rizwan.moviesapp.R;
import com.rizwan.moviesapp.Utils;
import com.rizwan.moviesapp.apis.model.MoviesInfo;

import static com.rizwan.moviesapp.apis.MoviesApiService.IMAGE_PATH;
import static com.rizwan.moviesapp.apis.MoviesApiService._SCHEME;

/***
 * 1. getting information from the previous activity and check all the required conditions;
 * 2. setup information to desired views;
 *
 */
public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_DATA = "movies_info";
    private MoviesInfo moviesInfo;
    private TextView tvName, tvReleaseDate, tvRating, tvSummary;
    private ImageView imageViewPoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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


        Uri.Builder builder = new Uri.Builder();
        builder.scheme(_SCHEME)
                .appendEncodedPath(IMAGE_PATH)
                .appendEncodedPath(moviesInfo.getPosterPath()).build();
        Utils.loadImage(this, imageViewPoster, builder.build(), R.drawable.ic_broken_image);

        tvSummary.setText(moviesInfo.getOverview());
    }

    private void init() {
        tvName = findViewById(R.id.tv_name);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvRating = findViewById(R.id.tv_rating);
        imageViewPoster = findViewById(R.id.img_poster);
        tvSummary = findViewById(R.id.tv_description);

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
}
