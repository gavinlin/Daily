package com.gavincode.bujo.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
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