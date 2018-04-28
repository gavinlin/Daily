package com.gavincode.bujo.presentation.ui.bullet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.ui.Navigator
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import xyz.klinker.android.drag_dismiss.activity.DragDismissActivity
import javax.inject.Inject

/**
 * Created by gavinlin on 20/3/18.
 */

class BulletActivity: DragDismissActivity(), HasSupportFragmentInjector{

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreateContent(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
        AndroidInjection.inject(this)
        val view = inflater.inflate(R.layout.activity_bullet, parent, false)
        setUpActionBar(view)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.bullet_container, BulletFragment.newInstance(intent.getStringExtra(Navigator.ARG_BULLET_ID) ?: "",
                        intent.getLongExtra(Navigator.ARG_DATE_LONG, 0)))
                .commit()
        return view
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bullet)
//
//        setUpActionBar()
//        supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.bullet_container, BulletFragment.newInstance(intent.getStringExtra(Navigator.ARG_BULLET_ID) ?: "",
//                        intent.getLongExtra(Navigator.ARG_DATE_LONG, 0)))
//                .commit()
//    }

    private fun setUpActionBar(view: View) {
        setSupportActionBar(view.findViewById(R.id.bullet_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onBackPressed() {
        (supportFragmentManager
                .findFragmentById(R.id.bullet_container) as BulletFragment?)
                ?.handleBack() ?: super.onBackPressed()
    }
}