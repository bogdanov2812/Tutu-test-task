package com.bogdanov.tutu.data.local.database

import android.content.Context
import androidx.room.Room

object Database {
    lateinit var instance: ApplicationDatabase
        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            "appdb"
        )
            .build()
    }
}