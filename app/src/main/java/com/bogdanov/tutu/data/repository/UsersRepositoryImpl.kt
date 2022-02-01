package com.bogdanov.tutu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bogdanov.tutu.data.remote.network.GithubApi
import com.bogdanov.tutu.domain.repository.UsersRepository
import com.bogdanov.tutu.data.remote.paging_source.UsersPagingSource
import com.bogdanov.tutu.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class UsersRepositoryImpl(
    private val api: GithubApi
): UsersRepository {

    override suspend fun getUserInfo(username: String): Flow<User> = flow{
        try {
            val response = api.getUserInfo(username)

            if (response.isSuccessful && response.body()!=null) {
                emit(response.body()!!.toDomainModel())
            }
        }catch (e: Exception){

        }
    }

    override fun searchUsers(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 7,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UsersPagingSource(api, query) }
        ).flow
    }


}