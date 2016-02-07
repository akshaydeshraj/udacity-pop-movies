package com.axay.movies.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.axay.movies.BuildConfig;
import com.axay.movies.R;
import com.axay.movies.commons.BaseFragment;
import com.axay.movies.data.api.TmdbApi;
import com.axay.movies.data.api.model.DiscoverMovieResponse;
import com.axay.movies.data.api.model.Movie;
import com.axay.movies.data.provider.MovieContract;
import com.axay.movies.ui.adapter.MoviesAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_ADULT;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_BACKDROP;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_OVERVIEW;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_POPULARITY;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_POSTER;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_RELEASE_DATE;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_TITLE;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_VIDEO;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE;
import static com.axay.movies.data.provider.MovieContract.MovieEntry.COLUMN_VOTE_COUNT;

/**
 * @author akshay
 * @since 27/12/15
 */
public class MoviesFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MoviesAdapter mMoviesAdapter;

    private RecyclerView mRecyclerView;

    private ArrayList<Movie> moviesData = new ArrayList<>();
    private List<Movie> favouritesMoviesList = new ArrayList<>();

    //For pagination
    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    private int pageNumber = 1;

    private final String FILTER_POPULARITY = "popularity.desc";
    private final String FILTER_RATING = "vote_average.desc";

    private String filter = FILTER_POPULARITY; //default value of filter

    private static final int CURSOR_LOADER_ID = 0;

    @Inject
    TmdbApi tmdbApi;

    HashMap<String, String> queryMap = new HashMap<>();

    //Query params for /discover endpoint
    final String PARAM_API_KEY = "api_key";
    final String PARAM_SORT_BY = "sort_by";
    final String PARAM_PAGE = "page";

    Subscription subscription;

    public interface OnMovieSelectedListener {
        void onItemSelected(Movie movie);
    }

    OnMovieSelectedListener mCallback;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Cursor c =
                getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        new String[]{MovieContract.MovieEntry._ID},
                        null,
                        null,
                        null);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mMoviesAdapter = new MoviesAdapter(getActivity(), moviesData, this);

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movies_grid);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMoviesAdapter);

        queryMap.put(PARAM_API_KEY, BuildConfig.TMDB_API_KEY);

        sendRequest(filter, String.valueOf(pageNumber));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false;
                            ++pageNumber;
                            sendRequest(filter, String.valueOf(pageNumber));
                        }
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    protected void setupComponent() {
        initialiseComponent().inject(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort_popularity:
                applyFilter(FILTER_POPULARITY);
                break;
            case R.id.action_sort_rating:
                applyFilter(FILTER_RATING);
                break;
            case R.id.action_favourites:
                showFavourites();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnMovieSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "No Favourites", Toast.LENGTH_SHORT).show();
        } else {
            for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(data.getString(data.getColumnIndex(COLUMN_MOVIE_ID)));
                movie.setTitle(data.getString(data.getColumnIndex(COLUMN_TITLE)));
                movie.setPosterPath(data.getString(data.getColumnIndex(COLUMN_POSTER)));
                movie.setOverview(data.getString(data.getColumnIndex(COLUMN_OVERVIEW)));
                movie.setBackdropPath(data.getString(data.getColumnIndex(COLUMN_BACKDROP)));
                movie.setVoteAverage(data.getString(data.getColumnIndex(COLUMN_VOTE_AVERAGE)));
                movie.setReleaseDate(data.getString(data.getColumnIndex(COLUMN_RELEASE_DATE)));
                movie.setPopularity(data.getString(data.getColumnIndex(COLUMN_POPULARITY)));
                movie.setVoteCount(data.getString(data.getColumnIndex(COLUMN_VOTE_COUNT)));
                movie.setVideo(Boolean.parseBoolean(data.getString(data.getColumnIndex(COLUMN_VIDEO))));
                movie.setAdult(Boolean.parseBoolean(data.getString(data.getColumnIndex(COLUMN_ADULT))));

                favouritesMoviesList.add(movie);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Do Nothing
    }

    private void applyFilter(String filter) {
        moviesData.clear();
        mMoviesAdapter.notifyDataSetChanged();

        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        pageNumber = 1;
        this.filter = filter;
        sendRequest(filter, String.valueOf(pageNumber));
    }

    private void showFavourites() {
        mMoviesAdapter.clearAdapter();

        moviesData.clear();
        moviesData.addAll(favouritesMoviesList);
        mMoviesAdapter.notifyItemRangeInserted(moviesData.size(),
                favouritesMoviesList.size());
    }

    private void sendRequest(String filter, String pageNumber) {

        queryMap.put(PARAM_PAGE, pageNumber);
        queryMap.put(PARAM_SORT_BY, filter);

        subscription = tmdbApi.discoverMovies(queryMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DiscoverMovieResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("Request Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(DiscoverMovieResponse discoverMovieResponse) {
                        moviesData.addAll(discoverMovieResponse.getResults());
                        mMoviesAdapter.notifyItemRangeInserted(moviesData.size(),
                                discoverMovieResponse.getResults().size());
                        loading = true;
                    }
                });
    }

    public void itemClicked(int position) {
        mCallback.onItemSelected(moviesData.get(position));
    }
}