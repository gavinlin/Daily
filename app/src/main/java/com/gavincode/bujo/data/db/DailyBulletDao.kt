package com.gavincode.bujo.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.gavincode.bujo.data.model.DailyBulletEntity
import io.reactivex.Maybe

/**
 * Created by gavinlin on 10/3/18.
 */

@Dao
interface DailyBulletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dailyBulletEntity: DailyBulletEntity)

    @Query("SELECT * FROM DailyBullet WHERE uid = :id")
    fun loadById(id: String): Maybe<DailyBulletEntity>

    @Query("SELECT * FROM DailyBullet WHERE date = :epochDay")
    fun loadByDate(epochDay: Long): Maybe<List<DailyBulletEntity>>

}