package com.gavincode.bujo.data.db

import androidx.room.*
import com.gavincode.bujo.data.model.DailyBulletEntity
import com.gavincode.bujo.data.model.DailyBulletWithAttachment
import io.reactivex.Maybe

/**
 * Created by gavinlin on 10/3/18.
 */

@Dao
interface DailyBulletDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dailyBulletEntity: DailyBulletEntity)

    @Delete
    fun deleteDailyBullet(dailyBulletEntity: DailyBulletEntity)

    @Query("SELECT * FROM DailyBullet WHERE uid = :id")
    fun loadById(id: String): Maybe<DailyBulletWithAttachment>

    @Query("SELECT * FROM DailyBullet WHERE date = :epochDay")
    fun loadByDate(epochDay: Long): Maybe<List<DailyBulletWithAttachment>>

}