package com.gavincode.bujo.data.mapper

import com.gavincode.bujo.data.model.AttachmentEntity
import com.gavincode.bujo.domain.Attachment

/**
 * Created by gavinlin on 24/3/18.
 */

object AttachmentMapper {
    fun toAttachment(attachmentEntity: AttachmentEntity): Attachment {
         return Attachment(
                attachmentEntity.id,
                attachmentEntity.uriPath,
                attachmentEntity.name,
                attachmentEntity.size,
                attachmentEntity.mimeType
        )
    }

    fun toAttachments(attachmentEntities: List<AttachmentEntity>): List<Attachment> {
        val list = mutableListOf<Attachment>()
        attachmentEntities.forEach {
            list.add(toAttachment(it))
        }
        return list
    }
}