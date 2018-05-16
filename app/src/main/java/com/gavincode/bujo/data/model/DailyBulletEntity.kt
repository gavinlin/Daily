package com.gavincode.bujo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

/**
 * Created by gavinlin on 7/3/18.
 */

@Entity(tableName = "DailyBullet")
data class DailyBulletEntity (
        @PrimaryKey
        val uid: String,
        val title: String,
        val content: String,
        val ticked: Boolean,
        val date: LocalDate,
        val bullet: Int,
        val archive: Boolean,
        val done: Boolean)

