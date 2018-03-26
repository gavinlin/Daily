package com.gavincode.bujo.presentation.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gavincode.bujo.R
import kotlinx.android.synthetic.main.fragment_daily_plan_list.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by gavinlin on 26/3/18.
 */


class DailyListFragment: Fragment() {

    companion object {
        fun newInstance(day: LocalDate): DailyListFragment {
            val fragment = DailyListFragment()
            val args = Bundle()
            args.putSerializable("date", day)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily_plan_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (arguments?.getSerializable("date") as LocalDate?)?.let {
            daily_plan_list_date_view.text = it.format(DateTimeFormatter.ISO_LOCAL_DATE)
        }
    }
}