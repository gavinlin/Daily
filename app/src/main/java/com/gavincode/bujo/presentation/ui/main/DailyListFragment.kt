package com.gavincode.bujo.presentation.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gavincode.bujo.R
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.presentation.ui.Navigator
import com.gavincode.bujo.presentation.ui.bullet.BulletActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_daily_plan_list.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Created by gavinlin on 26/3/18.
 */


class DailyListFragment: Fragment(), DailyListClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var dailyListViewModel: DailyListViewModel

    companion object {

        private const val ARG_DATE = "date"


        fun newInstance(day: LocalDate): DailyListFragment {
            val fragment = DailyListFragment()
            val args = Bundle()
            args.putSerializable(ARG_DATE, day)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily_plan_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = arguments?.getSerializable("date") as LocalDate?
        date?.let {
            daily_plan_list_date_view.text = it.format(DateTimeFormatter.ISO_LOCAL_DATE)
        }
        dailyListViewModel =
                ViewModelProviders.of(this, viewModelFactory)
                        .get(DailyListViewModel::class.java)
        daily_list_recycler_view.layoutManager = LinearLayoutManager(context)
        daily_list_recycler_view.adapter = DailyListAdapter()
        (daily_list_recycler_view.adapter as DailyListAdapter).dailyListOnClickListener = this
        daily_plan_list_date_view.visibility = View.VISIBLE
        daily_list_recycler_view.visibility = View.GONE
        swipe_refresh_view.setOnRefreshListener {
            date?.let {
                dailyListViewModel.fetchLiveData(it)
            }
        }
        bindViewModel()
        date?.let {
            dailyListViewModel.fetchLiveData(it)
        }
    }

    private fun bindViewModel() {
        dailyListViewModel.bindUiModel()
                .observe(this, Observer {
                    it?.let { render(it) }
                })
    }

    private fun render(dailyListUiModel: DailyListUiModel) {
        when (dailyListUiModel) {
            is DailyListUiModel.Idle -> onIdle()
            is DailyListUiModel.Loading -> onLoading()
            is DailyListUiModel.Empty -> onEmpty()
            is DailyListUiModel.DailyBullets -> onDailyBullets(dailyListUiModel.dailyBullets)
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

    // DailyListClickListener
    override fun onClick(bulletId: String) {
        val intent = Intent(activity, BulletActivity::class.java)
        intent.putExtra(Navigator.ARG_BULLET_ID, bulletId)
        activity?.startActivity(intent)
    }
}