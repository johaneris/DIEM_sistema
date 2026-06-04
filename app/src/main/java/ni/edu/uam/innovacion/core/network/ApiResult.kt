package ni.edu.uam.innovacion.core.network

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class HttpError(
        val code: Int,
        val message: String,
        val error: ErrorResponse? = null
    ) : ApiResult<Nothing>()

    data class NetworkError(
        val message: String,
        val cause: Throwable? = null
    ) : ApiResult<Nothing>()

    data class SessionExpired(
        val message: String = "Sesion expirada. Inicie sesion nuevamente."
    ) : ApiResult<Nothing>()

    data class UnknownError(
        val message: String,
        val cause: Throwable? = null
    ) : ApiResult<Nothing>()
}
