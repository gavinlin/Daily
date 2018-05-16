package com.gavincode.bujo.presentation.ui.widget

import android.content.Context
import androidx.core.widget.NestedScrollView
import android.util.AttributeSet

class NonFocusingScrollView: NestedScrollView{
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
            :super(context, attrs, defStyle)

//    override fun onRequestFocusInDescendants(direction: Int, previouslyFocusedRect: Rect?): Boolean {
//        return true
//    }
}