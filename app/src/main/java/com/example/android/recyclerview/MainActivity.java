/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.recyclerview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

// COMPLETED (8) Implement GreenAdapter.ListItemClickListener from the MainActivity
public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 20;

    /*
     * References to RecyclerView and Adapter to reset the list to its
     * "pretty" state when the reset menu item is clicked.
     */
    private MovieAdapter mAdapter;
    private RecyclerView mMoviesList;
    private TextView mErrorMessage;

    enum SearchType {
        PopularMovies,
        TopRatedMovies
    }

    // COMPLETED (9) Create a Toast variable called mToast to store the current Toast
    /*
     * If we hold a reference to our Toast, we can cancel it (if it's showing)
     * to display a new Toast. If we didn't do this, Toasts would be delayed
     * in showing up if you clicked many list items in quick succession.
     */
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeMovieSearchQuery(SearchType.PopularMovies);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);


        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(NUM_LIST_ITEMS, this);
        mMoviesList.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Log.d("WWD", "In onOptionsItemSelected");
        switch (itemId) {
            case R.id.action_popular:
                makeMovieSearchQuery(SearchType.PopularMovies);
                return true;
            case R.id.action_top_rated:
                makeMovieSearchQuery(SearchType.TopRatedMovies);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // COMPLETED (10) Override ListItemClickListener's onListItemClick method
    /**
     * This is where we receive our callback from
     * {@link MovieAdapter.ListItemClickListener}
     *
     * This callback is invoked when you click on an item in the list.
     *
     * @param clickedItemIndex Index in the list of the item that was clicked.
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        /* if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show(); */
        if (NetworkUtils.getNetworkConnected()) {
            Intent myIntent = new Intent(this, DetailActivity.class);
            myIntent.putExtra("intIndex", clickedItemIndex);
            Log.d("WWD", "starting detail activity");
            startActivity(myIntent);
        }
    }

    private void makeMovieSearchQuery(SearchType type) {
        Log.d("WWD", "in MovieSearchQuery");
        URL fetchMovieUrl;
        if (type == SearchType.PopularMovies)
           fetchMovieUrl = NetworkUtils.buildPopularUrl();
        else
            fetchMovieUrl = NetworkUtils.buildTopRatedUrl();
        new MovieQueryTask().execute(fetchMovieUrl);
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieResults = null;
            Log.d("WWD", "in doInBackground");
            try {
                movieResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieResults;
        }

        public void showRecyclerView() {
            mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
            mMoviesList.setVisibility(View.VISIBLE);

            mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
            mErrorMessage.setVisibility(View.INVISIBLE);
        }

        public void showErrorMessage() {
            mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
            mMoviesList.setVisibility(View.INVISIBLE);

            mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
            mErrorMessage.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String movieSearchResults) {
            Log.d("WWD", "in onPostExecute");
            if (NetworkUtils.getNetworkConnected()) {
                showRecyclerView();
                if (movieSearchResults != null && !movieSearchResults.equals("")) {
                    Log.d("WWD", "got movie results");
                    JsonUtil.parseMovieJson(movieSearchResults);
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                showErrorMessage();
            }
        }

    }

}
