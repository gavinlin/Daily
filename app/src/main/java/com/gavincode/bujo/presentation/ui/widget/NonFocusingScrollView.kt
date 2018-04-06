package com.gavincode.bujo.presentation.ui.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ScrollView

class NonFocusingScrollView: ScrollView {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
            :super(context, attrs, defStyle)

    override fun onRequestFocusInDescendants(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        return true
    }
}