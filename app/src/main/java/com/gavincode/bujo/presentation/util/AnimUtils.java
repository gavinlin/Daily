package com.gavincode.bujo.presentation.util;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class AnimUtils {

    private AnimUtils () {}

    private static Interpolator fastOutSlowIn;

    public static Interpolator getFastOutSlowInInterpolator(Context context) {
        if (fastOutSlowIn == null) {
            fastOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_slow_in);
        }
        return fastOutSlowIn;
    }
}
