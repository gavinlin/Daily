package com.gavincode.bujo.presentation.ui.widget

import android.animation.LayoutTransition
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.util.CalendarBus
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.WeekFields
import java.util.*

/**
 * Created by gavinlin on 12/3/18.
 */

class WeekCalendar: LinearLayout {

    private lateinit var weeksAdapter: WeeksAdapter

    var selectedDay: DayItem? = null

    private var currentListPosition = 0

    lateinit var listViewWeeks: WeekListView
        private set

    var mCalendarDayTextColor: Int = 0
    var mCalendarCurrentDayColor: Int = 0
    var mCalendarPastDayTextColor: Int = 0
    var mCalendarBackgroundColor: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet)
        : super(context, attrs){

        val t = context.obtainStyledAttributes(attrs, R.styleable.ColorOptionsView, 0, 0)
        mCalendarBackgroundColor = t.getColor(R.styleable.ColorOptionsView_calendarColor,
                ContextCompat.getColor(context, R.color.colorPrimary))
        mCalendarDayTextColor = t.getColor(R.styleable.ColorOptionsView_calendarDayTextColor,
                ContextCompat.getColor(context, R.color.calendar_text_default))
        mCalendarCurrentDayColor = t.getColor(R.styleable.ColorOptionsView_calendarCurrentDayTextColor,
                ContextCompat.getColor(context, R.color.calendar_text_current_day))
        mCalendarPastDayTextColor = t.getColor(R.styleable.ColorOptionsView_calendarPastDayTextColor,
                ContextCompat.getColor(context, R.color.colorPrimary))

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_calendar,this,true)
        orientation = VERTICAL
//        alpha = 0f
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        listViewWeeks = findViewById(R.id.list_week)
        listViewWeeks.layoutManager = LinearLayoutManager(context)
        listViewWeeks.setHasFixedSize(true)
        listViewWeeks.itemAnimator = null
        listViewWeeks.setSnapEnabled(true)
        layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

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

    fun init() {
        val today = CalendarManager.today
        val locale = CalendarManager.locale
        val weeks = CalendarManager.weeks

        setUpAdapter(today, weeks, mCalendarDayTextColor, mCalendarCurrentDayColor,
                mCalendarPastDayTextColor)
        scrollToDate(today, weeks, locale)
    }

    fun scrollToDate(date: LocalDate, weeks: List<WeekItem>, locale: Locale) {
        var curretnWeekIndex: Int? = null

        for (i in 0..(weeks.size - 1)) {
            if (sameWeek(date , weeks[i].date, locale)) {
                curretnWeekIndex = i
                break
            }
        }
        curretnWeekIndex?.let {
            listViewWeeks.post({
                scrollToPosition(it)
            })
        }
    }

    override fun setBackgroundColor(color: Int) {
        listViewWeeks.setBackgroundColor(color)
    }

    private fun sameWeek(thisDay: LocalDate, otherDay: LocalDate, locale: Locale)
            : Boolean{
        val temporalField = WeekFields.of(locale).weekOfWeekBasedYear()
        return thisDay.get(temporalField) == otherDay.get(temporalField)
    }

    private fun setUpAdapter(today: LocalDate, weeks: List<WeekItem>,
                             dayTextColor: Int,
                             currentDayTextColor: Int,
                             postDayTextColor: Int) {
        val weeksAdapter = WeeksAdapter(today, dayTextColor, currentDayTextColor, postDayTextColor)
        listViewWeeks.adapter = weeksAdapter
        weeksAdapter.updateWeeksItem(weeks)
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

    private fun scrollToPosition(targetPosition: Int) {
        listViewWeeks.layoutManager.scrollToPosition(targetPosition)
    }

    private fun updateItemAtPosition(position: Int) {
        listViewWeeks.adapter.notifyItemChanged(position)
    }

    private fun updateSelectedDay(dayItem: DayItem) {
        var currentWeekIndex: Int? = null
        var day = dayItem.date
        if (!dayItem.equals(selectedDay)) {
            dayItem.isSelected = true
            selectedDay?.isSelected = false
            selectedDay = dayItem
        }

        for (i in 0..(CalendarManager.weeks.size - 1)) {
            if (day.isEqual(CalendarManager.weeks[i].date)) {
                currentWeekIndex = i
                break
            }
        }

        currentWeekIndex?.let {
            if (it != currentListPosition) {
                updateItemAtPosition(it)
            }
        }
    }
}