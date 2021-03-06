package com.gavincode.bujo.presentation.ui.widget

import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.KeyEvent


class BulletEditText: AppCompatEditText {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
            :super(context, attrs, defStyle)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        event?.run {
            if (keyCode == KeyEvent.KEYCODE_BACK && action == KeyEvent.ACTION_UP) {
                clearFocus()
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }
}