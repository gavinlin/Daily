package com.gavincode.bujo.presentation.ui.widget

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by gavinlin on 17/3/18.
 */

class ScrollingCalendarBehavior(context: Context, attributeSet: AttributeSet)
    : AppBarLayout.Behavior(context, attributeSet) {
    override fun onInterceptTouchEvent(parent: CoordinatorLayout?, child: AppBarLayout?, ev: MotionEvent?): Boolean {
        return false
    }
}