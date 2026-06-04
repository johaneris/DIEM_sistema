package ni.edu.uam.innovacion.data.remote.user

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuariosApiService {
    @GET("usuarios")
    suspend fun listarUsuarios(): Response<List<UsuarioResponse>>

    @POST("usuarios")
    suspend fun crearUsuario(@Body request: CrearUsuarioRequest): Response<UsuarioResponse>

    @GET("usuarios/{idUsuario}")
    suspend fun obtenerUsuario(@Path("idUsuario") idUsuario: Long): Response<UsuarioResponse>

    @PUT("usuarios/{idUsuario}")
    suspend fun actualizarUsuario(
        @Path("idUsuario") idUsuario: Long,
        @Body request: ActualizarUsuarioRequest
    ): Response<UsuarioResponse>

    @PATCH("usuarios/{idUsuario}/contrasena")
    suspend fun cambiarContrasena(
        @Path("idUsuario") idUsuario: Long,
        @Body request: CambiarContrasenaRequest
    ): Response<UsuarioResponse>

    @POST("usuarios/{idUsuario}/perfiles/estudiante")
    suspend fun crearPerfilEstudiante(
        @Path("idUsuario") idUsuario: Long,
        @Body request: PerfilEstudianteRequest
    ): Response<PerfilEstudianteResponse>

    @GET("usuarios/{idUsuario}/perfiles/estudiante")
    suspend fun obtenerPerfilEstudiante(
        @Path("idUsuario") idUsuario: Long
    ): Response<PerfilEstudianteResponse>

    @PUT("usuarios/{idUsuario}/perfiles/estudiante")
    suspend fun actualizarPerfilEstudiante(
        @Path("idUsuario") idUsuario: Long,
        @Body request: PerfilEstudianteRequest
    ): Response<PerfilEstudianteResponse>

    @GET("usuarios/{idUsuario}/perfiles/estudiante/doble-titulaciones")
    suspend fun listarDobleTitulaciones(
        @Path("idUsuario") idUsuario: Long
    ): Response<List<DobleTitulacionResponse>>

    @POST("usuarios/{idUsuario}/perfiles/estudiante/doble-titulaciones")
    suspend fun crearDobleTitulacion(
        @Path("idUsuario") idUsuario: Long,
        @Body request: DobleTitulacionRequest
    ): Response<DobleTitulacionResponse>

    @DELETE("usuarios/{idUsuario}/perfiles/estudiante/doble-titulaciones/{idDobleTitulacion}")
    suspend fun eliminarDobleTitulacion(
        @Path("idUsuario") idUsuario: Long,
        @Path("idDobleTitulacion") idDobleTitulacion: Long
    ): Response<Unit>

    @POST("usuarios/{idUsuario}/perfiles/administrador")
    suspend fun crearPerfilAdministrador(
        @Path("idUsuario") idUsuario: Long,
        @Body request: PerfilAdministradorRequest
    ): Response<PerfilAdministradorResponse>

    @GET("usuarios/{idUsuario}/perfiles/administrador")
    suspend fun obtenerPerfilAdministrador(
        @Path("idUsuario") idUsuario: Long
    ): Response<PerfilAdministradorResponse>

    @POST("usuarios/{idUsuario}/perfiles/docente")
    suspend fun crearPerfilDocente(
        @Path("idUsuario") idUsuario: Long,
        @Body request: PerfilDocenteRequest
    ): Response<PerfilDocenteResponse>

    @GET("usuarios/{idUsuario}/perfiles/docente")
    suspend fun obtenerPerfilDocente(
        @Path("idUsuario") idUsuario: Long
    ): Response<PerfilDocenteResponse>

    @POST("usuarios/{idUsuario}/perfiles/mentor")
    suspend fun crearPerfilMentor(
        @Path("idUsuario") idUsuario: Long,
        @Body request: PerfilMentorRequest
    ): Response<PerfilMentorResponse>

    @GET("usuarios/{idUsuario}/perfiles/mentor")
    suspend fun obtenerPerfilMentor(
        @Path("idUsuario") idUsuario: Long
    ): Response<PerfilMentorResponse>

    @POST("usuarios/{idUsuario}/perfiles/participante-externo")
    suspend fun crearPerfilParticipanteExterno(
        @Path("idUsuario") idUsuario: Long,
        @Body request: PerfilParticipanteExternoRequest
    ): Response<PerfilParticipanteExternoResponse>

    @GET("usuarios/{idUsuario}/perfiles/participante-externo")
    suspend fun obtenerPerfilParticipanteExterno(
        @Path("idUsuario") idUsuario: Long
    ): Response<PerfilParticipanteExternoResponse>
}
