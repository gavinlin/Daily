package com.gavincode.bujo.domain

import org.threeten.bp.LocalDate


/**
 * Created by gavinlin on 7/3/18.
 */

data class DailyBullet (
        val id: String,
        var title: String,
        var content: String,
        var isList: Boolean,
        var date: LocalDate,
        var bullet: Int,
        var isArchive: Boolean,
        var done: Boolean,
        var attachments: List<Attachment>?)
