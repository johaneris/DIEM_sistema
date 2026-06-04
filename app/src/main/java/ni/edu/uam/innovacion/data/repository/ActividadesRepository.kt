package ni.edu.uam.innovacion.data.repository

import ni.edu.uam.innovacion.core.network.ApiResult
import ni.edu.uam.innovacion.core.network.safeApiCall
import ni.edu.uam.innovacion.core.session.AuthTokenStore
import ni.edu.uam.innovacion.data.remote.activity.ActividadRequest
import ni.edu.uam.innovacion.data.remote.activity.ActividadResponse
import ni.edu.uam.innovacion.data.remote.activity.ActividadesApiService

class ActividadesRepository(
    private val api: ActividadesApiService,
    private val tokenStore: AuthTokenStore
) {
    suspend fun listarTodas(): ApiResult<List<ActividadResponse>> =
        safeApiCall(tokenStore) { api.listarTodas() }

    suspend fun listarDisponibles(): ApiResult<List<ActividadResponse>> =
        safeApiCall { api.listarDisponibles() }

    suspend fun obtener(idActividad: Long): ApiResult<ActividadResponse> =
        safeApiCall(tokenStore) { api.obtener(idActividad) }

    suspend fun obtenerDisponible(idActividad: Long): ApiResult<ActividadResponse> =
        safeApiCall { api.obtenerDisponible(idActividad) }

    suspend fun crear(request: ActividadRequest): ApiResult<ActividadResponse> =
        safeApiCall(tokenStore) { api.crear(request) }

    suspend fun actualizar(
        idActividad: Long,
        request: ActividadRequest
    ): ApiResult<ActividadResponse> =
        safeApiCall(tokenStore) { api.actualizar(idActividad, request) }

    suspend fun publicar(idActividad: Long): ApiResult<ActividadResponse> =
        safeApiCall(tokenStore) { api.publicar(idActividad) }

    suspend fun iniciar(idActividad: Long): ApiResult<ActividadResponse> =
        safeApiCall(tokenStore) { api.iniciar(idActividad) }

    suspend fun finalizar(idActividad: Long): ApiResult<ActividadResponse> =
        safeApiCall(tokenStore) { api.finalizar(idActividad) }

    suspend fun cancelar(idActividad: Long): ApiResult<ActividadResponse> =
        safeApiCall(tokenStore) { api.cancelar(idActividad) }

    suspend fun archivar(idActividad: Long): ApiResult<ActividadResponse> =
        safeApiCall(tokenStore) { api.archivar(idActividad) }
}
