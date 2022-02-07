package com.bogdanov.tutu.di

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.bogdanov.tutu.data.local.database.ApplicationDatabase
import com.bogdanov.tutu.data.local.database.Database
import com.bogdanov.tutu.data.remote.network.Networking
import com.bogdanov.tutu.data.repository.UsersRepositoryImpl
import com.bogdanov.tutu.domain.repository.UsersRepository
import com.bogdanov.tutu.presentation.user_detail.UserDetailViewModel
import com.bogdanov.tutu.presentation.user_list.UsersListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalPagingApi
val appModule = module {

    single<UsersRepository> { UsersRepositoryImpl(Networking.githubApi, Database.instance.userDao(), Database.instance)}

    viewModel { UsersListViewModel(get()) }
    viewModel { UserDetailViewModel(get())}

}