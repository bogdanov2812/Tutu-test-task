package com.bogdanov.tutu.data.remote.network

import com.bogdanov.tutu.data.remote.models.ItemsWrapper
import com.bogdanov.tutu.data.remote.models.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("users/{username}")
    suspend fun getUserInfo(
        @Path("username") username: String
    ): Response<UserDto>

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): Response<ItemsWrapper<UserDto>>
}