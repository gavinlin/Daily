package com.gavincode.bujo.data.datastore

import com.gavincode.bujo.data.db.DailyBulletDao
import org.threeten.bp.LocalDate

/**
 * Created by gavinlin on 10/3/18.
 */

class DailyBulletLocalDataStore(val dailyBulletDao: DailyBulletDao): DailyBulletDataStore {

    fun getDailyBullets(localDate: LocalDate) {
    }
}