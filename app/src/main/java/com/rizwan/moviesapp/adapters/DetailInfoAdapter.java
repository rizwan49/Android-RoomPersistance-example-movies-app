package com.rizwan.moviesapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rizwan.moviesapp.R;
import com.rizwan.moviesapp.apis.model.detail.review.ReviewFieldModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final DetailInfoAdapter.ListItemOnClickListener mOnClickListener;
    private static final int REVIEW_POS = 2;
    private static final int VIDEO_POS = 1;
    private static final int HEADER = 0;
    List<ReviewFieldModel> list;

    public DetailInfoAdapter(List<ReviewFieldModel> list, ListItemOnClickListener listener) {
        mOnClickListener = listener;
        if (list == null) return;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case HEADER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_layout, parent, false);

                return new HeaderHolder(itemView);
            case VIDEO_POS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_item_layout, parent, false);

                return new VideoHolder(itemView);

            case REVIEW_POS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.review_item_layout, parent, false);

                return new ReviewHolder(itemView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        if (!TextUtils.isEmpty(list.get(position).getName()))
            return VIDEO_POS;
        if (!TextUtils.isEmpty(list.get(position).getContent()))
            return REVIEW_POS;
        return HEADER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER:
                ((HeaderHolder) holder).bindHeaderView(position);
                break;
            case VIDEO_POS:
                ((VideoHolder) holder).bindVideoView(position);
                break;
            case REVIEW_POS:
                ((ReviewHolder) holder).bindReviewView(position);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0)
            return 0;
        return list.size();
    }

    public void addAll(List<ReviewFieldModel> results) {
        if (results == null) return;

        if (list == null)
            list = new ArrayList<>();
        else
            list.clear();

        list.addAll(results);
        notifyDataSetChanged();
    }


    class ReviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reviewTv)
        TextView reviewTv;

        private ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        private void bindReviewView(int position) {
            reviewTv.setText(list.get(position).getContent());
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.videoTitle)
        TextView videoTitle;

        private VideoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        private void bindVideoView(int position) {
            videoTitle.setText(list.get(position).getName());
        }

        @Override
        public void onClick(View view) {
            if (mOnClickListener != null)
                mOnClickListener.onListItemClick(list.get(getAdapterPosition()));
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.header)
        TextView headerTv;

        private HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindHeaderView(int position) {
            try {
                if (list.get(position + 1) != null)
                    headerTv.setText(list.get(position).getType());
            } catch (Exception ee) {
                headerTv.setVisibility(View.GONE);
            }
        }
    }


    public interface ListItemOnClickListener {
        void onListItemClick(ReviewFieldModel selectedObject);
    }

}
