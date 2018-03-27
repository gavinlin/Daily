package com.gavincode.bujo.data.repository

import com.gavincode.bujo.data.db.AttachmentDao
import com.gavincode.bujo.data.db.DailyBulletDao
import com.gavincode.bujo.data.db.Journal
import com.gavincode.bujo.data.mapper.AttachmentMapper
import com.gavincode.bujo.data.mapper.DailyBulletMapper
import com.gavincode.bujo.domain.Attachment
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
class DailyBulletRepositoryImpl @Inject constructor(
        private val db: Journal,private val dailyBulletDao: DailyBulletDao,
        private val attachmentDao: AttachmentDao): DailyBulletRepository {

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
        dailyBullet.attachments?.let {
            it.forEach {
                updateAttachment(it, dailyBullet.id)
            }
        }
        try {
            db.beginTransaction()
            dailyBulletDao.insert(DailyBulletMapper.toDailyBulletEntity(dailyBullet))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun updateAttachment(attachment: Attachment, parentId: String) {
        try {
            db.beginTransaction()
            attachmentDao.insert(AttachmentMapper.toAttachmentEntity(attachment, parentId))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun deleteDailyBullet(dailyBullet: DailyBullet) {
        try {
            db.beginTransaction()
            dailyBullet.attachments?.forEach {
                attachmentDao.delete(AttachmentMapper.toAttachmentEntity(it, dailyBullet.id))
            }
            dailyBulletDao.deleteDailyBullet(DailyBulletMapper.toDailyBulletEntity(dailyBullet))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}