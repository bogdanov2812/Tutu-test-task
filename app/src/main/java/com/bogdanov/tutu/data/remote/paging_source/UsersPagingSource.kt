package com.bogdanov.tutu.data.remote.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bogdanov.tutu.data.remote.network.GithubApi
import com.bogdanov.tutu.domain.models.User
import retrofit2.HttpException

class UsersPagingSource(
    private val api: GithubApi,
    private val query: String
): PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        val anchorPosition = state.anchorPosition ?: return null

        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val pageIndex = params.key ?: 1
        val pageSize = params.loadSize

        if (query.isBlank()){
            return LoadResult.Page(emptyList(),prevKey = null, nextKey = null)
        }

        return try {
            val response = api.searchUsers(query = query, perPage = pageSize, page = pageIndex)

            if (response.isSuccessful && response.body() != null){
                val users = response.body()!!.items.map { it.toDomainModel() }
                val nextPageNumber = if (users.size == pageSize) pageIndex + (params.loadSize / 7) else null
                val prevPageNumber = if (pageIndex == 1) null else pageIndex - 1

                LoadResult.Page(users, prevPageNumber, nextPageNumber)
            }else{
                LoadResult.Error(HttpException(response))
            }

        }catch (e: Exception){
            println(e)
            LoadResult.Error(throwable = e)
        }
    }
}