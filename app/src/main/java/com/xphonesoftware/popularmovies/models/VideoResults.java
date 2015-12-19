package com.xphonesoftware.popularmovies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VideoResults {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<VideoResultDetails> results = new ArrayList<VideoResultDetails>();

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The results
     */
    public List<VideoResultDetails> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<VideoResultDetails> results) {
        this.results = results;
    }

}