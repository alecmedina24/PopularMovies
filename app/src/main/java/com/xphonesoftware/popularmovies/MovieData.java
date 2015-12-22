package com.xphonesoftware.popularmovies;

import com.xphonesoftware.popularmovies.models.Movie;

import java.util.ArrayList;

/**
 * Used to maintain global state
 * Created by alecmedina on 12/21/15.
 */
public class MovieData {
    private static MovieData ourInstance = new MovieData();
    private ArrayList<Movie> movies;
    private int page = 0;

    public static MovieData getInstance() {
        return ourInstance;
    }

    private MovieData() {
        movies = new ArrayList<>();
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int nextPage() {
        ++page;
        return page;
    }
}
