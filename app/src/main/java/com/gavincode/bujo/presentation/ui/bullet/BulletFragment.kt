package com.gavincode.bujo.presentation.ui.bullet

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.gavincode.bujo.R
import com.gavincode.bujo.domain.DailyBullet
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_bullet.*
import javax.inject.Inject

/**
 * Created by gavinlin on 20/3/18.
 */

class BulletFragment: Fragment() {

    companion object {
        private const val BULLET_ID = "dailyBulletId"

        fun newInstance(bulletId: String): Fragment {
            val bundle = Bundle()
            bundle.putString(BULLET_ID, bulletId)
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
        bindViewHolder()
    }

    private fun bindViewHolder() {
        bulletViewModel.getDailyBullet()
                .observe(this, Observer{
                    it?.let { render(it) }
                })

        bulletViewModel.getSaved()
                .observe(this, Observer {
                    it?.let { onSaved(it) }
                })

        bulletViewModel
                .fetchDailyBullet(arguments?.getString(BULLET_ID) ?: "")
    }

    private fun onSaved(shouldSave: Boolean) {
        activity?.finish()
    }

    private fun render(dailyBullet: DailyBullet) {
        bullet_title.setText(dailyBullet.title)
        if (dailyBullet.ticked) {

        } else {
            val contentView = AppCompatTextView(context, null)
            bullet_content_container.layoutResource = R.layout.view_bullet_content
            bullet_content_container.inflate()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId) {
                android.R.id.home -> bulletViewModel.exit()
                else -> {}
            }
        }
        return super.onOptionsItemSelected(item)
    }
}