package com.xphonesoftware.popularmovies.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.xphonesoftware.popularmovies.R;
import com.xphonesoftware.popularmovies.models.Movie;

/**
 * This activity is the entry point to the application
 */
public class MainActivity extends AppCompatActivity implements PosterDisplayFragment.OnMovieSelectedListener {

    private int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        // sort movie poster list through an alert dialog
        if (id == R.id.action_sort) {
            final PosterDisplayFragment posterDisplayFragment =
                    getPosterDisplayFragment();
            String [] optionsId = { getString(R.string.most_popular),
                    getString(R.string.highest_rated), getString(R.string.favorite_movies)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.sort_order).setItems(optionsId,
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        order = PosterDisplayFragment.ORDER_POPULARITY;
                    } else if (which ==1) {
                        order = PosterDisplayFragment.ORDER_RATING;
                    } else {
                        order = PosterDisplayFragment.FAVORITE_MOVIES;
                    }
                    posterDisplayFragment.setSortOrder(order);
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // find the fragment used to display a grid of movie posters
    public PosterDisplayFragment getPosterDisplayFragment() {
        return (PosterDisplayFragment)
                (getSupportFragmentManager().findFragmentById(R.id.fragment));
    }

    @Override
    public void onMovieSelected(Movie movie) {
        MovieDetailFragment movieDetailFrag = (MovieDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_fragment);
        if (movieDetailFrag != null) {
            movieDetailFrag.update(movie);
        }
    }
}
