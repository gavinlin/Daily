package com.gavincode.bujo.presentation.di

import android.arch.persistence.room.Room
import com.gavincode.bujo.BujoApplication
import com.gavincode.bujo.data.db.AttachmentDao
import com.gavincode.bujo.data.db.DailyBulletDao
import com.gavincode.bujo.data.db.Journal
import com.gavincode.bujo.data.repository.DailyBulletRepositoryImpl
import com.gavincode.bujo.domain.repository.DailyBulletRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by gavinlin on 11/3/18.
 */

@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideDailyBulletRepository(dailyBulletDao: DailyBulletDao, attachmentDao: AttachmentDao)
        : DailyBulletRepository {
        return DailyBulletRepositoryImpl(dailyBulletDao, attachmentDao)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(application: BujoApplication): Journal {
        return Room.databaseBuilder(application,
                Journal::class.java, "journal.db").build()
    }

    @Provides
    @Singleton
    fun provideDailyBulletDao(db: Journal): DailyBulletDao {
        return db.dailyBulletDao()
    }

    @Provides
    @Singleton
    fun provideAttachmentDao(db: Journal): AttachmentDao {
        return db.attachmentDao()
    }
}