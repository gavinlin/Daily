package com.gavincode.bujo.presentation.ui.bullet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gavincode.bujo.R

/**
 * Created by gavinlin on 20/3/18.
 */

class BulletActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bullet)

        setUpActionBar()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.bullet_container, BulletFragment.newInstance())
                .commit()
    }

    private fun setUpActionBar() {
        setSupportActionBar(findViewById(R.id.bullet_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}