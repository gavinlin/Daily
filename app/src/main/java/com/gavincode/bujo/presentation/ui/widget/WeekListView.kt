package com.gavincode.bujo.presentation.ui.widget

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.gavincode.bujo.presentation.util.CalendarBus
import timber.log.Timber

/**
 * Created by gavinlin on 14/3/18.
 */

class WeekListView: androidx.recyclerview.widget.RecyclerView {

    var mUserScrolling: Boolean = false
    var mScrolling: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet)
        : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
        : super(context, attrs, defStyle)

    fun setSnapEnabled(enabled: Boolean) {
        if (enabled) {
            addOnScrollListener(mScrollListener)
        } else {
            removeOnScrollListener(mScrollListener)
        }
    }

    // endregion

    // region Private methods

    private val mScrollListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val weeksAdapter = adapter as WeeksAdapter

            when (newState) {
                androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE -> {
                    Timber.i("scroll state idle")
                    if (mUserScrolling) {
                        scrollToView(getCenterView())
                        postDelayed({ weeksAdapter.dragging = false }, 700) // Wait for recyclerView to settle
                    }

                    mUserScrolling = false
                    mScrolling = false
                }
            // If scroll is caused by a touch (scroll touch, not any touch)
                androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING -> {
//                    BusProvider.getInstance().send(Events.CalendarScrolledEvent())
                    CalendarBus.send(CalendarEvent.CalendarScrollEvent())
                    // If scroll was initiated already, this is not a user scrolling, but probably a tap, else set userScrolling
                    if (!mScrolling) {
                        mUserScrolling = true
                    }

                    weeksAdapter.dragging = true
                    Timber.i("scroll state  dragging")
                }
                androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING -> {
                    // The user's finger is not touching the list anymore, no need
                    // for any alpha animation then
                    weeksAdapter.alphaSet = true
                    mScrolling = true
                    Timber.i("scroll state settling")
                }
            }
        }
    }

    private fun getChildClosestToPosition(y: Int): View? {
        if (childCount <= 0) {
            return null
        }

        val itemHeight = getChildAt(0).measuredHeight

        var closestY = 9999
        var closestChild: View? = null

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            val childCenterY = child.y.toInt() + itemHeight / 2
            val yDistance = childCenterY - y

            // If child center is closer than previous closest, set it as closest
            if (Math.abs(yDistance) < Math.abs(closestY)) {
                closestY = yDistance
                closestChild = child
            }
        }

        return closestChild
    }

    private fun getCenterView(): View? {
        return getChildClosestToPosition(measuredHeight / 2)
    }

    private fun scrollToView(child: View?) {
        if (child == null) {
            return
        }

        stopScroll()

        val scrollDistance = getScrollDistance(child)

        if (scrollDistance != 0) {
            smoothScrollBy(0, scrollDistance)
        }
    }

    private fun getScrollDistance(child: View): Int {
        val itemHeight = getChildAt(0).measuredHeight
        val centerY = measuredHeight / 2

        val childCenterY = child.y.toInt() + itemHeight / 2

        return childCenterY - centerY
    }
}