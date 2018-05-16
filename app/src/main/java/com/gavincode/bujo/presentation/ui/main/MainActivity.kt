package com.gavincode.bujo.presentation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gavincode.bujo.R
import com.gavincode.bujo.presentation.ui.inbox.InboxFragment
import com.gavincode.bujo.presentation.ui.setting.SettingFragment
import com.gavincode.bujo.presentation.ui.widget.CalendarManager
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.LocalDate
import javax.inject.Inject

/**
 * Created by gavinlin on 26/2/18.
 */

class MainActivity: AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val calendarManager = CalendarManager
        val minDate = LocalDate.of(2017, 1, 1)
        val maxDate = LocalDate.now().plusYears(1)
        calendarManager.buildCal(minDate, maxDate)

        val dailyPlanFragment = DailyPlanFragment.getInstance()
        val settingFragment = SettingFragment.getInstance()
        val inboxFragment = InboxFragment.getInstance()

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_inbox -> {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_container, inboxFragment)
                            .commitNow()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_calendar -> {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_container, dailyPlanFragment)
                            .commitNow()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_setting -> {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_container, settingFragment)
                            .commitNow()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        bottom_navigation.selectedItemId = R.id.action_inbox
    }


    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> {
        return dispatchingAndroidInjector
    }
}