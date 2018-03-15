package com.gavincode.bujo.presentation.ui.widget

/**
 * Created by gavinlin on 16/3/18.
 */

sealed class CalendarEvent {
    class CalendarScrollEvent: CalendarEvent()
    class ListViewTouchEvent: CalendarEvent()
    class DayClickedEvent(val dayItem: DayItem): CalendarEvent()
}