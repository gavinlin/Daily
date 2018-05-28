package com.gavincode.bujo.presentation.ui.widget

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.util.CalendarBus
import com.gavincode.bujo.presentation.util.duration
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber

/**
 * Created by gavinlin on 12/3/18.
 */

class WeekCalendar: LinearLayout {

    var selectedDay: DayItem? = null

    private var currentListPosition = 0
    private val disposables = CompositeDisposable()

    lateinit var listViewWeeks: WeekListView
        private set

    private var mCalendarDayTextColor: Int = 0
    private var mCalendarCurrentDayColor: Int = 0
    private var mCalendarPastDayTextColor: Int = 0
    private var mCalendarBackgroundColor: Int = 0
    private var mCalendarSelectedColor: Int = 0
    var currentDayObserver: Observer<LocalDate>? = null

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
        mCalendarSelectedColor = ContextCompat.getColor(context, android.R.color.white)

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_calendar,this,true)
        orientation = VERTICAL
//        alpha = 0f
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        listViewWeeks = findViewById(R.id.list_week)
        listViewWeeks.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        listViewWeeks.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL))
        listViewWeeks.setHasFixedSize(true)
        listViewWeeks.itemAnimator = null
        listViewWeeks.setSnapEnabled(true)
        setUpAdapter(CalendarManager.today, CalendarManager.weeks, mCalendarDayTextColor, mCalendarCurrentDayColor,
                mCalendarPastDayTextColor, mCalendarSelectedColor)
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
                            is CalendarEvent.DayClickedEvent -> CalendarManager.setCurrentDay(it.dayItem.date)
                        }
                    }
                }
                .addTo(disposables)

        currentDayObserver = Observer {
            it?.let { handleDayChanged(it) }
        }

        currentDayObserver?.let {
            CalendarManager.currentDayLiveData.observeForever(it)
        }
    }



    override fun onDetachedFromWindow() {
        disposables.dispose()
        currentDayObserver?.let { CalendarManager.currentDayLiveData.removeObserver(it) }
        currentDayObserver = null
        super.onDetachedFromWindow()
    }

    private fun handleDayChanged(day: LocalDate) {
        scrollToDate(day)
        collapseCalendarView()

        val dayItem = CalendarManager.days[Math.abs(CalendarManager.days.first().date.duration(day)).toInt()]
        updateSelectedDay(dayItem)
    }

    fun init() {
        val today = CalendarManager.today
        val locale = CalendarManager.locale
        val weeks = CalendarManager.weeks

        setUpAdapter(today, weeks, mCalendarDayTextColor, mCalendarCurrentDayColor,
                mCalendarPastDayTextColor, mCalendarSelectedColor)
        scrollToDate(CalendarManager.currentDayLiveData.value ?: today)
    }

    private fun scrollToDate(date: LocalDate) {
        val currentWeekIndex = getWeekIndexFromDay(date)
        if ((listViewWeeks.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstVisibleItemPosition() != currentWeekIndex) {
            listViewWeeks.post({
                scrollToPosition(currentWeekIndex)
            })
        }
    }

    override fun setBackgroundColor(color: Int) {
        listViewWeeks.setBackgroundColor(color)
    }

    private fun setUpAdapter(today: LocalDate, weeks: List<WeekItem>,
                             dayTextColor: Int,
                             currentDayTextColor: Int,
                             postDayTextColor: Int,
                             selectedTextColor: Int) {
        val weeksAdapter = WeeksAdapter(today, dayTextColor, currentDayTextColor, postDayTextColor,
                selectedTextColor)
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
        Timber.i("target position is $targetPosition")
        (listViewWeeks.layoutManager as androidx.recyclerview.widget.LinearLayoutManager)
                .scrollToPositionWithOffset(targetPosition, 0)
    }

    private fun updateItemAtPosition(position: Int) {
        listViewWeeks.adapter?.notifyItemChanged(position)
    }

    private fun updateSelectedDay(dayItem: DayItem) {
        var currentWeekIndex: Int? = null
        var day = dayItem.date
        if (!dayItem.equals(selectedDay)) {
            dayItem.isSelected = true
            selectedDay?.isSelected = false
            selectedDay = dayItem
        }

        currentWeekIndex = getWeekIndexFromDay(day)

        if (currentWeekIndex != -1) {
            if (currentWeekIndex != currentListPosition) {
                updateItemAtPosition(currentListPosition)
            }
            currentListPosition = currentWeekIndex
            updateItemAtPosition(currentWeekIndex)
        }
    }

    private fun getWeekIndexFromDay(day: LocalDate): Int {
        return ChronoUnit.WEEKS.between(CalendarManager.days.first().date, day).toInt()
    }
}