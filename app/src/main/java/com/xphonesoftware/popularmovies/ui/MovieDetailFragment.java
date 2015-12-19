package com.xphonesoftware.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xphonesoftware.popularmovies.R;
import com.xphonesoftware.popularmovies.models.Movie;
import com.xphonesoftware.popularmovies.models.VideoResultDetails;
import com.xphonesoftware.popularmovies.models.VideoResults;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment {
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    private static final String MOVIE = "movie";
    private String urlPosterBase = "http://image.tmdb.org/t/p/original/";
    private Movie movie;

    private VideoResults movieMedia;
    private ReviewAdapter reviewAdapter;
    private int page = 0;
    private int totalPages;
    private ProgressBar progressBar;
    private Boolean isLoading = false;
    private Favorites favorites;
    private ListView listView;

    @Bind(R.id.movie_title)
    TextView titleText;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.user_rating)
    TextView userRating;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.checkBox)
    CheckBox favoritesStar;
    @Bind(R.id.movie_thumbnail)
    ImageView movieThumbnail;
    @Bind(R.id.no_movies)
    TextView noMovies;

    public static MovieDetailFragment newInstance() {
        MovieDetailFragment fragment = new MovieDetailFragment();
        return fragment;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        View headerLayout = inflater.inflate(R.layout.movie_detail_header, null);
        listView = (ListView) view.findViewById(R.id.review_listView);
        listView.addHeaderView(headerLayout);
        View footerLayout = inflater.inflate(R.layout.movie_detail_footer, null);
        progressBar = (ProgressBar) footerLayout.findViewById(R.id.progressBar);
        listView.addFooterView(footerLayout);
        footerLayout.setVisibility(View.VISIBLE);
        reviewAdapter = new ReviewAdapter(getActivity(), this);
        listView.setAdapter(reviewAdapter);

        ScrollListener scrollListener = new ScrollListener(this, progressBar);
        listView.setOnScrollListener(scrollListener);

        ButterKnife.bind(this, view);

        return view;
    }

    public void update(Movie newMovie) {
        this.movie = newMovie;
        favorites = new Favorites(getActivity());
        if (favorites.contains(String.valueOf(movie.getId()))) {
            favoritesStar.setChecked(true);
        } else {
            favoritesStar.setChecked(false);
        }
        favoritesStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    favorites.add(String.valueOf(movie.getId()));
                } else {
                    favorites.remove(String.valueOf(movie.getId()));

                }
            }
        });

        titleText.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        userRating.setText(movie.getVoteAverage() + "/10");
        releaseDate.setText(movie.getReleaseDate());

        Picasso.with(getActivity()).load(urlPosterBase + movie.getPosterPath()).into(movieThumbnail);
        getData();

        if (movie == null) {
            noMovies.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            noMovies.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public ReviewAdapter getReviewAdapter() {
        return this.reviewAdapter;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setIsLoading(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void getData() {
        if (!isLoading) {
            isLoading = true;
            MovieMediaTask fetchMovieMediaData = new MovieMediaTask();
            fetchMovieMediaData.execute();
            MovieReviewTask reviews = new MovieReviewTask(movie, this);
            reviews.execute(++page);
        }
    }

    public void setTotalPageCount(int totalPages) {
        this.totalPages = totalPages;
    }

    private class ScrollListener implements AbsListView.OnScrollListener {

        MovieDetailFragment movieDetailFragment;
        ProgressBar progressBar;

        public ScrollListener(MovieDetailFragment movieDetailFragment, ProgressBar progressBar) {
            this.movieDetailFragment = movieDetailFragment;
            this.progressBar = progressBar;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // ignored
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // if at the bottom of the list show a spinner and request the next page of movies
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                progressBar.setVisibility(View.VISIBLE);
                if (page < totalPages) {
                    getData();
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class MovieMediaTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final String urlTrailerBase = "http://api.themoviedb.org/3/movie";
                final String API_KEY = "api_key";
                final String API_VALUE = "ed1b942e1ee7f2f81bec1461b84e5e87";
                final String movieId = String.valueOf(movie.getId());
                final String videoPath = "videos";

                Uri videoUri = Uri.parse(urlTrailerBase).buildUpon()
                        .appendPath(movieId)
                        .appendPath(videoPath).appendQueryParameter(API_KEY, API_VALUE).build();
                Log.v(TAG, videoUri.toString());

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
                movieMedia = gson.fromJson(reader, VideoResults.class);

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
            LayoutInflater inflater = getActivity().getLayoutInflater();
            LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.media_list);
            for (final VideoResultDetails video : movieMedia.getResults()) {
                View view = inflater.inflate(R.layout.media_button_layout, null);
                Button button = (Button) view.findViewById(R.id.trailer_button);
                button.setText(video.getType());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String trailerKey = video.getKey();
                        String trailerUrl = "https://www.youtube.com/watch?v=" + trailerKey;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                        startActivity(intent);
                    }
                });
                linearLayout.addView(view);
            }
        }
    }

}
