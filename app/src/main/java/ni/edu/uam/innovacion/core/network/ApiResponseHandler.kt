package ni.edu.uam.innovacion.core.network

import com.google.gson.Gson
import java.io.IOException
import ni.edu.uam.innovacion.core.session.AuthTokenStore
import retrofit2.Response

suspend fun <T> safeApiCall(
    tokenStore: AuthTokenStore? = null,
    call: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                @Suppress("UNCHECKED_CAST")
                ApiResult.Success(Unit as T)
            }
        } else {
            val error = parseErrorResponse(response)
            if (response.code() == 401) {
                tokenStore?.clear()
                ApiResult.SessionExpired(error?.message ?: "Sesion expirada. Inicie sesion nuevamente.")
            } else {
                ApiResult.HttpError(
                    code = response.code(),
                    message = error?.message ?: response.message().ifBlank { "Error HTTP ${response.code()}" },
                    error = error
                )
            }
        }
    } catch (exception: IOException) {
        ApiResult.NetworkError(
            message = exception.message ?: "No se pudo conectar con el backend.",
            cause = exception
        )
    } catch (exception: Exception) {
        ApiResult.UnknownError(
            message = exception.message ?: "Ocurrio un error inesperado.",
            cause = exception
        )
    }
}

private fun <T> parseErrorResponse(response: Response<T>): ErrorResponse? {
    val errorBody = response.errorBody()?.string() ?: return null
    return runCatching {
        Gson().fromJson(errorBody, ErrorResponse::class.java)
    }.getOrNull()
}
