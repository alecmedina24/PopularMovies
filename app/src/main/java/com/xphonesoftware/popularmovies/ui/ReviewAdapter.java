package com.xphonesoftware.popularmovies.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xphonesoftware.popularmovies.models.Review;
import com.xphonesoftware.popularmovies.models.ReviewResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alecmedina on 12/10/15.
 */
public class ReviewAdapter extends BaseAdapter {

    private Context context;
    private MovieDetailFragment movieDetailFragment;
    private List<Review> reviewResults;

    public ReviewAdapter(Context context, MovieDetailFragment movieDetailFragment) {
        this.context = context;
        this.movieDetailFragment = movieDetailFragment;
        reviewResults = new ArrayList<>();
    }

    public void updateReviews(ReviewResults reviewResults) {
        this.reviewResults.addAll(reviewResults.getResults());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return reviewResults.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(((Review) getItem(position)).getContent());

        return textView;
    }
}
