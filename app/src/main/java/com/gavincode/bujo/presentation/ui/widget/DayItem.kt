package com.gavincode.bujo.presentation.ui.widget

import org.threeten.bp.LocalDate

/**
 * Created by gavinlin on 14/3/18.
 */

data class DayItem(
        var date: LocalDate, var value: Int,
        var isToday: Boolean, var month: String
) {
    var dayOfTheWeek: Int = 0
    var isFirstDayOfTheMonth: Boolean = false
    var isSelected: Boolean = false

    fun buildDayItem(localDate: LocalDate) {
        date = localDate

        value = date.dayOfMonth
        isToday = localDate.isEqual(LocalDate.now())
        month = date.month.name
        isFirstDayOfTheMonth = date.isEqual(date.withDayOfMonth(1))
    }
}