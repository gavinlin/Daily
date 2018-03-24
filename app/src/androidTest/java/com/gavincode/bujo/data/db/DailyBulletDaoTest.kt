package com.gavincode.bujo.data.db

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.runner.AndroidJUnit4
import com.gavincode.bujo.data.model.AttachmentEntity
import com.gavincode.bujo.data.model.DailyBulletEntity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import java.util.*

/**
 * Created by gavinlin on 10/3/18.
 */

@RunWith(AndroidJUnit4::class)
class DailyBulletDaoTest: DbTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testInsert() {
        val dailyBulletEntity = com.gavincode.bujo.data.model.DailyBulletEntity(
                UUID.randomUUID().toString(),
                "abc",
                "",
                true,
                LocalDate.now(),
                0,
                false
        )
        val attachment = AttachmentEntity(
                UUID.randomUUID().toString(),
                "/abc/def",
                "abc",
                200,
                "jpeg",
                dailyBulletEntity.uid
        )

        try {
            db.beginTransaction()
            db.dailyBulletDao().insert(dailyBulletEntity)
            db.attachmentDao().insert(attachment)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        db.dailyBulletDao().loadById(dailyBulletEntity.uid)
                .test()
                .assertValue {
                    it.dailyBullet?.title == "abc" &&
                            it.dailyBullet?.ticked == true &&
                            it.attachments.size == 1 &&
                            it.attachments[0].id == attachment.id
                }
//        val gotBullet = getValue(liveData)
//        assert(gotBullet.uid == dailyBulletEntity.uid)
//        assert(gotBullet.title == "abc")
//        assert(gotBullet.images!!.size == 2)
//        assert(gotBullet.images!![0] == "/cde")
//        assertEquals(gotBullet.date.toEpochDay(), 1520726400/60/60/24
//
//        )
//        println(gotBullet.images!![1])
    }

    @Test
    fun testLoadByDate() {
        val dailyBulletEntity = DailyBulletEntity(
                UUID.randomUUID().toString(),
                "cde",
                "",
                true,
                LocalDate.now(),
                0,
                false
        )

        val dailyBulletEntity2 = DailyBulletEntity(
                UUID.randomUUID().toString(),
                "fgh",
                "",
                true,
                LocalDate.now(),
                0,
                false
        )

        
    }
}