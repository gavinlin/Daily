package com.gavincode.bujo.presentation.ui.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
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

    @BindView(R.id.bottom_navigation)
    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_calendar -> {
                    if (!(supportFragmentManager.findFragmentById(R.id.main_container) is DailyPlanFragment)) {
                        val fragment = DailyPlanFragment.getInstance()
                        supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.main_container, fragment)
                                .commit()
                        return@setOnNavigationItemSelectedListener true
                    }
                }
            }
            false
        }
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }
}