package com.bogdanov.tutu.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bogdanov.tutu.data.local.dao.RemoteKeysDao
import com.bogdanov.tutu.data.local.dao.UserDao
import com.bogdanov.tutu.data.local.models.RemoteKeys
import com.bogdanov.tutu.data.local.models.UserEntity

@Database(
    entities = [
        UserEntity::class,
               RemoteKeys::class],
        version = ApplicationDatabase.DB_VERSION
)

abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DB_VERSION = 1
    }
}