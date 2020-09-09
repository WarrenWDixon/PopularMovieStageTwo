package com.example.android.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {
    private ImageView mThumbnail;
    private TextView mTitle;
    private TextView mVoteAverage;
    private TextView mReleaseDate;
    private TextView mOverview;
    final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        String relativePath = new String();
        String fullPath = new String();
        Intent intent = getIntent();
        int index = intent.getIntExtra("intIndex", 0);
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
}
