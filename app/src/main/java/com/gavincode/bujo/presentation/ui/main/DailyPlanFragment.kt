package com.gavincode.bujo.presentation.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.ui.widget.CalendarManager
import com.gavincode.bujo.presentation.ui.widget.WeekCalendar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.threeten.bp.LocalDate
import timber.log.Timber

/**
 * Created by gavinlin on 26/2/18.
 */

class DailyPlanFragment: Fragment() {
    companion object {
        fun getInstance(): DailyPlanFragment {
            val fragment = DailyPlanFragment()
            return fragment
        }
    }

    @BindView(R.id.week_calendar_view)
    lateinit var calendarView: WeekCalendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val calendarManager = CalendarManager
        val minDate = LocalDate.now().minusMonths(2).withDayOfMonth(1)
        val maxDate = LocalDate.now().plusYears(1)
        calendarManager.buildCal(minDate, maxDate)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_daily_plan, container, false)
        ButterKnife.bind(this, view)
        calendarView.init()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            db.collection("users").document(it.uid).collection("tasks")
                    .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        Timber.d("${doc.id} => ${doc.data["content"]}")
                    }
                } else {
                    it.exception?.printStackTrace()
                }
            }
        }
    }
}