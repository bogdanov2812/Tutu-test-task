package com.bogdanov.tutu.presentation.user_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdanov.tutu.domain.models.User
import com.bogdanov.tutu.domain.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val repository: UsersRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<User?>(null)
    val userInfo: StateFlow<User?> = _userInfo

    fun getUserInfo(username: String){
        viewModelScope.launch {
            repository.getUserInfo(username).collectLatest {
                _userInfo.emit(it)
            }
        }

    }

}