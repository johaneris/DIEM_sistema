package ni.edu.uam.innovacion.data.repository

import ni.edu.uam.innovacion.core.network.ApiResult
import ni.edu.uam.innovacion.core.network.safeApiCall
import ni.edu.uam.innovacion.core.session.AuthTokenStore
import ni.edu.uam.innovacion.data.remote.user.ActualizarUsuarioRequest
import ni.edu.uam.innovacion.data.remote.user.AsignarRolRequest
import ni.edu.uam.innovacion.data.remote.user.CambiarContrasenaRequest
import ni.edu.uam.innovacion.data.remote.user.CrearUsuarioRequest
import ni.edu.uam.innovacion.data.remote.user.DobleTitulacionRequest
import ni.edu.uam.innovacion.data.remote.user.DobleTitulacionResponse
import ni.edu.uam.innovacion.data.remote.user.PerfilAdministradorRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilAdministradorResponse
import ni.edu.uam.innovacion.data.remote.user.PerfilDocenteRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilDocenteResponse
import ni.edu.uam.innovacion.data.remote.user.PerfilEstudianteRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilEstudianteResponse
import ni.edu.uam.innovacion.data.remote.user.PerfilMentorRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilMentorResponse
import ni.edu.uam.innovacion.data.remote.user.PerfilParticipanteExternoRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilParticipanteExternoResponse
import ni.edu.uam.innovacion.data.remote.user.UsuarioResponse
import ni.edu.uam.innovacion.data.remote.user.UsuariosApiService

class UsuariosRepository(
    private val api: UsuariosApiService,
    private val tokenStore: AuthTokenStore
) {
    suspend fun asignarRol(idUsuario: Long, nombreRol: String): ApiResult<UsuarioResponse> =
        safeApiCall(tokenStore) { api.asignarRol(idUsuario, AsignarRolRequest(nombreRol)) }

    suspend fun listarUsuarios(): ApiResult<List<UsuarioResponse>> =
        safeApiCall(tokenStore) { api.listarUsuarios() }

    suspend fun crearUsuario(request: CrearUsuarioRequest): ApiResult<UsuarioResponse> =
        safeApiCall(tokenStore) { api.crearUsuario(request) }

    suspend fun obtenerUsuario(idUsuario: Long): ApiResult<UsuarioResponse> =
        safeApiCall(tokenStore) { api.obtenerUsuario(idUsuario) }

    suspend fun actualizarUsuario(
        idUsuario: Long,
        request: ActualizarUsuarioRequest
    ): ApiResult<UsuarioResponse> =
        safeApiCall(tokenStore) { api.actualizarUsuario(idUsuario, request) }

    suspend fun cambiarContrasena(
        idUsuario: Long,
        request: CambiarContrasenaRequest
    ): ApiResult<UsuarioResponse> =
        safeApiCall(tokenStore) { api.cambiarContrasena(idUsuario, request) }

    suspend fun crearPerfilEstudiante(
        idUsuario: Long,
        request: PerfilEstudianteRequest
    ): ApiResult<PerfilEstudianteResponse> =
        safeApiCall(tokenStore) { api.crearPerfilEstudiante(idUsuario, request) }

    suspend fun obtenerPerfilEstudiante(idUsuario: Long): ApiResult<PerfilEstudianteResponse> =
        safeApiCall(tokenStore) { api.obtenerPerfilEstudiante(idUsuario) }

    suspend fun actualizarPerfilEstudiante(
        idUsuario: Long,
        request: PerfilEstudianteRequest
    ): ApiResult<PerfilEstudianteResponse> =
        safeApiCall(tokenStore) { api.actualizarPerfilEstudiante(idUsuario, request) }

    suspend fun listarDobleTitulaciones(idUsuario: Long): ApiResult<List<DobleTitulacionResponse>> =
        safeApiCall(tokenStore) { api.listarDobleTitulaciones(idUsuario) }

    suspend fun crearDobleTitulacion(
        idUsuario: Long,
        request: DobleTitulacionRequest
    ): ApiResult<DobleTitulacionResponse> =
        safeApiCall(tokenStore) { api.crearDobleTitulacion(idUsuario, request) }

    suspend fun eliminarDobleTitulacion(
        idUsuario: Long,
        idDobleTitulacion: Long
    ): ApiResult<Unit> =
        safeApiCall(tokenStore) { api.eliminarDobleTitulacion(idUsuario, idDobleTitulacion) }

    suspend fun crearPerfilAdministrador(
        idUsuario: Long,
        request: PerfilAdministradorRequest
    ): ApiResult<PerfilAdministradorResponse> =
        safeApiCall(tokenStore) { api.crearPerfilAdministrador(idUsuario, request) }

    suspend fun obtenerPerfilAdministrador(idUsuario: Long): ApiResult<PerfilAdministradorResponse> =
        safeApiCall(tokenStore) { api.obtenerPerfilAdministrador(idUsuario) }

    suspend fun crearPerfilDocente(
        idUsuario: Long,
        request: PerfilDocenteRequest
    ): ApiResult<PerfilDocenteResponse> =
        safeApiCall(tokenStore) { api.crearPerfilDocente(idUsuario, request) }

    suspend fun obtenerPerfilDocente(idUsuario: Long): ApiResult<PerfilDocenteResponse> =
        safeApiCall(tokenStore) { api.obtenerPerfilDocente(idUsuario) }

    suspend fun crearPerfilMentor(
        idUsuario: Long,
        request: PerfilMentorRequest
    ): ApiResult<PerfilMentorResponse> =
        safeApiCall(tokenStore) { api.crearPerfilMentor(idUsuario, request) }

    suspend fun obtenerPerfilMentor(idUsuario: Long): ApiResult<PerfilMentorResponse> =
        safeApiCall(tokenStore) { api.obtenerPerfilMentor(idUsuario) }

    suspend fun crearPerfilParticipanteExterno(
        idUsuario: Long,
        request: PerfilParticipanteExternoRequest
    ): ApiResult<PerfilParticipanteExternoResponse> =
        safeApiCall(tokenStore) { api.crearPerfilParticipanteExterno(idUsuario, request) }

    suspend fun obtenerPerfilParticipanteExterno(
        idUsuario: Long
    ): ApiResult<PerfilParticipanteExternoResponse> =
        safeApiCall(tokenStore) { api.obtenerPerfilParticipanteExterno(idUsuario) }
}
