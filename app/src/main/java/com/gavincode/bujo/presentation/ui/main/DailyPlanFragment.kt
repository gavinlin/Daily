package com.gavincode.bujo.presentation.ui.main

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.ui.bullet.BulletActivity
import com.gavincode.bujo.presentation.ui.widget.CalendarManager
import com.gavincode.bujo.presentation.util.duration
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_daily_plan.*
import org.threeten.bp.LocalDate

/**
 * Created by gavinlin on 26/2/18.
 */

class DailyPlanFragment: Fragment() {
    companion object {
        fun getInstance(): DailyPlanFragment {
            return DailyPlanFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val calendarManager = CalendarManager
        val minDate = LocalDate.now().minusMonths(2).withDayOfMonth(1)
        val maxDate = LocalDate.now().plusYears(1)
        calendarManager.buildCal(minDate, maxDate)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_daily_plan, container, false)
        activity?.let {
            ButterKnife.bind(this, view)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        week_calendar_view.init()

        daily_bullet_view.adapter = DailyPlanPagerAdapter(CalendarManager.days[0].date,
                CalendarManager.days.last().date, activity!!.supportFragmentManager)
        daily_bullet_view.currentItem =
                Math.abs(CalendarManager.today.duration(CalendarManager.days.first().date)).toInt()
        daily_bullet_view.addOnPageChangeListener(object :ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                CalendarManager.setCurrentDay(CalendarManager.days.first().date.plusDays(position.toLong()))
            }
        })
        CalendarManager.currentDayLiveData.observe(this, Observer {
            (activity as MainActivity).supportActionBar?.title = it!!.month.name
            daily_bullet_view.setCurrentItem(Math.abs(it!!.duration(CalendarManager.days.first().date)).toInt(), true)
        })
    }

    @OnClick(R.id.daily_bullet_button)
    fun onBulletClicked() {
        val intent = Intent(activity, BulletActivity::class.java)
        activity?.startActivity(intent)
    }
}