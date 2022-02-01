package com.bogdanov.tutu.data.remote.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemsWrapper<T>(
    val items: List<T>
)
