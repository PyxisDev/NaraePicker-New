package com.github.windsekirun.narae.picker.util;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Palette-Twitter-Android-New
 * FrescoRoundUtils
 * Created by WindSekirun on 2016. 6. 1..
 */
public class FrescoUtils {

    public static GenericDraweeHierarchy setRounding(boolean isCircleUseAvater, SimpleDraweeView imageView) {
        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();

        RoundingParams roundingParams = hierarchy.getRoundingParams();
        if (roundingParams == null)
            roundingParams = new RoundingParams();

        roundingParams.setCornersRadius(10);
        roundingParams.setRoundAsCircle(isCircleUseAvater);
        hierarchy.setRoundingParams(roundingParams);

        return hierarchy;
    }

    public static GenericDraweeHierarchy forHeader(SimpleDraweeView imageView) {
        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
        ColorFilter filter = new PorterDuffColorFilter(0x87000000, PorterDuff.Mode.DARKEN);

        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setActualImageColorFilter(filter);
        return hierarchy;
    }

    public static GenericDraweeHierarchy setCenterCrop(SimpleDraweeView imageView) {
        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();

        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        return hierarchy;
    }
}
