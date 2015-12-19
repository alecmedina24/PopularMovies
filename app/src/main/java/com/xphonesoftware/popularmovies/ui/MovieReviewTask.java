package com.xphonesoftware.popularmovies.ui;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.xphonesoftware.popularmovies.models.Movie;
import com.xphonesoftware.popularmovies.models.ReviewResults;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alecmedina on 12/10/15.
 */
public class MovieReviewTask extends AsyncTask<Integer, Void, Void> {

    final String LOG_TAG = MovieReviewTask.class.getSimpleName();
    private Movie movie;
    private ReviewResults reviewResults;
    private MovieDetailFragment movieDetailFragment;
    private ReviewAdapter reviewAdapter;
    private ProgressBar progressBar;

    public MovieReviewTask(Movie movie, MovieDetailFragment movieDetailFragment) {
        this.movie = movie;
        this.movieDetailFragment = movieDetailFragment;
        this.progressBar = movieDetailFragment.getProgressBar();
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int page = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            final String urlTrailerBase = "http://api.themoviedb.org/3/movie";
            final String API_KEY = "api_key";
            final String API_VALUE = "ed1b942e1ee7f2f81bec1461b84e5e87";
            final int movieId = movie.getId();
            final String videoPath = "reviews";

            Uri videoUri = Uri.parse(urlTrailerBase).buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .appendPath(videoPath).appendQueryParameter(API_KEY, API_VALUE).
                            appendQueryParameter("page", String.valueOf(page)).build();
            Log.v(LOG_TAG, videoUri.toString());

            URL url = new URL(videoUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            Gson gson = new Gson();
            reviewResults = gson.fromJson(reader, ReviewResults.class);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("log", "Error closing stream", e);
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        movieDetailFragment.setIsLoading(false);
        this.reviewAdapter = movieDetailFragment.getReviewAdapter();
        reviewAdapter.updateReviews(reviewResults);
        movieDetailFragment.setTotalPageCount(reviewResults.getTotalPages());
        progressBar.setVisibility(View.GONE);
    }
}
