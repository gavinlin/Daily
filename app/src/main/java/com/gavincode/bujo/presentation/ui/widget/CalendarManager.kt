package com.gavincode.bujo.presentation.ui.widget

import androidx.lifecycle.MutableLiveData
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.util.*


/**
 * Created by gavinlin on 16/3/18.
 */

object CalendarManager {
    val today = LocalDate.now()
    val days = mutableListOf<DayItem>()
    val weeks = mutableListOf<WeekItem>()
    var locale: Locale = Locale.getDefault()
    private val temporalField = WeekFields.of(locale).weekOfWeekBasedYear()
    val weekdayFormatter = DateTimeFormatter.ofPattern("EEEEE", locale)
    private val monthHalfNameFormatter = DateTimeFormatter.ofPattern("MMM", locale)

    val currentDayLiveData = MutableLiveData<LocalDate>()

    fun setCurrentDay(day: LocalDate) {
        currentDayLiveData.postValue(day)
    }

    fun buildCal(minDate: LocalDate, maxDate: LocalDate) {
        currentDayLiveData.postValue(today)
        val maxMonth = maxDate.monthValue
        val maxYear = maxDate.year

        var weekCounter = minDate
        var currentMonth = weekCounter.monthValue
        var currentYear = weekCounter.year


        while ((currentMonth <= maxMonth || currentYear < maxYear)
                && currentYear < maxYear + 1) {
            val currentWeekOfYear = weekCounter.get(temporalField)
            val weekItem = WeekItem(currentWeekOfYear,
                    currentYear, weekCounter, weekCounter.format(monthHalfNameFormatter),
                    weekCounter.monthValue, getDayCells(weekCounter))
            weeks.add(weekItem)

            weekCounter = weekCounter.plusWeeks(1)
            currentMonth = weekCounter.monthValue
            currentYear = weekCounter.year
        }
    }

    private fun getDayCells(day: LocalDate): List<DayItem> {
        var localDay = day
        val dayItems = mutableListOf<DayItem>()
        val dayOfWeek = day.dayOfWeek.value
        val firstDayOfWeek = day.with(DayOfWeek.SUNDAY).dayOfWeek.value
        var offset = firstDayOfWeek - dayOfWeek
        if (offset > 0) {
            offset -= 7
        }
        localDay = localDay.plusDays(offset.toLong())

        for (i in 0..6) {
            val dayItem = DayItem(localDay, localDay.dayOfMonth,
                    localDay.format(monthHalfNameFormatter))
            dayItems.add(dayItem)
            localDay = localDay.plusDays(1)
        }
        days.addAll(dayItems)
        return dayItems
    }
}