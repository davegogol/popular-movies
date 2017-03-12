package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.domain.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>  {

    private List<Review> mReviewsList;

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        String content = mReviewsList.get(position).getContent();
        String author = mReviewsList.get(position).getAuthor();
        holder.mReviewContentTextView.setText(content);
        holder.mReviewAuthorTextView.setText(author);
    }

    @Override
    public int getItemCount() {
        if (null == mReviewsList) return 0;
        return mReviewsList.size();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView mReviewContentTextView;
        final TextView mReviewAuthorTextView;

        ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewContentTextView = (TextView) view.findViewById(R.id.review_content);
            mReviewAuthorTextView = (TextView) view.findViewById(R.id.review_author);
        }
    }

    public void setReviewsListData(List<Review> trailerList) {
        mReviewsList = trailerList;
        notifyDataSetChanged();
    }
}