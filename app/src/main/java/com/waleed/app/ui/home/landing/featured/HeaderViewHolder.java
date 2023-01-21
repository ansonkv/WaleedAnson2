package com.waleed.app.ui.home.landing.featured;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waleed.app.R;


final class HeaderViewHolder extends RecyclerView.ViewHolder {

    final TextView tvTitle;

    HeaderViewHolder(@NonNull final View view) {
        super(view);

        tvTitle = view.findViewById(R.id.tvFeaturedTitle);
    }
}
