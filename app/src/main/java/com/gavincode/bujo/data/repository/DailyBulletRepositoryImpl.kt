package com.gavincode.bujo.data.repository

import com.gavincode.bujo.data.db.DailyBulletDao
import com.gavincode.bujo.data.mapper.DailyBulletMapper
import com.gavincode.bujo.domain.DailyBullet
import com.gavincode.bujo.domain.repository.DailyBulletRepository
import io.reactivex.Maybe
import org.threeten.bp.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by gavinlin on 10/3/18.
 */

@Singleton
class DailyBulletRepositoryImpl @Inject constructor(val dailyBulletDao: DailyBulletDao): DailyBulletRepository {

    override fun getDailyBullets(date: LocalDate): Maybe<List<DailyBullet>> {
        return dailyBulletDao.loadByDate(date.toEpochDay())
                .map({DailyBulletMapper.toDailyBulletList(it)})
    }

    override fun getDailyBullet(id: String): Maybe<DailyBullet> {
        return dailyBulletDao.loadById(id).map({
            DailyBulletMapper.toDailyBullet(it)
        })
    }

    override fun updateDailyBullet(dailyBullet: DailyBullet) {
        dailyBulletDao.insert(DailyBulletMapper.toDailyBulletEntity(dailyBullet))
    }

}