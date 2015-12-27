package com.axay.movies.ui.fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.axay.movies.BuildConfig;
import com.axay.movies.R;
import com.axay.movies.data.Movie;
import com.axay.movies.ui.adapter.MoviesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author akshay
 * @since 27/12/15
 */
public class MoviesFragment extends Fragment {

    public static final String TAG = MoviesFragment.class.getSimpleName();

    private MoviesAdapter mMoviesAdapter;

    private RecyclerView mRecyclerView;

    private ArrayList<Movie> moviesData = new ArrayList<>();

    //For pagination
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private int pageNumber = 1;
    private String filter = null;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(int position);
    }

    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreateView");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        mMoviesAdapter = new MoviesAdapter(getActivity(), moviesData);

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movies_grid);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMoviesAdapter);

        sendRequest(filter, String.valueOf(pageNumber));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
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

    private void sendRequest(String filter, String pageNumber) {
        new FetchMoviesTask().execute(filter, pageNumber);
    }


    private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        final String KEY_RESULTS = "results";

        final String KEY_TITLE = "title";
        final String KEY_POSTER_PATH = "poster_path";
        final String KEY_OVERVIEW = "overview";
        final String KEY_BACKDROP_PATH = "backdrop_path";
        final String KEY_VOTE_AVERAgE = "vote_average";

        JSONObject response = new JSONObject(movieJsonStr);
        JSONArray moviesArray = response.getJSONArray(KEY_RESULTS);

        ArrayList<Movie> movieArrayList = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject jsonObject = moviesArray.getJSONObject(i);

            String title = jsonObject.getString(KEY_TITLE);
            String posterPath = jsonObject.getString(KEY_POSTER_PATH);
            String overview = jsonObject.getString(KEY_OVERVIEW);
            String backdropPath = jsonObject.getString(KEY_BACKDROP_PATH);
            String voteAverage = jsonObject.getString(KEY_VOTE_AVERAgE);

            Movie movie = new Movie(title, posterPath, overview, backdropPath, voteAverage);
            movieArrayList.add(movie);
        }

        return movieArrayList;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            boolean filter = false;
            if (!(params[0] == null)) {
                filter = true;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr;

            try {

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String PARAM_API_KEY = "api_key";
                final String PARAM_SORT_BY = "sort_by";
                final String PARAM_PAGE = "page";

                Uri.Builder uriBuilder = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(PARAM_API_KEY, BuildConfig.TMDB_API_KEY)
                        .appendQueryParameter(PARAM_PAGE, String.valueOf(pageNumber));

                if (filter) {
                    uriBuilder.appendQueryParameter(PARAM_SORT_BY, params[0]);
                }

                Uri builtUri = uriBuilder.build();

                URL url = new URL(builtUri.toString());

                Log.d(TAG, "Built Uri " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                moviesJsonStr = builder.toString();

                Log.d(TAG, "Response " + moviesJsonStr);

            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            moviesData.addAll(movies);
            mMoviesAdapter.notifyItemRangeInserted(moviesData.size(), movies.size());
            loading = true;
        }
    }
}