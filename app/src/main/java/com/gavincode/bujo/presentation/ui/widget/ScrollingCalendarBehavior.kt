package com.gavincode.bujo.presentation.ui.widget

import android.content.Context
import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by gavinlin on 17/3/18.
 */

class ScrollingCalendarBehavior(context: Context, attributeSet: AttributeSet)
    : com.google.android.material.appbar.AppBarLayout.Behavior(context, attributeSet) {

    override fun onInterceptTouchEvent(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: com.google.android.material.appbar.AppBarLayout, ev: MotionEvent): Boolean {
      return false
    }
}