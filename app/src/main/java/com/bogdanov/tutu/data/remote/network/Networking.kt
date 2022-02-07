package com.bogdanov.tutu.data.remote.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import timber.log.Timber

object Networking {
    private val okhttpClient = OkHttpClient.Builder()
        .addInterceptor (HttpLoggingInterceptor{
            Timber.tag("Network").d(it)
        }
            .setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okhttpClient)
        .build()

    val githubApi: GithubApi
        get() = retrofit.create()
}