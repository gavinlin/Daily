package com.gavincode.bujo.presentation.di

import com.gavincode.bujo.presentation.ui.bullet.BulletActivity
import com.gavincode.bujo.presentation.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by gavinlin on 23/3/18.
 */

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeBulletActivity(): BulletActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}