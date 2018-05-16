package com.gavincode.bujo.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.ui.main.MainActivity
import timber.log.Timber

/**
 * Created by gavinlin on 25/2/18.
 */

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timber.d("User has been login")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}