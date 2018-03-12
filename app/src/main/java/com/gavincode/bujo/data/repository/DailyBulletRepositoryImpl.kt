package com.gavincode.bujo.data.repository

import com.gavincode.bujo.data.datastore.DailyBulletDataStore
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
class DailyBulletRepositoryImpl @Inject constructor(dailyBulletDataStore: DailyBulletDataStore): DailyBulletRepository {

    

    override fun getDailyBullets(date: LocalDate): Maybe<List<DailyBullet>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDailyBullet(id: String): Maybe<DailyBullet> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateDailyBullet(dailyBullet: DailyBullet) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}