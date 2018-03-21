package com.gavincode.bujo.presentation.ui.bullet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.gavincode.bujo.R

/**
 * Created by gavinlin on 20/3/18.
 */

class BulletFragment: Fragment() {

    companion object {
        fun newInstance(): Fragment {
            return BulletFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bullet, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId) {
                android.R.id.home -> activity?.finish()
                else -> {}
            }
        }
        return super.onOptionsItemSelected(item)
    }
}