package com.xphonesoftware.popularmovies.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;
import com.xphonesoftware.popularmovies.R;
import com.xphonesoftware.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

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

    public int getCount(){
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
        if(convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridLayoutManager.LayoutParams(540, 770));
        } else {
            imageView = (ImageView) convertView;
        }

        if (movies.size() == 0) {
            // return hardcoded image if no movies have been retrieved
            imageView.setImageResource(mImageIds[1]);
        } else {
            // issue request to retrieve image for movie poster
            final Movie movie = movies.get(position);
            String urlPosterBase = "http://image.tmdb.org/t/p/w185/";
            String posterPath = movie.getPosterPath();
            if (movie.getPosterPath() == null) {
                imageView.setImageResource(mImageIds[0]);
            } else {
                Picasso.with(context).load(urlPosterBase + posterPath).into(imageView);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // starts the detail activity when the poster is clicked
                    Log.v(TAG, movie.getTitle());
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.MOVIE_ARG, movie);
                    context.startActivity(intent);
                }
            });
        }

        return imageView;
    }

    // array of hardcoded images
    private Integer[] mImageIds = {
            R.drawable.not_available, R.drawable.zoom_loading
    };
}
