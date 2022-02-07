package com.bogdanov.tutu.domain.repository

import androidx.paging.PagingData
import com.bogdanov.tutu.data.local.models.UserEntity
import com.bogdanov.tutu.data.remote.models.UserDto
import com.bogdanov.tutu.domain.models.User
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface UsersRepository {
    fun getUserInfo (username: String): Flow<User?>

    fun searchUsers(query: String): Flow<PagingData<User>>

    fun getUsers(query: String): Flow<PagingData<User>>

}