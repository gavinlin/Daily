package com.gavincode.bujo.presentation.util;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;

public class ColorUtils {
    public static @CheckResult @ColorInt int modifyAlpha(@ColorInt int color,
                                                         @IntRange(from = 0, to = 255) int alpha) {
        return (color & 0x00ffffff) | (alpha << 24);
    }

    /**
     * Set the alpha component of {@code color} to be {@code alpha}.
     */
    public static @CheckResult
    @ColorInt int modifyAlpha(@ColorInt int color,
                              @FloatRange(from = 0f, to = 1f) float alpha) {
        return modifyAlpha(color, (int) (255f * alpha));
    }
}
