package com.ssafy.santeut.ui.landing

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.santeut.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private var _state by mutableStateOf(UserState())
    val state = _state

    private val _uiEvent = MutableStateFlow<AuthUiEvent>(AuthUiEvent.Idle)
    val uiEvent: StateFlow<AuthUiEvent> = _uiEvent

    fun checkAuth() {
        viewModelScope.launch {
            _uiEvent.value = AuthUiEvent.Auth(false)
//            userUseCase.getToken()
//                .catch { e ->
//                    Log.d("CheckAuth", "Error: ${e.message}")
//                    _eventFlow.emit(UiEvent.UnAuth)
//                }
//                .collectLatest { data ->
//                    // 토큰 넣어 주자.
//                    _eventFlow.emit(UiEvent.Auth)
//                }
        }
    }

    @Stable
    sealed interface AuthUiEvent {
        @Immutable
        data object Idle : AuthUiEvent
        @Immutable
        data class Auth(val auth: Boolean) : AuthUiEvent
    }
}

