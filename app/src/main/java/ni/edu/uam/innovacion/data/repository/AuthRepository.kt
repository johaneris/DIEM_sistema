package ni.edu.uam.innovacion.data.repository

import kotlinx.coroutines.flow.Flow
import ni.edu.uam.innovacion.core.network.ApiResult
import ni.edu.uam.innovacion.core.network.safeApiCall
import ni.edu.uam.innovacion.core.session.AuthTokenStore
import ni.edu.uam.innovacion.data.remote.auth.AuthApiService
import ni.edu.uam.innovacion.data.remote.auth.AuthenticatedUserResponse
import ni.edu.uam.innovacion.data.remote.auth.LoginRequest

class AuthRepository(
    private val api: AuthApiService,
    private val tokenStore: AuthTokenStore
) {
    val accessToken: Flow<String?> = tokenStore.accessToken

    suspend fun login(correo: String, contrasena: String): ApiResult<AuthenticatedUserResponse> {
        val loginResult = safeApiCall { api.login(LoginRequest(correo, contrasena)) }
        when (loginResult) {
            is ApiResult.HttpError -> return loginResult
            is ApiResult.NetworkError -> return loginResult
            is ApiResult.SessionExpired -> return loginResult
            is ApiResult.UnknownError -> return loginResult
            is ApiResult.Success -> Unit
        }

        tokenStore.saveAccessToken(loginResult.data.accessToken)
        val authenticatedUser = me()
        if (authenticatedUser !is ApiResult.Success) {
            tokenStore.clear()
        }
        return authenticatedUser
    }

    suspend fun me(): ApiResult<AuthenticatedUserResponse> {
        val result = safeApiCall(tokenStore) { api.me() }
        if (result is ApiResult.Success) {
            val roles = result.data.roles.map { it.trim().lowercase() }
            if (ADMIN_ROLE !in roles) {
                tokenStore.clear()
                val rolesRecibidos = roles.ifEmpty { listOf("sin roles") }.joinToString()
                return ApiResult.HttpError(
                    code = 403,
                    message = "La sesion activa no pertenece a un administrador. Roles recibidos: $rolesRecibidos. Se esperaba: $ADMIN_ROLE."
                )
            }
        }
        return result
    }

    suspend fun hasSavedToken(): Boolean = !tokenStore.getAccessToken().isNullOrBlank()

    suspend fun logout() {
        tokenStore.clear()
    }

    private companion object {
        const val ADMIN_ROLE = "administrador"
    }
}
