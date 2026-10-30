package com.example.tugas10mvvm.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tugas10mvvm.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val usernameInput: String = "",
    val passwordInput: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface LoginEvent {
    data class ShowMessage(val message: String, val isSuccess: Boolean) : LoginEvent
}

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow: SharedFlow<LoginEvent> = _eventFlow.asSharedFlow()

    fun onUsernameChanged(username: String) {
        _uiState.update { it.copy(usernameInput = username) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(passwordInput = password) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        val username = uiState.value.usernameInput.trim()
        val password = uiState.value.passwordInput.trim()

        if (username.isEmpty() || password.isEmpty()) {
            viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowMessage("Username dan Password tidak boleh kosong!", isSuccess = false))
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val user = userRepository.getUserByUsername(username)
                if (user == null) {
                    _eventFlow.emit(LoginEvent.ShowMessage("User tidak ditemukan!", isSuccess = false))
                } else if (user.password != password) {
                    _eventFlow.emit(LoginEvent.ShowMessage("Password salah!", isSuccess = false))
                } else {
                    _eventFlow.emit(LoginEvent.ShowMessage("Login Berhasil! Selamat datang $username.", isSuccess = true))
                }
            } catch (e: Exception) {
                _eventFlow.emit(LoginEvent.ShowMessage("Terjadi kesalahan: ${e.message}", isSuccess = false))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

class LoginViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
