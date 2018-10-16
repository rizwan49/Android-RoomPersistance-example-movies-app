package com.rizwan.moviesapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rizwan.moviesapp.R;
import com.rizwan.moviesapp.Utils;
import com.rizwan.moviesapp.apis.model.MoviesInfo;

import static com.rizwan.moviesapp.apis.MoviesApiService.IMAGE_PATH;
import static com.rizwan.moviesapp.apis.MoviesApiService._SCHEME;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_DATA = "movies_info";
    MoviesInfo moviesInfo;

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
        tvRating.setText(moviesInfo.getPopularity() + "/10");


        Uri.Builder builder = new Uri.Builder();
        builder.scheme(_SCHEME)
                .appendEncodedPath(IMAGE_PATH)
                .appendEncodedPath(moviesInfo.getPosterPath()).build();
        Utils.loadImage(this, imageViewPoster, builder.build(), R.drawable.ic_broken_image);

        tvSummary.setText(moviesInfo.getOverview());
    }

    TextView tvName, tvReleaseDate, tvDuration, tvRating, tvSummary;
    Button btnFavorite;
    ImageView imageViewPoster;

    private void init() {
        tvName = findViewById(R.id.tv_name);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvDuration = findViewById(R.id.tv_duration);
        tvRating = findViewById(R.id.tv_rating);
        btnFavorite = findViewById(R.id.btn_favorite);
        imageViewPoster = findViewById(R.id.img_poster);
        tvSummary = findViewById(R.id.tv_description);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    public static void startDetailActivity(Context context, MoviesInfo selectedData) {
        Intent detailIntent = new Intent(context, DetailActivity.class);
        detailIntent.putExtra(EXTRA_DATA, selectedData);
        context.startActivity(detailIntent);
    }
}
