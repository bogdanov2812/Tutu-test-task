package com.bogdanov.tutu.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bogdanov.tutu.domain.models.User
import java.util.*

@Entity(
    tableName = "users",
)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id",)
    val id: Long,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "avatar")
    val avatar: String?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "company")
    val company: String?,
    @ColumnInfo(name = "location")
    val location: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "followers")
    val followers: Int?,
    @ColumnInfo(name = "following")
    val following: Int?,
    @ColumnInfo(name = "public_repos")
    val publicRepos: Int?,
){
    fun toDomainModel() = User(id, username, avatar, name, company, location, email, followers, following, publicRepos, true)
}
