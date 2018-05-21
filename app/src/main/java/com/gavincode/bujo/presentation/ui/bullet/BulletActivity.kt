package com.gavincode.bujo.presentation.ui.bullet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.ui.Navigator
import com.gavincode.bujo.presentation.ui.widget.ElasticDragDismissFrameLayout
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_bullet.*
import javax.inject.Inject

/**
 * Created by gavinlin on 20/3/18.
 */

class BulletActivity: AppCompatActivity(), HasSupportFragmentInjector{

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    var systemChromeFader: ElasticDragDismissFrameLayout.SystemChromeFader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bullet)

        setUpActionBar()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.bullet_container, BulletFragment.newInstance(intent.getStringExtra(Navigator.ARG_BULLET_ID) ?: "",
                        intent.getLongExtra(Navigator.ARG_DATE_LONG, 0)))
                .commit()
        draggable_frame.addListener(object: ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            override fun onDragDismissed() {
//                if (draggable_frame.translationY > 0) {
//                    getWindow().setReturnTransition(
//                            TransitionInflater.from(this@BulletActivity)
//                                    .inflateTransition(R.transition.bullet_return_downward))
//                }
//                finishAfterTransition()
            }
        })
        systemChromeFader = ElasticDragDismissFrameLayout.SystemChromeFader(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(findViewById(R.id.bullet_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onBackPressed() {
        (supportFragmentManager
                .findFragmentById(R.id.bullet_container) as BulletFragment?)
                ?.handleBack() ?: super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        draggable_frame.addListener(systemChromeFader)
    }

    override fun onPause() {
        super.onPause()
        draggable_frame.removeListener(systemChromeFader)
    }
}