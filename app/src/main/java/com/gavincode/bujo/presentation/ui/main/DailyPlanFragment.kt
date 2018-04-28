package com.gavincode.bujo.presentation.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.LinearLayout
import butterknife.ButterKnife
import butterknife.OnClick
import com.gavincode.bujo.R
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.presentation.ui.NavigateResultInfo
import com.gavincode.bujo.presentation.ui.Navigator
import com.gavincode.bujo.presentation.ui.bullet.BulletActivity
import com.gavincode.bujo.presentation.ui.widget.CalendarManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_daily_plan.*
import org.threeten.bp.LocalDate
import javax.inject.Inject

/**
 * Created by gavinlin on 26/2/18.
 */

class DailyPlanFragment: Fragment(), DailyListClickListener {

    companion object {
        fun getInstance(): DailyPlanFragment {
            return DailyPlanFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var dailyListViewModel: DailyListViewModel


    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val calendarManager = CalendarManager
        val minDate = LocalDate.of(2017, 1, 1)
//        val minDate = LocalDate.now().minusMonths(2).withDayOfMonth(1)
        val maxDate = LocalDate.now().plusYears(1)
        calendarManager.buildCal(minDate, maxDate)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_daily_plan, container, false)
        ButterKnife.bind(this, view)
        val appBar = inflater.inflate(R.layout.view_calendar_toolbar, null, false)
        appBar.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        (view as CoordinatorLayout).addView(appBar)
        val toolbar = appBar.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity)?.setSupportActionBar(toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        week_calendar_view.init()

        CalendarManager.currentDayLiveData.observe(this, Observer {
            (activity as MainActivity).supportActionBar?.title = it?.month?.name
//            daily_bullet_view.setCurrentItem(Math.abs(it!!.duration(CalendarManager.days.first().date)).toInt(), true)
            it?.run { dailyListViewModel.setDate(this) }
        })

        bindView()
    }

    private fun bindView() {
        dailyListViewModel =
                ViewModelProviders.of(this, viewModelFactory)
                        .get(DailyListViewModel::class.java)
        daily_list_recycler_view.layoutManager = LinearLayoutManager(context)
        daily_list_recycler_view.adapter = DailyListAdapter()
        (daily_list_recycler_view.adapter as DailyListAdapter).dailyListOnClickListener = this
        daily_plan_list_date_view.visibility = View.VISIBLE
        daily_list_recycler_view.visibility = View.GONE
        swipe_refresh_view.setOnRefreshListener {
            dailyListViewModel.fetchLiveData()
        }
        Navigator.getActivityForResultLiveData().observe(this,
                Observer { it?.apply { handleActivityResult(it) } })
        dailyListViewModel.bindDate().observe(this,
                Observer {
                    it?.run { dailyListViewModel.fetchLiveData() }
                })
        dailyListViewModel.bindUiModel().observe(this,
                Observer { it?.run { renderUi(this) } })
        Navigator.getActivityForResultLiveData().observe(this,
                Observer { it?.apply { handleActivityResult(it) } })
    }

    private fun renderUi(uiModel: DailyListUiModel) {
        when (uiModel) {
            is DailyListUiModel.Idle -> onIdle()
            is DailyListUiModel.Loading -> onLoading()
            is DailyListUiModel.Empty -> onEmpty()
            is DailyListUiModel.DailyBullets -> onDailyBullets(uiModel.dailyBullets)
        }
    }

    private fun onIdle() {
        swipe_refresh_view.isRefreshing = false
    }

    private fun onLoading() {
        if (swipe_refresh_view.isRefreshing) return
        swipe_refresh_view.isRefreshing = true
    }

    private fun onEmpty() {
        swipe_refresh_view.isRefreshing = false
        daily_plan_list_date_view.visibility = View.VISIBLE
        daily_list_recycler_view.visibility = View.GONE
    }

    private fun onDailyBullets(list: List<DailyBullet>) {
        swipe_refresh_view.isRefreshing = false
        daily_plan_list_date_view.visibility = View.GONE
        daily_list_recycler_view.visibility = View.VISIBLE

        (daily_list_recycler_view.adapter as DailyListAdapter)
                .updateList(list)
    }

    @OnClick(R.id.daily_bullet_button)
    fun onBulletClicked() {
        val intent = Intent(activity, BulletActivity::class.java)
        intent.putExtra(Navigator.ARG_DATE_LONG, CalendarManager.currentDayLiveData.value?.toEpochDay())
        startActivityForResult(intent, Navigator.REQ_BULLET_ADD)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.run {
            inflater?.inflate(R.menu.fragment_calendar, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return item?.run {
            when(itemId) {
                R.id.action_today -> {

                    CalendarManager.setCurrentDay(CalendarManager.today)
                    true
                }
                else ->super.onOptionsItemSelected(item)
            }
        } ?: return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Navigator.sendActivityResult(NavigateResultInfo(requestCode,  resultCode, data))
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleActivityResult(navigateResultInfo: NavigateResultInfo) {
        when (navigateResultInfo.requestCode) {
            Navigator.REQ_BULLET_EDIT, Navigator.REQ_BULLET_ADD -> {
                dailyListViewModel.fetchLiveData()
            }
        }
    }

    // DailyListClickListener
    override fun onClick(bulletId: String) {
        val intent = Intent(activity, BulletActivity::class.java)
        intent.putExtra(Navigator.ARG_BULLET_ID, bulletId)
        startActivityForResult(intent, Navigator.REQ_BULLET_EDIT)
    }
}