package com.gavincode.bujo.presentation.ui.bullet

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.gavincode.bujo.R
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.presentation.ui.Navigator
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_bullet.*
import kotlinx.android.synthetic.main.view_bullet_content.*
import javax.inject.Inject

/**
 * Created by gavinlin on 20/3/18.
 */

class BulletFragment: Fragment() {

    companion object {

        fun newInstance(bulletId: String, date: Long): Fragment {
            val bundle = Bundle()
            bundle.putString(Navigator.ARG_BULLET_ID, bulletId)
            bundle.putLong(Navigator.ARG_DATE_LONG, date)
            val fragment = BulletFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bulletViewModel: BulletViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bullet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bulletViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BulletViewModel::class.java)
        bindViewModel()
        prepareView()
    }

    private fun prepareView() {
        bullet_scroll_view.apply {
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS

            setOnTouchListener { v, _->
                v.requestFocusFromTouch()
                false
            }
        }
    }

    private fun setContentViewTouchDelegate() {
        bullet_content_parent_layout.post {
            val actualArea = Rect()
            bullet_content_parent_layout.getHitRect(actualArea)
            val touchDelegate = TouchDelegate(actualArea, bullet_content_edit_text)
            bullet_content_parent_layout.touchDelegate = touchDelegate
        }
    }

    private fun bindViewModel() {
        bulletViewModel.getDailyBullet()
                .observe(this, Observer{
                    it?.let { render(it) }
                })

        bulletViewModel.getSaved()
                .observe(this, Observer {
                    it?.let { onSaved(it) }
                })

        arguments?.getString(Navigator.ARG_BULLET_ID)?.apply {
            if (this.isNullOrBlank()) {
                bulletViewModel.newDailyBullet(arguments?.getLong(Navigator.ARG_DATE_LONG))
            } else {
                bulletViewModel
                        .fetchDailyBullet(arguments?.getString(Navigator.ARG_BULLET_ID) ?: "")
            }
        }
    }

    private fun onSaved(shouldSave: Boolean) {
        activity?.finish()
    }

    private fun render(dailyBullet: DailyBullet) {
        bullet_title.setText(dailyBullet.title)
        if (dailyBullet.ticked) {

        } else {
            bullet_content_container.layoutResource = R.layout.view_bullet_content
            bullet_content_container.inflate()
            bullet_content_edit_text.setText(dailyBullet.content)
            if (dailyBullet.content.isNullOrBlank()) {
                bullet_content_edit_text.requestFocus()
            }
//            setContentViewTouchDelegate()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId) {
                android.R.id.home -> {
                    bulletViewModel.getDailyBullet().value?.title = bullet_title.text.toString()
                    bulletViewModel.getDailyBullet().value?.content = bullet_content_edit_text.text.toString()

                    bulletViewModel.exit()
                }
                else -> {}
            }
        }
        return super.onOptionsItemSelected(item)
    }
}