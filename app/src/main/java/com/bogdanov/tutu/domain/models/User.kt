package com.bogdanov.tutu.domain.models

data class User(
    val id: Long,
    val username: String,
    val avatar: String?,
    val name: String?,
    val company: String?,
    val location: String?,
    val email: String?,
    val followers: Int?,
    val following: Int?,
    val publicRepos: Int?,
    val isCashed: Boolean
)
