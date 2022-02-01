package com.bogdanov.tutu.di

import com.bogdanov.tutu.data.remote.network.Networking
import com.bogdanov.tutu.data.repository.UsersRepositoryImpl
import com.bogdanov.tutu.domain.repository.UsersRepository
import com.bogdanov.tutu.presentation.user_detail.UserDetailViewModel
import com.bogdanov.tutu.presentation.user_list.UsersListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<UsersRepository> { UsersRepositoryImpl(Networking.githubApi)}

    viewModel { UsersListViewModel(get()) }
    viewModel { UserDetailViewModel(get())}

}