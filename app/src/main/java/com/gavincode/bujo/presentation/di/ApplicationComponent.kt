package com.gavincode.bujo.presentation.di

import com.gavincode.bujo.BujoApplication
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by gavinlin on 22/3/18.
 */

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: BujoApplication): Builder
        fun build(): ApplicationComponent
    }

    fun inject(application: BujoApplication)
}