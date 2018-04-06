package com.gavincode.bujo.presentation.ui.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.util.CalendarBus
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.*

/**
 * Created by gavinlin on 14/3/18.
 */

class WeeksAdapter(val today: LocalDate,
                   val dayTextColor: Int, val currentDayTextColor: Int, val pastDayTextColor: Int,
                   val selectedTextColor: Int)
    : RecyclerView.Adapter<WeeksAdapter.WeekViewHolder>() {

    companion object {
        const val FADE_DURATION: Long = 200
    }

    val weekList: MutableList<WeekItem> = mutableListOf()
    var alphaSet: Boolean = false

    var dragging: Boolean = false
        set(value) {
            field = value
            notifyItemRangeChanged(0, weekList.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_week, parent, false)
        return WeekViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weekList.size
    }

    override fun onBindViewHolder(holder: WeekViewHolder?, position: Int) {
        val weekItem = weekList[position]
        holder?.bindWeek(weekItem, today)
    }

    inner class WeekViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val mCells: MutableList<LinearLayout> = mutableListOf()
        private val monthLabel: TextView = view.findViewById(R.id.month_label)
        private val monthBackground: FrameLayout = view.findViewById(R.id.month_background)

        init {
            setUpChildren(view.findViewById(R.id.week_days_container))
        }

        private fun setUpChildren(daysLinearLayout: LinearLayout) {
            for (i in 0..(daysLinearLayout.childCount - 1)) {
                mCells.add(daysLinearLayout.getChildAt(i) as LinearLayout)
            }
        }

        private fun setUpMonthOverlay() {
            monthLabel.visibility = View.GONE

            if (dragging) {
                val fadeIn = AnimatorSet()
                fadeIn.duration = FADE_DURATION
                val animatorAlphaIn = ObjectAnimator.ofFloat(monthLabel, "alpha",
                        monthLabel.alpha, 1f)

                val animatorBackgroundAlphaIn =
                        ObjectAnimator.ofFloat(monthBackground, "alpha",monthBackground.alpha, 1f)
                fadeIn.playTogether(
                        animatorAlphaIn,
                        animatorBackgroundAlphaIn
                )
                fadeIn.addListener(object: Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        alphaSet = true
                    }

                })
                fadeIn.start()
            } else {
                val fadeOut = AnimatorSet()
                fadeOut.duration = FADE_DURATION
                val animatorAlphaOut = ObjectAnimator.ofFloat(monthLabel, "alpha",
                        monthLabel.alpha, 0f)
                val animatorBackgroundAlphaOut =
                        ObjectAnimator.ofFloat(monthBackground, "alpha",
                                monthBackground.alpha, 0f)
                fadeOut.playTogether(animatorBackgroundAlphaOut, animatorAlphaOut)
                fadeOut.addListener(object: Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        alphaSet = false
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
                fadeOut.start()
            }

            if (alphaSet) {
                monthBackground.alpha = 1f
                monthLabel.alpha = 1f
            } else {
                monthBackground.alpha = 0f
                monthLabel.alpha = 0f
            }

        }

        fun bindWeek(weekItem: WeekItem, today: LocalDate) {
            setUpMonthOverlay()
            val dayItems = weekItem.dayItems

            dayItems?.let {
                for (i in 0..(it.size - 1)) {
                    val dayItem = it[i]
                    val cellItem = mCells[i]

                    val dayView: TextView = cellItem.findViewById(R.id.view_day_day_label)
                    val monthView: TextView = cellItem.findViewById(R.id.view_day_month_label)
                    val circleView: View = cellItem.findViewById(R.id.view_day_circle_selected)

                    cellItem.setOnClickListener {
                        CalendarManager.setCurrentDay(dayItem.date)
                        CalendarBus.send(CalendarEvent.DayClickedEvent(dayItem))
                    }

                    monthView.visibility = View.GONE
                    dayView.setTextColor(dayTextColor)
                    monthView.setTextColor(dayTextColor)
                    circleView.visibility = View.GONE

                    dayView.text = dayItem.value.toString()

                    if (dayItem.isFirstDayOfTheMonth && !dayItem.isSelected) {
                        monthView.visibility = View.VISIBLE
                        monthView.text = dayItem.month
                        dayView.setTypeface(null, Typeface.BOLD)
                        monthView.setTypeface(null, Typeface.BOLD)
                    } else {
                        dayView.setTypeface(null, Typeface.NORMAL)
                    }

                    if (today.isAfter(dayItem.date) && !today.isEqual(dayItem.date)) {
                        monthView.setTextColor(pastDayTextColor)
                        dayView.setTextColor(pastDayTextColor)
                    }

                    if (dayItem.isToday && !dayItem.isSelected) {
                        dayView.setTextColor(currentDayTextColor)
                    }

                    if (dayItem.isSelected) {
                        dayView.setTextColor(selectedTextColor)
                        circleView.visibility = View.VISIBLE
                        val drawable = circleView.background as GradientDrawable
                        drawable.setStroke((1 * Resources.getSystem().displayMetrics.density).toInt(), dayTextColor)
                    }

                    //Check if the month label has to be displayed
                    if (dayItem.value == 15) {
                        monthLabel.visibility = View.VISIBLE
                        val stringBuilder = StringBuilder()
                        stringBuilder.append(
                                weekItem.date.month.getDisplayName(
                                        TextStyle.FULL_STANDALONE, Locale.ENGLISH).toUpperCase())
                        if (today.year != weekItem.year) {
                            stringBuilder.append(" ").append(weekItem.year)
                        }
                        monthLabel.text = stringBuilder.toString()
                    }
                }
            }
        }
    }

    fun updateWeeksItem(weeks: List<WeekItem>) {
        weekList.clear()
        weekList.addAll(weeks)
        notifyDataSetChanged()
    }
}