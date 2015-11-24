package com.xphonesoftware.popularmovies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alecmedina on 11/8/15.
 */
public class DiscoveredMovies implements Serializable {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Movie> Movies = new ArrayList<Movie>();
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalMovies;

    /**
     *
     * @return
     * The page
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    public DiscoveredMovies withPage(int page) {
        this.page = page;
        return this;
    }

    /**
     *
     * @return
     * The Movies
     */
    public List<Movie> getMovies() {
        return Movies;
    }

    /**
     *
     * @param Movies
     * The Movies
     */
    public void setMovies(List<Movie> Movies) {
        this.Movies = Movies;
    }

    public DiscoveredMovies withMovies(List<Movie> Movies) {
        this.Movies = Movies;
        return this;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public DiscoveredMovies withTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    /**
     *
     * @return
     * The totalMovies
     */
    public int getTotalMovies() {
        return totalMovies;
    }

    /**
     *
     * @param totalMovies
     * The total_Movies
     */
    public void setTotalMovies(int totalMovies) {
        this.totalMovies = totalMovies;
    }

    public DiscoveredMovies withTotalMovies(int totalMovies) {
        this.totalMovies = totalMovies;
        return this;
    }
}

