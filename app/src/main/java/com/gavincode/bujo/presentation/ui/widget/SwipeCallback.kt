package com.gavincode.bujo.presentation.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeCallback(context: Context):
        ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT ) {

    private val backgroundColor = Color.parseColor("#f44336")
    private val background = ColorDrawable()

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top

            background.color = backgroundColor
            background.setBounds(itemView.right + dX.toInt(),
                    itemView.top, itemView.right, itemView.bottom)
            background.draw(c)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}