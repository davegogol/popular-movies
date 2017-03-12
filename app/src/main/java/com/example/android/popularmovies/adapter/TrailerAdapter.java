package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.domain.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>  {

    private List<Trailer> mTrailersList;

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        String name = mTrailersList.get(position).getName();
        holder.mTrailerTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailersList) return 0;
        return mTrailersList.size();
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView mTrailerTextView;
        TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerTextView = (TextView) view.findViewById(R.id.trailer_data);
        }
    }

    public void setTrailersData(List<Trailer> trailerList) {
        mTrailersList = trailerList;
        notifyDataSetChanged();
    }
}
