package com.axay.movies.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.axay.movies.BuildConfig;
import com.axay.movies.R;
import com.axay.movies.commons.BaseFragment;
import com.axay.movies.data.api.TmdbApi;
import com.axay.movies.data.api.model.DiscoverMovieResponse;
import com.axay.movies.data.api.model.Movie;
import com.axay.movies.ui.adapter.MoviesAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author akshay
 * @since 27/12/15
 */
public class MoviesFragment extends BaseFragment {

    private MoviesAdapter mMoviesAdapter;

    private RecyclerView mRecyclerView;

    private ArrayList<Movie> moviesData = new ArrayList<>();

    //For pagination
    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    private int pageNumber = 1;

    private final String FILTER_POPULARITY = "popularity.desc";
    private final String FILTER_RATING = "vote_average.desc";

    private String filter = FILTER_POPULARITY; //default value of filter

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

    private void applyFilter(String filter) {
        moviesData.clear();
        mMoviesAdapter.notifyDataSetChanged();

        /*if (!fetchMoviesTask.isCancelled()) {
            fetchMoviesTask.cancel(true);
        }*/

        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        pageNumber = 1;
        this.filter = filter;
        sendRequest(filter, String.valueOf(pageNumber));
    }

    private void sendRequest(String filter, String pageNumber) {

        Timber.i("Sending Request");

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