package com.gavincode.bujo.presentation.di

import androidx.room.Room
import com.gavincode.bujo.BujoApplication
import com.gavincode.bujo.data.db.AttachmentDao
import com.gavincode.bujo.data.db.DailyBulletDao
import com.gavincode.bujo.data.db.Journal
import com.gavincode.bujo.data.db.JournalMigration
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

    companion object {
        const val DB_NAME = "journal.db"
    }

    @Provides
    @Singleton
    fun provideDailyBulletRepository(db: Journal, dailyBulletDao: DailyBulletDao, attachmentDao: AttachmentDao)
        : DailyBulletRepository {
        return DailyBulletRepositoryImpl(db, dailyBulletDao, attachmentDao)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(application: BujoApplication): Journal {
        return Room.databaseBuilder(application,
                Journal::class.java, DB_NAME)
                .addMigrations(JournalMigration.MIGRATION_1_2)
                .build()
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