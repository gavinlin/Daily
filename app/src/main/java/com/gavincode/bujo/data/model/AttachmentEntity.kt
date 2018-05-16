package com.gavincode.bujo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by gavinlin on 24/3/18.
 */

@Entity(tableName = "Attachment")
data class AttachmentEntity(
        @PrimaryKey val id: String,
        val uriPath: String,
        val name: String,
        val size: Long,
        val mimeType: String,
        val dailyBulletId: String
)