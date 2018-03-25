package com.gavincode.bujo.domain.repository

import com.gavincode.bujo.domain.Attachment
import com.gavincode.bujo.domain.DailyBullet
import io.reactivex.Maybe
import org.threeten.bp.LocalDate

/**
 * Created by gavinlin on 9/3/18.
 */

interface DailyBulletRepository {
    fun getDailyBullets(date: LocalDate): Maybe<List<DailyBullet>>
    fun getDailyBullet(id: String): Maybe<DailyBullet>
    fun updateDailyBullet(dailyBullet: DailyBullet)
    fun updateAttachment(attachment: Attachment, parentId: String)

    fun deleteDailyBullet(dailyBullet: DailyBullet)
}