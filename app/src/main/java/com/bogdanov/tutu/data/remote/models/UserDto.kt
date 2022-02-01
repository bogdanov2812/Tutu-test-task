package com.bogdanov.tutu.data.remote.models

import com.bogdanov.tutu.domain.models.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "login")
    val username: String,
    @Json(name = "avatar_url")
    val avatar: String?,
    val id: Long,
    val name: String?,
    val company: String?,
    val location: String?,
    val email: String?,
    val followers: Int?,
    val following: Int?,
    @Json(name="public_repos")
    val publicRepos: Int?,
){
    fun toDomainModel() = User(id, username, avatar, name, company, location, email, followers, following, publicRepos)
}
