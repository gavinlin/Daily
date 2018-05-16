package com.gavincode.bujo.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by gavinlin on 24/3/18.
 */

class DailyBulletWithAttachment {
    @Embedded
    var dailyBullet: DailyBulletEntity? = null

    @Relation(parentColumn = "uid",
            entityColumn = "dailyBulletId")
    var attachments: List<AttachmentEntity> = ArrayList()
}