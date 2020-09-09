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

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * We couldn't come up with a good name for this class. Then, we realized
 * that this lesson is about RecyclerView.
 *
 * RecyclerView... Recycling... Saving the planet? Being green? Anyone?
 * #crickets
 *
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 *
 * If you don't like our puns, we named this Adapter GreenAdapter because its
 * contents are green.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ImageViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();


    final private ListItemClickListener mOnClickListener;


    private static int viewHolderCount;
    private int mNumberItems;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public MovieAdapter(int numberOfItems, ListItemClickListener listener) {
        mNumberItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.image_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ImageViewHolder viewHolder = new ImageViewHolder(view);

        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Log.d("WWD", "#" + position);
        holder.bind(position);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    class ImageViewHolder extends RecyclerView.ViewHolder
        implements OnClickListener {

        ImageView mImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            Log.d("WWD", "in bind listIndex is " + listIndex);
            final String BASE_URL = "http://image.tmdb.org/t/p/w185";
            String relativePath = new String();
            String fullPath     = new String();
            if (NetworkUtils.getNetworkConnected()) {
                if (JsonUtil.getDataRead()) {
                    relativePath = JsonUtil.getPosterPath(listIndex);
                    Log.d("WWD", "in bind rel path is " + relativePath);

                    fullPath = BASE_URL + relativePath;
                    Picasso.get().load(fullPath).into(mImageView);
                }
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
