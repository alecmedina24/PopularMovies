package com.xphonesoftware.popularmovies.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xphonesoftware.popularmovies.models.Movie;

/**
 * Display details about a specified movie
 */
public class MovieDetailActivity extends AppCompatActivity
        implements MovieDetailFragment.OnFragmentInteractionListener {

    public static final String MOVIE_ARG = "movie";
    private MovieDetailFragment movieDetailFragment;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        this.movie = (Movie) extras.get(MOVIE_ARG);
        this.movieDetailFragment = MovieDetailFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                movieDetailFragment).commit();
    }

    // Necessary method from implementing OnFragmentInteractionListener
    public void onFragmentInteraction(Uri uri) {
        // Not used
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        movieDetailFragment.update(movie);
    }
}
