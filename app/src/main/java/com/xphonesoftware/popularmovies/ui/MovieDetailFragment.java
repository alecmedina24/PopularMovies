package com.xphonesoftware.popularmovies.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xphonesoftware.popularmovies.R;
import com.xphonesoftware.popularmovies.models.Movie;

public class MovieDetailFragment extends Fragment {
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    private static final String MOVIE = "movie";
    private String urlPosterBase = "http://image.tmdb.org/t/p/original/";

    private Movie movie;

    private OnFragmentInteractionListener mListener;

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(MovieDetailFragment.MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable(MOVIE);
            Log.v(TAG, movie.getTitle());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Below sets all text and resources related to each movie within the detail activity
        TextView titleText = (TextView) view.findViewById(R.id.movie_title);
        titleText.setText(movie.getTitle());

        TextView overViewText = (TextView) view.findViewById(R.id.overview);
        overViewText.setText(movie.getOverview());

        TextView userRating = (TextView) view.findViewById(R.id.user_rating);
        String userRatingNum = String.valueOf(movie.getVoteAverage());
        userRating.setText(userRatingNum + "/10");

        TextView releaseDate = (TextView) view.findViewById(R.id.release_date);
        releaseDate.setText(movie.getReleaseDate());

        ImageView movieThumbnail = (ImageView) view.findViewById(R.id.movie_thumbnail);
        Picasso.with(getActivity()).load(urlPosterBase + movie.getPosterPath()).into(movieThumbnail);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

}
