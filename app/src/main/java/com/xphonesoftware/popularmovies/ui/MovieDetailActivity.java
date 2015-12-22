package com.xphonesoftware.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xphonesoftware.popularmovies.R;
import com.xphonesoftware.popularmovies.models.Movie;

/**
 * Display details about a specified movie
 */
public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_ARG = "movie";
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        this.movie = (Movie) extras.get(MOVIE_ARG);
        setContentView(R.layout.activity_movie_detail);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        MovieDetailFragment mdf = (MovieDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        mdf.update(movie);
    }
}
