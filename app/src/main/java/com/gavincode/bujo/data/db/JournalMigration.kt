package com.gavincode.bujo.data.db

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration


object JournalMigration {
    val MIGRATION_1_2 = object: Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE DailyBullet ADD COLUMN done INTEGER NOT NULL DEFAULT 0")
        }
    }
}