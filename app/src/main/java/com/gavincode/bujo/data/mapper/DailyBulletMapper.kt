package com.gavincode.bujo.data.mapper

import com.gavincode.bujo.data.model.DailyBulletEntity
import com.gavincode.bujo.domain.DailyBullet

/**
 * Created by gavinlin on 22/3/18.
 */

object DailyBulletMapper {
    fun toDailyBullet(dailyBulletEntity: DailyBulletEntity): DailyBullet {
        return DailyBullet(dailyBulletEntity.uid,
                dailyBulletEntity.title,
                dailyBulletEntity.content,
                dailyBulletEntity.ticked,
                dailyBulletEntity.date,
                dailyBulletEntity.bullet,
                dailyBulletEntity.images)
    }

    fun toDailyBulletList(it: List<DailyBulletEntity>): List<DailyBullet> {
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
                dailyBullet.images
        )
    }
}