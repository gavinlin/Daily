package com.gavincode.bujo.domain

import org.threeten.bp.LocalDate


/**
 * Created by gavinlin on 7/3/18.
 */

data class DailyBullet (
        val id: String,
        val title: String,
        val content: String,
        val ticked: Boolean,
        val date: LocalDate,
        val bullet: Int,
        val isArchive: Boolean,
        val images: List<Attachment>?)
