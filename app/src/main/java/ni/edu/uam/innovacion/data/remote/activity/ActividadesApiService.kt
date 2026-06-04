package ni.edu.uam.innovacion.data.remote.activity

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ActividadesApiService {
    @GET("admin/actividades")
    suspend fun listarTodas(): Response<List<ActividadResponse>>

    @POST("admin/actividades")
    suspend fun crear(@Body request: ActividadRequest): Response<ActividadResponse>

    @GET("admin/actividades/{idActividad}")
    suspend fun obtener(@Path("idActividad") idActividad: Long): Response<ActividadResponse>

    @PUT("admin/actividades/{idActividad}")
    suspend fun actualizar(
        @Path("idActividad") idActividad: Long,
        @Body request: ActividadRequest
    ): Response<ActividadResponse>

    @PATCH("admin/actividades/{idActividad}/publicar")
    suspend fun publicar(@Path("idActividad") idActividad: Long): Response<ActividadResponse>

    @PATCH("admin/actividades/{idActividad}/iniciar")
    suspend fun iniciar(@Path("idActividad") idActividad: Long): Response<ActividadResponse>

    @PATCH("admin/actividades/{idActividad}/finalizar")
    suspend fun finalizar(@Path("idActividad") idActividad: Long): Response<ActividadResponse>

    @PATCH("admin/actividades/{idActividad}/cancelar")
    suspend fun cancelar(@Path("idActividad") idActividad: Long): Response<ActividadResponse>

    @PATCH("admin/actividades/{idActividad}/archivar")
    suspend fun archivar(@Path("idActividad") idActividad: Long): Response<ActividadResponse>

    @GET("actividades/disponibles")
    suspend fun listarDisponibles(): Response<List<ActividadResponse>>

    @GET("actividades/{idActividad}")
    suspend fun obtenerDisponible(@Path("idActividad") idActividad: Long): Response<ActividadResponse>
}
