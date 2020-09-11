package com.example.android.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    private ImageView mThumbnail;
    private TextView mTitle;
    private TextView mVoteAverage;
    private TextView mReleaseDate;
    private TextView mOverview;
    final String BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String VIDEO_KEY = "VIDEO_KEY";
    private String key = null;
    private int index;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.movie_detail);
        String relativePath = new String();
        String fullPath = new String();
        Intent intent = getIntent();
        index = intent.getIntExtra("intIndex", 0);
        Log.d("WWD", "in detail activity index is " + index);
        mThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mOverview = (TextView) findViewById(R.id.tv_overview);

        mTitle.setText(JsonUtil.getTitle(index));
        mVoteAverage.setText(JsonUtil.getPopularity(index));
        mReleaseDate.setText(JsonUtil.getReleaseDate(index));
        mOverview.setText(JsonUtil.getOverview(index));

        relativePath = JsonUtil.getPosterPath(index);
        Log.d("WWD", "in detail rel path is " + relativePath);

        fullPath = BASE_URL + relativePath;
        Log.d("WWD", "full path is " + fullPath);
        Picasso.get().load(fullPath).into(mThumbnail);

    }

    public void playTrailer(View view) {
        URL fetchMovieDetailsUrl;
        Log.d("WWD", " *******************  in playTrailer **************");
        Log.d("WWD", "index is " + index);
        String ID = JsonUtil.getID(index);
        Log.d("WWD", "ID is " + ID);
        fetchMovieDetailsUrl = NetworkUtils.buildGetVideoUrl(JsonUtil.getID(index));
        Log.d("WWD", " ************** the details URL is  ***********" + fetchMovieDetailsUrl);
        Log.d("WWD", "got further this time");
        new DetailActivity.MovieDetailTask().execute(fetchMovieDetailsUrl);
    }
    public class MovieDetailTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("WWD", "in MovieDetailTask onPreExecute");
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieResults = null;
            Log.d("WWD", "================================== in MovieDetailTask doInBackground   ===========================");
            try {
                movieResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieResults;
        }


        @Override
        protected void onPostExecute(String movieSearchResults) {
            Log.d("WWD", "============================== in onPostExecute for details =======================================");
            Log.d("WWD", "detail results" + movieSearchResults);
            if (NetworkUtils.getNetworkConnected()) {
                if (movieSearchResults != null && !movieSearchResults.equals("")) {
                    Log.d("WWD", "got movie results");
                     key = JsonUtil.parseDetailJson(movieSearchResults);
                     if (key != null) {
                         Intent intent = new Intent(context, WebviewActivity.class);
                         intent.putExtra(VIDEO_KEY, key);
                         startActivity(intent);
                     } else {
                         Log.d("WWD", "no trailer available");
                     }
                }
            } else {
                Log.d("WWD", "network error");
            }
        }

    }
}
