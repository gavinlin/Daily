package com.gavincode.bujo.presentation.ui.widget

import org.threeten.bp.LocalDate

/**
 * Created by gavinlin on 14/3/18.
 */

data class DayItem(
        val date: LocalDate, val value: Int,
        var month: String
) {
    val isToday = date.isEqual(LocalDate.now())
    val isFirstDayOfTheMonth: Boolean = date.isEqual(date.withDayOfMonth(1))
    var isSelected: Boolean = false
}