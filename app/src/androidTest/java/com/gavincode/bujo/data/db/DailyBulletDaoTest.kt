package com.gavincode.bujo.data.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.test.runner.AndroidJUnit4
import com.gavincode.bujo.data.model.DailyBulletEntity
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by gavinlin on 10/3/18.
 */

@RunWith(AndroidJUnit4::class)
class DailyBulletDaoTest: DbTest() {

    @Test
    fun testInsert() {
        val dailyBulletEntity = com.gavincode.bujo.data.model.DailyBulletEntity(
                UUID.randomUUID().toString(),
                "abc",
                "",
                "",
                LocalDate.now(),
                0,
                listOf("/cde", "goodtodo")
        )

        try {
            db.beginTransaction()
            db.dailyBulletDao().insert(dailyBulletEntity)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        val liveData = db.dailyBulletDao().loadById(dailyBulletEntity.uid)
        val gotBullet = getValue(liveData)
        assert(gotBullet.uid == dailyBulletEntity.uid)
        assert(gotBullet.title == "abc")
        assert(gotBullet.images!!.size == 2)
        assert(gotBullet.images!![0] == "/cde")
        assertEquals(gotBullet.date.toEpochDay(), 1520726400/60/60/24

        )
        println(gotBullet.images!![1])
    }

    @Test
    fun testLoadByDate() {
        val dailyBulletEntity = DailyBulletEntity(
                UUID.randomUUID().toString(),
                "cde",
                "",
                "",
                LocalDate.now(),
                0,
                listOf("")
        )

        val dailyBulletEntity2 = DailyBulletEntity(
                UUID.randomUUID().toString(),
                "fgh",
                "",
                "",
                LocalDate.now(),
                0,
                listOf("")
        )



    }

    fun <T> getValue(liveData: LiveData<T>): T {
        val data: Array<Any> = arrayOf<Any>(1)
        val latch = CountDownLatch(1)
        val observer: Observer<T> = object: Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t as Any
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return data[0] as T
    }
}