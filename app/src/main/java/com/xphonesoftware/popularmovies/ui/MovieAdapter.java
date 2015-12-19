package com.xphonesoftware.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xphonesoftware.popularmovies.R;
import com.xphonesoftware.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Movie adapter to receive and populate gridView
 */
public class MovieAdapter extends BaseAdapter {

    private final String TAG = MovieAdapter.class.getSimpleName();
    private Context context;
    private PosterDisplayFragment posterDisplayFragment;
    private ArrayList<Movie> movies;

    public void updateMovies(List<Movie> newMovies) {
        movies.addAll(newMovies);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movies.clear();
    }

    public MovieAdapter(Context context, PosterDisplayFragment posterDisplayFragment) {
        this.context = context;
        this.posterDisplayFragment = posterDisplayFragment;
        movies = new ArrayList<>();
    }

    public int getCount() {
        return movies.size();
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (posterDisplayFragment.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                imageView.setLayoutParams(new GridLayoutManager.LayoutParams(540, 770));
            } else {
                imageView.setLayoutParams(new GridLayoutManager.LayoutParams(900, 1300));
            }
        } else {
            imageView = (ImageView) convertView;
        }

        // issue request to retrieve image for movie poster
        final Movie movie = movies.get(position);
        String urlPosterBase = "http://image.tmdb.org/t/p/w185/";
        String posterPath = movie.getPosterPath();
        Picasso.with(context).load(urlPosterBase + posterPath).placeholder(R.drawable.zoom_loading)
                .error(R.drawable.not_available).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // starts the detail activity when the poster is clicked
                Log.v(TAG, movie.getTitle());
                boolean hasTwoFragments = posterDisplayFragment.getResources().getBoolean(R.bool.has_two_panes);
                if (hasTwoFragments) {
                    ((MainActivity) posterDisplayFragment.getActivity()).onMovieSelected(movie);
                } else {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.MOVIE_ARG, movie);
                    context.startActivity(intent);
                }
            }
        });
        return imageView;
    }

}
