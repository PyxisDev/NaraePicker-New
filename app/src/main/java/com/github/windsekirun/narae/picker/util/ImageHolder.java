package com.github.windsekirun.narae.picker.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.github.windsekirun.narae.picker.R;

public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public ImageView check;

        public ImageHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            check = (ImageView) itemView.findViewById(R.id.img_check);
        }
    }