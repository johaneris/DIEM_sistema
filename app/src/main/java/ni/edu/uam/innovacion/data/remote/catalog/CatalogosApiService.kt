package ni.edu.uam.innovacion.data.remote.catalog

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CatalogosApiService {
    @GET("admin/catalog/ambitos-actividad")
    suspend fun listarAmbitosActividad(): Response<List<AmbitoActividadResponse>>

    @GET("admin/catalog/ambitos-actividad/activos")
    suspend fun listarAmbitosActividadActivos(): Response<List<AmbitoActividadResponse>>

    @GET("admin/catalog/ambitos-actividad/requieren-categoria")
    suspend fun listarAmbitosRequierenCategoria(): Response<List<AmbitoActividadResponse>>

    @GET("admin/catalog/ambitos-actividad/no-requieren-categoria")
    suspend fun listarAmbitosNoRequierenCategoria(): Response<List<AmbitoActividadResponse>>

    @GET("admin/catalog/ambitos-actividad/{id}")
    suspend fun obtenerAmbitoActividad(@Path("id") id: Long): Response<AmbitoActividadResponse>

    @POST("admin/catalog/ambitos-actividad")
    suspend fun crearAmbitoActividad(@Body request: AmbitoActividadRequest): Response<AmbitoActividadResponse>

    @PUT("admin/catalog/ambitos-actividad/{id}")
    suspend fun actualizarAmbitoActividad(
        @Path("id") id: Long,
        @Body request: AmbitoActividadRequest
    ): Response<AmbitoActividadResponse>

    @PATCH("admin/catalog/ambitos-actividad/{id}/activar")
    suspend fun activarAmbitoActividad(@Path("id") id: Long): Response<AmbitoActividadResponse>

    @PATCH("admin/catalog/ambitos-actividad/{id}/inactivar")
    suspend fun inactivarAmbitoActividad(@Path("id") id: Long): Response<AmbitoActividadResponse>

    @PATCH("admin/catalog/ambitos-actividad/{id}/archivar")
    suspend fun archivarAmbitoActividad(@Path("id") id: Long): Response<AmbitoActividadResponse>

    @GET("admin/catalog/facultades")
    suspend fun listarFacultades(): Response<List<FacultadResponse>>

    @GET("admin/catalog/facultades/activas")
    suspend fun listarFacultadesActivas(): Response<List<FacultadResponse>>

    @GET("admin/catalog/facultades/{id}")
    suspend fun obtenerFacultad(@Path("id") id: Long): Response<FacultadResponse>

    @POST("admin/catalog/facultades")
    suspend fun crearFacultad(@Body request: FacultadRequest): Response<FacultadResponse>

    @PUT("admin/catalog/facultades/{id}")
    suspend fun actualizarFacultad(
        @Path("id") id: Long,
        @Body request: FacultadRequest
    ): Response<FacultadResponse>

    @PATCH("admin/catalog/facultades/{id}/activar")
    suspend fun activarFacultad(@Path("id") id: Long): Response<FacultadResponse>

    @PATCH("admin/catalog/facultades/{id}/inactivar")
    suspend fun inactivarFacultad(@Path("id") id: Long): Response<FacultadResponse>

    @PATCH("admin/catalog/facultades/{id}/archivar")
    suspend fun archivarFacultad(@Path("id") id: Long): Response<FacultadResponse>

    @GET("admin/catalog/carreras")
    suspend fun listarCarreras(): Response<List<CarreraResponse>>

    @GET("admin/catalog/carreras/activas")
    suspend fun listarCarrerasActivas(): Response<List<CarreraResponse>>

    @GET("admin/catalog/carreras/facultad/{idFacultad}")
    suspend fun listarCarrerasPorFacultad(
        @Path("idFacultad") idFacultad: Long
    ): Response<List<CarreraResponse>>

    @GET("admin/catalog/carreras/facultad/{idFacultad}/activas")
    suspend fun listarCarrerasActivasPorFacultad(
        @Path("idFacultad") idFacultad: Long
    ): Response<List<CarreraResponse>>

    @GET("admin/catalog/carreras/{id}")
    suspend fun obtenerCarrera(@Path("id") id: Long): Response<CarreraResponse>

    @POST("admin/catalog/carreras")
    suspend fun crearCarrera(@Body request: CarreraRequest): Response<CarreraResponse>

    @PUT("admin/catalog/carreras/{id}")
    suspend fun actualizarCarrera(
        @Path("id") id: Long,
        @Body request: CarreraRequest
    ): Response<CarreraResponse>

    @PATCH("admin/catalog/carreras/{id}/activar")
    suspend fun activarCarrera(@Path("id") id: Long): Response<CarreraResponse>

    @PATCH("admin/catalog/carreras/{id}/inactivar")
    suspend fun inactivarCarrera(@Path("id") id: Long): Response<CarreraResponse>

    @PATCH("admin/catalog/carreras/{id}/archivar")
    suspend fun archivarCarrera(@Path("id") id: Long): Response<CarreraResponse>

    @GET("admin/catalog/categorias-diem")
    suspend fun listarCategoriasDiem(): Response<List<CategoriaDiemResponse>>

    @GET("admin/catalog/categorias-diem/activas")
    suspend fun listarCategoriasDiemActivas(): Response<List<CategoriaDiemResponse>>

    @GET("admin/catalog/categorias-diem/ambito/{idAmbitoActividad}")
    suspend fun listarCategoriasDiemPorAmbito(
        @Path("idAmbitoActividad") idAmbitoActividad: Long
    ): Response<List<CategoriaDiemResponse>>

    @GET("admin/catalog/categorias-diem/ambito/{idAmbitoActividad}/activas")
    suspend fun listarCategoriasDiemActivasPorAmbito(
        @Path("idAmbitoActividad") idAmbitoActividad: Long
    ): Response<List<CategoriaDiemResponse>>

    @GET("admin/catalog/categorias-diem/diem/activas")
    suspend fun listarCategoriasDiemActivasDeAmbitosDiem(): Response<List<CategoriaDiemResponse>>

    @GET("admin/catalog/categorias-diem/{id}")
    suspend fun obtenerCategoriaDiem(@Path("id") id: Long): Response<CategoriaDiemResponse>

    @POST("admin/catalog/categorias-diem")
    suspend fun crearCategoriaDiem(@Body request: CategoriaDiemRequest): Response<CategoriaDiemResponse>

    @PUT("admin/catalog/categorias-diem/{id}")
    suspend fun actualizarCategoriaDiem(
        @Path("id") id: Long,
        @Body request: CategoriaDiemRequest
    ): Response<CategoriaDiemResponse>

    @PATCH("admin/catalog/categorias-diem/{id}/activar")
    suspend fun activarCategoriaDiem(@Path("id") id: Long): Response<CategoriaDiemResponse>

    @PATCH("admin/catalog/categorias-diem/{id}/inactivar")
    suspend fun inactivarCategoriaDiem(@Path("id") id: Long): Response<CategoriaDiemResponse>

    @PATCH("admin/catalog/categorias-diem/{id}/archivar")
    suspend fun archivarCategoriaDiem(@Path("id") id: Long): Response<CategoriaDiemResponse>

    @GET("admin/catalog/fuentes-proyecto")
    suspend fun listarFuentesProyecto(): Response<List<FuenteProyectoResponse>>

    @GET("admin/catalog/fuentes-proyecto/activas")
    suspend fun listarFuentesProyectoActivas(): Response<List<FuenteProyectoResponse>>

    @GET("admin/catalog/fuentes-proyecto/categoria/{categoria}")
    suspend fun listarFuentesProyectoPorCategoria(
        @Path("categoria") categoria: String
    ): Response<List<FuenteProyectoResponse>>

    @GET("admin/catalog/fuentes-proyecto/categoria/{categoria}/activas")
    suspend fun listarFuentesProyectoActivasPorCategoria(
        @Path("categoria") categoria: String
    ): Response<List<FuenteProyectoResponse>>

    @GET("admin/catalog/fuentes-proyecto/{id}")
    suspend fun obtenerFuenteProyecto(@Path("id") id: Long): Response<FuenteProyectoResponse>

    @POST("admin/catalog/fuentes-proyecto")
    suspend fun crearFuenteProyecto(@Body request: FuenteProyectoRequest): Response<FuenteProyectoResponse>

    @PUT("admin/catalog/fuentes-proyecto/{id}")
    suspend fun actualizarFuenteProyecto(
        @Path("id") id: Long,
        @Body request: FuenteProyectoRequest
    ): Response<FuenteProyectoResponse>

    @PATCH("admin/catalog/fuentes-proyecto/{id}/activar")
    suspend fun activarFuenteProyecto(@Path("id") id: Long): Response<FuenteProyectoResponse>

    @PATCH("admin/catalog/fuentes-proyecto/{id}/inactivar")
    suspend fun inactivarFuenteProyecto(@Path("id") id: Long): Response<FuenteProyectoResponse>

    @PATCH("admin/catalog/fuentes-proyecto/{id}/archivar")
    suspend fun archivarFuenteProyecto(@Path("id") id: Long): Response<FuenteProyectoResponse>

    @GET("admin/catalog/roles")
    suspend fun listarRoles(): Response<List<RolResponse>>

    @GET("admin/catalog/roles/activos")
    suspend fun listarRolesActivos(): Response<List<RolResponse>>

    @GET("admin/catalog/roles/{id}")
    suspend fun obtenerRol(@Path("id") id: Long): Response<RolResponse>

    @POST("admin/catalog/roles")
    suspend fun crearRol(@Body request: RolRequest): Response<RolResponse>

    @PUT("admin/catalog/roles/{id}")
    suspend fun actualizarRol(
        @Path("id") id: Long,
        @Body request: RolRequest
    ): Response<RolResponse>

    @PATCH("admin/catalog/roles/{id}/activar")
    suspend fun activarRol(@Path("id") id: Long): Response<RolResponse>

    @PATCH("admin/catalog/roles/{id}/inactivar")
    suspend fun inactivarRol(@Path("id") id: Long): Response<RolResponse>

    @PATCH("admin/catalog/roles/{id}/archivar")
    suspend fun archivarRol(@Path("id") id: Long): Response<RolResponse>

    @GET("admin/catalog/roles-participacion")
    suspend fun listarRolesParticipacion(): Response<List<RolParticipacionResponse>>

    @GET("admin/catalog/roles-participacion/activos")
    suspend fun listarRolesParticipacionActivos(): Response<List<RolParticipacionResponse>>

    @GET("admin/catalog/roles-participacion/{id}")
    suspend fun obtenerRolParticipacion(@Path("id") id: Long): Response<RolParticipacionResponse>

    @POST("admin/catalog/roles-participacion")
    suspend fun crearRolParticipacion(
        @Body request: RolParticipacionRequest
    ): Response<RolParticipacionResponse>

    @PUT("admin/catalog/roles-participacion/{id}")
    suspend fun actualizarRolParticipacion(
        @Path("id") id: Long,
        @Body request: RolParticipacionRequest
    ): Response<RolParticipacionResponse>

    @PATCH("admin/catalog/roles-participacion/{id}/activar")
    suspend fun activarRolParticipacion(@Path("id") id: Long): Response<RolParticipacionResponse>

    @PATCH("admin/catalog/roles-participacion/{id}/inactivar")
    suspend fun inactivarRolParticipacion(@Path("id") id: Long): Response<RolParticipacionResponse>

    @PATCH("admin/catalog/roles-participacion/{id}/archivar")
    suspend fun archivarRolParticipacion(@Path("id") id: Long): Response<RolParticipacionResponse>
}
