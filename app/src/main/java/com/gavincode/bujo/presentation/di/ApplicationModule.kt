package com.gavincode.bujo.presentation.di

import android.content.Context
import com.gavincode.bujo.BujoApplication
import com.gavincode.bujo.data.repository.DailyBulletRepositoryImpl
import com.gavincode.bujo.domain.repository.DailyBulletRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by gavinlin on 11/3/18.
 */

@Module
class ApplicationModule(val application: BujoApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun proivdeDailyBulletRepository(dailyBulletRepository: DailyBulletRepositoryImpl)
        : DailyBulletRepository {
        return dailyBulletRepository
    }
}