package com.gavincode.bujo.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.gavincode.bujo.R
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.presentation.ui.NavigateResultInfo
import com.gavincode.bujo.presentation.ui.Navigator
import com.gavincode.bujo.presentation.ui.bullet.BulletActivity
import com.gavincode.bujo.presentation.ui.widget.CalendarManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_daily_plan.*
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_daily_plan, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
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

        add_fab.setOnClickListener {
            handleAddClicked()
        }

        bindView()
    }

    private fun handleAddClicked() {
        val intent = Intent(activity, BulletActivity::class.java)
        intent.putExtra(Navigator.ARG_DATE_LONG, CalendarManager.currentDayLiveData.value?.toEpochDay())
        startActivityForResult(intent, Navigator.REQ_BULLET_ADD)
//        val addTaskFragment = AddTaskFragment.getInstance()
//        addTaskFragment.show(fragmentManager, "addTask")

    }

    private fun bindView() {
        dailyListViewModel =
                ViewModelProviders.of(this, viewModelFactory)
                        .get(DailyListViewModel::class.java)
        daily_list_recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
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

//    @OnClick(R.id.daily_bullet_button)
//    fun onBulletClicked() {
//        val intent = Intent(activity, BulletActivity::class.java)
//        intent.putExtra(Navigator.ARG_DATE_LONG, CalendarManager.currentDayLiveData.value?.toEpochDay())
//        startActivityForResult(intent, Navigator.REQ_BULLET_ADD)
//    }

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