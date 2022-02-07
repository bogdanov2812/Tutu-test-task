package com.bogdanov.tutu.presentation.user_list

import androidx.lifecycle.*
import androidx.paging.*
import com.bogdanov.tutu.data.remote.models.UserDto
import com.bogdanov.tutu.data.remote.paging_source.UsersPagingSource
import com.bogdanov.tutu.domain.models.User
import com.bogdanov.tutu.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest


class UsersListViewModel(
    private val repository: UsersRepository
) : ViewModel() {

    val usersFlow: Flow<PagingData<User>>

    private val query = MutableLiveData("")

    init {
        usersFlow = query.asFlow()
            .flatMapLatest {
                repository.searchUsers(it)

            }
            .cachedIn(viewModelScope)

    }


    fun searchBy(value: String){
        this.query.value = value
    }
}