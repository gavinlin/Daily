package com.gavincode.bujo.data.mapper

import com.gavincode.bujo.data.model.DailyBulletEntity
import com.gavincode.bujo.data.model.DailyBulletWithAttachment
import com.gavincode.bujo.domain.DailyBullet

/**
 * Created by gavinlin on 22/3/18.
 */

object DailyBulletMapper {
    fun toDailyBullet(dailyBulletEntity: DailyBulletWithAttachment): DailyBullet {
        var dailyBullet: DailyBullet? = null
        dailyBulletEntity.dailyBullet?.let {
            dailyBullet = DailyBullet(it.uid,
                it.title,
                it.content,
                it.ticked,
                it.date,
                it.bullet,
                    it.archive,
                AttachmentMapper.toAttachments(dailyBulletEntity.attachments))
        }
        return dailyBullet!!
    }

    fun toDailyBulletList(it: List<DailyBulletWithAttachment>): List<DailyBullet> {
        val list = mutableListOf<DailyBullet>()
        it.forEach({
            list.add(toDailyBullet(it))
        })
        return list
    }

    fun toDailyBulletEntity(dailyBullet: DailyBullet): DailyBulletEntity {
        return DailyBulletEntity(
                dailyBullet.id,
                dailyBullet.title,
                dailyBullet.content,
                dailyBullet.ticked,
                dailyBullet.date,
                dailyBullet.bullet,
                dailyBullet.isArchive
        )
    }
}