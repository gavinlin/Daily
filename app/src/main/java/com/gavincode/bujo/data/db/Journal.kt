package com.gavincode.bujo.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.gavincode.bujo.data.model.AttachmentEntity
import com.gavincode.bujo.data.model.DailyBulletEntity

/**
 * Created by gavinlin on 10/3/18.
 */

@Database(entities = [DailyBulletEntity::class,
    AttachmentEntity::class], version = 1)
@TypeConverters(DailyBulletConverters::class)
abstract class Journal: RoomDatabase() {
    abstract fun dailyBulletDao(): DailyBulletDao
    abstract fun attachmentDao(): AttachmentDao
}