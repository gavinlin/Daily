package com.gavincode.bujo.data.db

import androidx.sqlite.room.Room
import androidx.test.InstrumentationRegistry
import org.junit.After
import org.junit.Before

/**
 * Created by gavinlin on 10/3/18.
 */

abstract class DbTest {
    protected lateinit var db: Journal

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                Journal::class.java).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() {
        db.close()
    }
}