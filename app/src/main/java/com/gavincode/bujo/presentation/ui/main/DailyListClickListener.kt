package com.gavincode.bujo.presentation.ui.main

import android.view.View

interface DailyListClickListener {
    fun onClick(view: View, bulletId: String)
}