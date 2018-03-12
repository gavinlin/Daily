package com.gavincode.bujo.presentation.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gavincode.bujo.R

/**
 * Created by gavinlin on 26/2/18.
 */

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = DailyPlanFragment.getInstance()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit()
    }
}