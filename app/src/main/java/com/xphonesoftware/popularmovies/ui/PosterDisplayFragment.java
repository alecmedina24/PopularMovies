package com.xphonesoftware.popularmovies.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.xphonesoftware.popularmovies.R;
import com.xphonesoftware.popularmovies.models.DiscoveredMovies;
import com.xphonesoftware.popularmovies.models.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Fragment responsible for retrieving movie data
 */
public class PosterDisplayFragment extends Fragment {

    public static final int ORDER_POPULARITY = 0;
    public static final int ORDER_RATING = 1;
    public static final int FAVORITE_MOVIES = 2;

    private int sortOrder = ORDER_POPULARITY;
    private int page = 0;
    private boolean isLoading;
    private int totalMovies;
    private ProgressBar progressBar;
    private MovieAdapter movieAdapter;
    private ScrollListener scrollListener;
    private int orientation;
    private ArrayList<Movie> favoriteMovies;
    private Favorites favorites;
    private OnMovieSelectedListener onMovieSelectedListener;

    public interface OnMovieSelectedListener {
        public void onMovieSelected(Movie movie);
    }

    public PosterDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poster_display, container, false);

        // Sets the adapter for the gridView
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        movieAdapter = new MovieAdapter(getActivity(), this);
        gridView.setAdapter(movieAdapter);

        // Sets the progress bar for the gridView
        progressBar = (ProgressBar) rootView.findViewById(R.id.show_more);
        scrollListener = new ScrollListener(this);
        gridView.setOnScrollListener(scrollListener);

        orientation = getResources().getConfiguration().orientation;
        favoriteMovies = new ArrayList<>();
        favorites = new Favorites(getActivity());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(sortOrder);
    }

    public void getData(int sortOrder) {
        if (!isLoading) {
            isLoading = true;
            FetchMovieData getMovies = new FetchMovieData();
            getMovies.execute(++page, sortOrder);
        }
    }

    public void getNextPage() {
        getData(sortOrder);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
        movieAdapter.clearMovies();
        page = 1;
        getData(sortOrder);
    }

    public int getOrientation() {
        return orientation;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onMovieSelectedListener = (PosterDisplayFragment.OnMovieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onMovieSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onMovieSelectedListener = null;
    }


    /*
     * Retrieve movie data from the moviedb web service
     */
    private class FetchMovieData extends AsyncTask<Integer, Void, Void> {

        private final String LOG_TAG = FetchMovieData.class.getSimpleName();

        private DiscoveredMovies discoveredMovies;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isLoading = false;
            if (sortOrder != FAVORITE_MOVIES) {
                movieAdapter.updateMovies(discoveredMovies.getMovies());
            } else {
                movieAdapter.clearMovies();
                movieAdapter.updateMovies(favoriteMovies);
            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int page = params[0];
            int sortOrder = params[1];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            Set<String> favoritesIdList = new HashSet<>();

            try {
                String baseUrl = "http://api.themoviedb.org/3/discover/movie?";
                switch (sortOrder) {
                    case ORDER_POPULARITY:
                        baseUrl += "sort_by=popularity.desc";
                        break;
                    case ORDER_RATING:
                        baseUrl += "sort_by=vote_average.desc";
                        break;
                    case FAVORITE_MOVIES:
                        baseUrl = "http://api.themoviedb.org/3/movie";
                        favoritesIdList = favorites.get("Favorites");
                        break;
                }
                final String API_KEY = "api_key";
                final String API_VALUE = "ed1b942e1ee7f2f81bec1461b84e5e87"; // TODO - replace/remove with API key

                if (sortOrder != FAVORITE_MOVIES) {
                    Uri builtUri = Uri.parse(baseUrl).buildUpon()
                            .appendQueryParameter(API_KEY, API_VALUE).appendQueryParameter("page",
                                    String.valueOf(page)).build();

                    URL url = new URL(builtUri.toString());

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
                    discoveredMovies = gson.fromJson(reader, DiscoveredMovies.class);

                    totalMovies = discoveredMovies.getTotalMovies();

                } else {
                    favoriteMovies.clear();
                    for (String movie : favoritesIdList) {
                        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                                .appendQueryParameter(API_KEY, API_VALUE).appendPath(movie).build();
                        Log.v("XXX", String.valueOf(builtUri));

                        URL url = new URL(builtUri.toString());

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
                        favoriteMovies.add(gson.fromJson(reader, Movie.class));
                    }
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }
    }

    private class ScrollListener implements AbsListView.OnScrollListener {

        PosterDisplayFragment posterDisplayFragment;
        ProgressBar progressBar;

        public ScrollListener(PosterDisplayFragment posterDisplayFragment) {
            this.posterDisplayFragment = posterDisplayFragment;
            this.progressBar = this.posterDisplayFragment.getProgressBar();
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // ignored
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // if at the bottom of the list show a spinner and request the next page of movies
            if (sortOrder != FAVORITE_MOVIES) {
                if (firstVisibleItem + visibleItemCount == totalItemCount &&
                        (firstVisibleItem + visibleItemCount) < totalMovies) {
                    progressBar.setVisibility(View.VISIBLE);
                    posterDisplayFragment.getNextPage();
                }
            }
        }
    }
}
