package com.bogdanov.tutu.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bogdanov.tutu.data.local.models.UserEntity
import com.bogdanov.tutu.domain.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(userEntities: List<UserEntity>)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("DELETE FROM users")
    suspend fun removeAll()

    @Query("SELECT * FROM users WHERE username LIKE '%' || :username || '%'")
    fun getAllUser(username: String): PagingSource<Int, UserEntity>
}