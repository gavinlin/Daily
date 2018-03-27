package com.gavincode.bujo.presentation.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.gavincode.bujo.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by gavinlin on 26/2/18.
 */

class MainActivity: AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = DailyPlanFragment.getInstance()
        val toolbar: Toolbar = this.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit()
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }
}