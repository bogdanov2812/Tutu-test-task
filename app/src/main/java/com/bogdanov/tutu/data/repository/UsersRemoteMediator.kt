package com.bogdanov.tutu.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bogdanov.tutu.data.local.dao.RemoteKeysDao
import com.bogdanov.tutu.data.local.dao.UserDao
import com.bogdanov.tutu.data.local.database.ApplicationDatabase
import com.bogdanov.tutu.data.local.models.RemoteKeys
import com.bogdanov.tutu.data.local.models.UserEntity
import com.bogdanov.tutu.data.remote.network.GithubApi
import com.bogdanov.tutu.domain.models.User
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class UsersRemoteMediator(
    private val api: GithubApi,
    private val query: String,
    private val appDatabase: ApplicationDatabase
): RemoteMediator<Int, UserEntity>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, UserEntity>): MediatorResult {
        try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )

                    nextPage
                }
            }

            println(loadType)
            println(currentPage)

            val response = api.searchUsers(query = query, page = currentPage, perPage = state.config.pageSize)

            var endOfPaginationReached = true

            if (response.isSuccessful && response.body() != null) {
                val users = response.body()!!.items.map { it.toDomainModel() }

                endOfPaginationReached = false

                val prevPage = if (currentPage == 1) null else currentPage - 1
                val nextPage = if (endOfPaginationReached) null else currentPage + 1

                appDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        appDatabase.userDao().removeAll()
                        appDatabase.remoteKeysDao().clearRemoteKeys()
                    }
                    val keys = users.map { user ->
                        RemoteKeys(
                            id = user.id,
                            prevKey = prevPage,
                            nextKey = nextPage
                        )
                    }
                    appDatabase.remoteKeysDao().insertAll(remoteKey = keys)

                    val usersEntity = response.body()!!.items.map { it.toEntity() }

                    println(usersEntity)
                    appDatabase.userDao().insertUsers(userEntities = usersEntity)
                }


            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            println("Exception = $e")
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UserEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                appDatabase.remoteKeysDao().getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, UserEntity>
    ): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { user ->
                appDatabase.remoteKeysDao().getRemoteKeys(id = user.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, UserEntity>
    ): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { user ->
                println(user)
                appDatabase.remoteKeysDao().getRemoteKeys(id = user.id)
            }
    }
}