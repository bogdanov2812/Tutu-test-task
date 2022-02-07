package com.bogdanov.tutu.data.repository

import androidx.paging.*
import com.bogdanov.tutu.data.local.dao.UserDao
import com.bogdanov.tutu.data.local.database.ApplicationDatabase
import com.bogdanov.tutu.data.local.models.UserEntity
import com.bogdanov.tutu.data.remote.network.GithubApi
import com.bogdanov.tutu.domain.repository.UsersRepository
import com.bogdanov.tutu.data.remote.paging_source.UsersPagingSource
import com.bogdanov.tutu.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.lang.Exception

@ExperimentalPagingApi
class UsersRepositoryImpl(
    private val api: GithubApi,
    private val userDao: UserDao,
    private val database: ApplicationDatabase
): UsersRepository {

    override fun getUserInfo(username: String): Flow<User?> = flow{
        try {
            var userEntity = userDao.getUserByUsername(username)

            if (userEntity == null){
                val response = api.getUserInfo(username)

                if (response.isSuccessful && response.body()!=null) {
                    userEntity = response.body()!!.toEntity()

                    userDao.insertUsers(listOf(userEntity))
                    emit(userEntity.toDomainModel())

                }
            }else{
                emit(userEntity.toDomainModel())
            }


        }catch (e: Exception){
            println(e)
        }
    }.flowOn(Dispatchers.IO)

    override fun searchUsers(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 7,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UsersPagingSource(api, query) }

        ).flow
    }

    override fun getUsers(query: String): Flow<PagingData<User>> {
        val pagingSource = {database.userDao().getAllUser(query)}
        return Pager(config = PagingConfig(
            pageSize = 7,
            enablePlaceholders = false
        ),
            pagingSourceFactory = pagingSource,
            remoteMediator = UsersRemoteMediator(api,query, appDatabase = database)
        ).flow
            .map { it->
                it.map {
                    it.toDomainModel() }
            }
    }


}