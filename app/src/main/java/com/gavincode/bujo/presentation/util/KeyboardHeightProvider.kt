package com.gavincode.bujo.presentation.util

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.gavincode.bujo.R


class KeyboardHeightProvider(private val activity: Activity): PopupWindow() {

    val popupView: View
    val parentView: View

    var observer: KeyboardHeightObserver? = null

    init {
        val inflater = activity.layoutInflater
        popupView = inflater.inflate(R.layout.keyboard_height_popup, null, false)
        contentView = popupView

        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED

        parentView = activity.findViewById(android.R.id.content)

        width = 0
        height = WindowManager.LayoutParams.MATCH_PARENT

        popupView.viewTreeObserver.addOnGlobalLayoutListener {
            handleOnGlobalLayout()
        }
    }

    private fun handleOnGlobalLayout() {
        val screenSize = Point()
        activity.windowManager.defaultDisplay.getSize(screenSize)

        val rect = Rect()
        popupView.getWindowVisibleDisplayFrame(rect)

        val keyboardHeight = screenSize.y - rect.bottom

        observer?.onKeyboardHeightChanged(keyboardHeight)
    }

    fun start() {
        if (!isShowing && parentView.windowToken != null) {
            setBackgroundDrawable(ColorDrawable(0))
            showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0)
        }
    }

    fun close() {
        dismiss()
    }

}

interface KeyboardHeightObserver {
    fun onKeyboardHeightChanged(height: Int)
}