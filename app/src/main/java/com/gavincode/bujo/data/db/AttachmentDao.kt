package com.gavincode.bujo.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.gavincode.bujo.data.model.AttachmentEntity

/**
 * Created by gavinlin on 24/3/18.
 */

@Dao
interface AttachmentDao {
    @Insert(onConflict = REPLACE)
    fun insert(attachmentEntity: AttachmentEntity)

    @Delete
    fun delete(attachmentEntity: AttachmentEntity)
}