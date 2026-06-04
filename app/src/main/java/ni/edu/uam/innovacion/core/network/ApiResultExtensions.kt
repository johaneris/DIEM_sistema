package ni.edu.uam.innovacion.core.network

fun ApiResult<*>.readableMessage(): String = when (this) {
    is ApiResult.HttpError -> message
    is ApiResult.NetworkError -> message
    is ApiResult.SessionExpired -> message
    is ApiResult.UnknownError -> message
    is ApiResult.Success -> "Operacion completada."
}

fun <T> ApiResult<T>.getOrNull(): T? = when (this) {
    is ApiResult.Success -> data
    else -> null
}

fun ApiResult<*>.isSessionExpired(): Boolean = this is ApiResult.SessionExpired
