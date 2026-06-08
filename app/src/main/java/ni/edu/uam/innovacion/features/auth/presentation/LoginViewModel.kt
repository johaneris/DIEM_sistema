package ni.edu.uam.innovacion.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.innovacion.core.network.ApiResult
import ni.edu.uam.innovacion.core.network.readableMessage
import ni.edu.uam.innovacion.data.remote.auth.AuthenticatedUserResponse
import ni.edu.uam.innovacion.data.repository.AuthRepository

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggingOut: Boolean = false,
    val user: AuthenticatedUserResponse? = null,
    val errorMessage: String? = null,
    val logoutError: String? = null,
    val sessionChecked: Boolean = false
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login(correo: String, contrasena: String) {
        if (correo.isBlank() || contrasena.isBlank()) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Ingrese su correo y contrasena para continuar.",
                    sessionChecked = true
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            when (val result = authRepository.login(correo.trim(), contrasena)) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = result.data,
                        errorMessage = null,
                        sessionChecked = true
                    )
                }

                else -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.readableMessage(),
                        sessionChecked = true
                    )
                }
            }
        }
    }

    fun checkSession() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            if (!authRepository.hasSavedToken()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = null,
                        errorMessage = null,
                        sessionChecked = true
                    )
                }
                return@launch
            }

            when (val result = authRepository.me()) {
                is ApiResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = result.data,
                        errorMessage = null,
                        sessionChecked = true
                    )
                }

                else -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = null,
                        errorMessage = null,
                        sessionChecked = true
                    )
                }
            }
        }
    }

    fun logout() {
        _uiState.update { it.copy(isLoggingOut = true, logoutError = null) }
        viewModelScope.launch {
            when (val result = authRepository.logout()) {
                is ApiResult.Success, is ApiResult.SessionExpired -> {
                    _uiState.update { LoginUiState(sessionChecked = true) }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            isLoggingOut = false,
                            logoutError = result.readableMessage()
                        )
                    }
                }
            }
        }
    }

    fun clearLogoutError() {
        _uiState.update { it.copy(logoutError = null) }
    }

    fun markSessionExpired() {
        _uiState.update { LoginUiState(sessionChecked = true) }
    }

    companion object {
        fun factory(authRepository: AuthRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(authRepository) as T
                }
            }
    }
}
