package com.gavincode.bujo.data.mapper

import com.gavincode.bujo.data.model.DailyBulletEntity
import com.gavincode.bujo.data.model.DailyBulletWithAttachment
import com.gavincode.bujo.domain.DailyBullet

/**
 * Created by gavinlin on 22/3/18.
 */

object DailyBulletMapper {
    fun toDailyBullet(dailyBulletEntity: DailyBulletWithAttachment): DailyBullet {
        return dailyBulletEntity.dailyBullet?.run {
            DailyBullet(uid, title, content, ticked, date,
                    bullet, archive, done,
                    AttachmentMapper.toAttachments(dailyBulletEntity.attachments)
            )
        } ?: throw NullPointerException("Daily bullet is null")
    }

    fun toDailyBulletList(it: List<DailyBulletWithAttachment>): List<DailyBullet> {
        val list = mutableListOf<DailyBullet>()
        it.forEach({
            list.add(toDailyBullet(it))
        })
        return list
    }

    fun toDailyBulletEntity(dailyBullet: DailyBullet): DailyBulletEntity {
        return dailyBullet.run {
            DailyBulletEntity(id, title, content, isList, date, bullet, isArchive, done)
        }
    }
}