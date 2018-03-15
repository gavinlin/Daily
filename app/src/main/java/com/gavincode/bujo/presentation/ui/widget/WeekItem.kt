package com.gavincode.bujo.presentation.ui.widget

import org.threeten.bp.LocalDate

/**
 * Created by gavinlin on 14/3/18.
 */

data class WeekItem(var weekInYear: Int, var year: Int,
                    var date: LocalDate, var label: String,
                    var month: Int,
                    var dayItems: List<DayItem>? = null)