package com.axay.movies.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author akshay
 * @since 27/12/15
 */
public class Movie implements Parcelable {

    private String id;

    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    private String overview;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private String voteAverage;

    @SerializedName("release_date")
    private String releaseDate;

    private String originalTitle;

    private String popularity;

    @SerializedName("vote_count")
    private String voteCount;

    private boolean video;

    private boolean adult;

    public Movie(String title, String posterPath, String overview,
                 String backdropPath, String voteAverage, String releaseDate, String originalTitle) {
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
    }

    public Movie(Parcel source) {
        this.title = source.readString();
        this.posterPath = source.readString();
        this.overview = source.readString();
        this.backdropPath = source.readString();
        this.voteAverage = source.readString();
        this.releaseDate = source.readString();
        this.originalTitle = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(backdropPath);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
    }

    static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }
}