package ni.edu.uam.innovacion.features.catalog.data.remote

import ni.edu.uam.innovacion.features.catalog.data.model.*
import retrofit2.http.*

interface CatalogApiService {
    // Roles
    @GET("api/admin/catalog/roles")
    suspend fun listarRoles(): List<RolResponse>

    @POST("api/admin/catalog/roles")
    suspend fun crearRol(@Body request: RolRequest): RolResponse

    @PUT("api/admin/catalog/roles/{id}")
    suspend fun actualizarRol(@Path("id") id: Long, @Body request: RolRequest): RolResponse

    @PATCH("api/admin/catalog/roles/{id}/activar")
    suspend fun activarRol(@Path("id") id: Long): RolResponse

    @PATCH("api/admin/catalog/roles/{id}/inactivar")
    suspend fun inactivarRol(@Path("id") id: Long): RolResponse

    @PATCH("api/admin/catalog/roles/{id}/archivar")
    suspend fun archivarRol(@Path("id") id: Long): RolResponse

    // Facultades
    @GET("api/admin/catalog/facultades")
    suspend fun listarFacultades(): List<FacultadResponse>

    @POST("api/admin/catalog/facultades")
    suspend fun crearFacultad(@Body request: FacultadRequest): FacultadResponse

    @PUT("api/admin/catalog/facultades/{id}")
    suspend fun actualizarFacultad(@Path("id") id: Long, @Body request: FacultadRequest): FacultadResponse

    @PATCH("api/admin/catalog/facultades/{id}/activar")
    suspend fun activarFacultad(@Path("id") id: Long): FacultadResponse

    @PATCH("api/admin/catalog/facultades/{id}/inactivar")
    suspend fun inactivarFacultad(@Path("id") id: Long): FacultadResponse

    @PATCH("api/admin/catalog/facultades/{id}/archivar")
    suspend fun archivarFacultad(@Path("id") id: Long): FacultadResponse

    // Carreras
    @GET("api/admin/catalog/carreras")
    suspend fun listarCarreras(): List<CarreraResponse>

    @GET("api/admin/catalog/carreras/facultad/{idFacultad}")
    suspend fun listarCarrerasPorFacultad(@Path("idFacultad") idFacultad: Long): List<CarreraResponse>

    @POST("api/admin/catalog/carreras")
    suspend fun crearCarrera(@Body request: CarreraRequest): CarreraResponse

    @PUT("api/admin/catalog/carreras/{id}")
    suspend fun actualizarCarrera(@Path("id") id: Long, @Body request: CarreraRequest): CarreraResponse

    @PATCH("api/admin/catalog/carreras/{id}/activar")
    suspend fun activarCarrera(@Path("id") id: Long): CarreraResponse

    @PATCH("api/admin/catalog/carreras/{id}/inactivar")
    suspend fun inactivarCarrera(@Path("id") id: Long): CarreraResponse

    @PATCH("api/admin/catalog/carreras/{id}/archivar")
    suspend fun archivarCarrera(@Path("id") id: Long): CarreraResponse
}