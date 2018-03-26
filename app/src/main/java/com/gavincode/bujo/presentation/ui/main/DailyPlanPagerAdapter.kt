package com.gavincode.bujo.presentation.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.gavincode.bujo.presentation.util.duration
import org.threeten.bp.LocalDate

/**
 * Created by gavinlin on 26/3/18.
 */

class DailyPlanPagerAdapter(private val min: LocalDate, private val max: LocalDate,
                            fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return DailyListFragment.newInstance(min.plusDays(position.toLong()))
    }

    override fun getCount(): Int {
        return Math.abs(max.duration(min).toInt())
    }

}