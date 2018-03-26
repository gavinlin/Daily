package com.gavincode.bujo.presentation.util

import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.WeekFields
import java.util.*

/**
 * Created by gavinlin on 26/3/18.
 */

fun LocalDate.duration(other: LocalDate): Long {
    return Duration.between(this.atStartOfDay(), other.atStartOfDay()).toDays()
}

fun LocalDate.sameWeek(other: LocalDate, locale: Locale): Boolean {
    val temporalField = WeekFields.of(locale).weekOfWeekBasedYear()
    return this.get(temporalField) == other.get(temporalField)
}