package com.gavincode.bujo.presentation.ui.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.util.CalendarBus

/**
 * Created by gavinlin on 12/3/18.
 */

class WeekCalendar: LinearLayout {

    private lateinit var weeksAdapter: WeeksAdapter

    var selectedDay: DayItem? = null

    private var currentListPosition = 0

    lateinit var listViewWeeks: WeekListView
        private set

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet)
        : super(context, attrs){
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_calendar, this, true)
        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        listViewWeeks = findViewById(R.id.list_week)
        listViewWeeks.layoutManager = LinearLayoutManager(context)
        listViewWeeks.setHasFixedSize(true)
        listViewWeeks.itemAnimator = null
        listViewWeeks.setSnapEnabled(true)

        viewTreeObserver.addOnGlobalLayoutListener (object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                if(width != 0 && height != 0) {
                    collapseCalendarView()
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        CalendarBus.toObservable()
                .subscribe {
                    if (it is CalendarEvent) {
                        when (it) {
                            is CalendarEvent.CalendarScrollEvent -> expandCalendarView()
                            is CalendarEvent.ListViewTouchEvent -> collapseCalendarView()
                            is CalendarEvent.DayClickedEvent -> {
                                val dayItem = it.dayItem
                                updateSelectedDay(dayItem)
                            }
                        }
                    }
                }
    }

    private fun expandCalendarView() {
        val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = (resources.getDimension(R.dimen.day_cell_height) * 5).toInt()
        setLayoutParams(layoutParams)
    }

    private fun collapseCalendarView() {
        val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = resources.getDimension(R.dimen.day_cell_height).toInt()
        setLayoutParams(layoutParams)
    }

    private fun updateSelectedDay(dayItem: DayItem) {
        if (!dayItem.equals(selectedDay)) {
            dayItem.isSelected = true
            selectedDay?.isSelected = false
            selectedDay = dayItem
        }


    }
}