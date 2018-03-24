package com.gavincode.bujo.presentation.ui.bullet

import com.gavincode.bujo.presentation.di.FragmentBuildersModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by gavinlin on 23/3/18.
 */

@Module
abstract class BulletActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeBulletActivity(): BulletActivity
}