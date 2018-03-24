package com.gavincode.bujo.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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
        val archive: Boolean)

