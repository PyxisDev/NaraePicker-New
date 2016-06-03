package com.github.windsekirun.narae.picker.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.windsekirun.narae.picker.R;

public class ImageHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView thumbnail;
        public ImageView check;

        public ImageHolder(View itemView) {
            super(itemView);
            thumbnail = (SimpleDraweeView) itemView.findViewById(R.id.img_thumbnail);
            check = (ImageView) itemView.findViewById(R.id.img_check);
        }
    }