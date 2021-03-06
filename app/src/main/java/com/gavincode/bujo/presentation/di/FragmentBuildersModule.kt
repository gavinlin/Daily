package com.gavincode.bujo.presentation.di

import com.gavincode.bujo.presentation.ui.bullet.BulletFragment
import com.gavincode.bujo.presentation.ui.inbox.InboxFragment
import com.gavincode.bujo.presentation.ui.main.AddBulletFragment
import com.gavincode.bujo.presentation.ui.main.DailyPlanFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by gavinlin on 24/3/18.
 */

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeBulletFragment(): BulletFragment

    @ContributesAndroidInjector
    abstract fun contributeDailyPlanFragment(): DailyPlanFragment

    @ContributesAndroidInjector
    abstract fun contributeAddTaskFragment(): AddBulletFragment

    @ContributesAndroidInjector
    abstract fun contributeInboxFragment(): InboxFragment
}