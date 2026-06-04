package ni.edu.uam.innovacion.data.repository

import ni.edu.uam.innovacion.core.network.ApiResult
import ni.edu.uam.innovacion.core.network.safeApiCall
import ni.edu.uam.innovacion.core.session.AuthTokenStore
import ni.edu.uam.innovacion.data.remote.catalog.AmbitoActividadRequest
import ni.edu.uam.innovacion.data.remote.catalog.AmbitoActividadResponse
import ni.edu.uam.innovacion.data.remote.catalog.CarreraRequest
import ni.edu.uam.innovacion.data.remote.catalog.CarreraResponse
import ni.edu.uam.innovacion.data.remote.catalog.CategoriaDiemRequest
import ni.edu.uam.innovacion.data.remote.catalog.CategoriaDiemResponse
import ni.edu.uam.innovacion.data.remote.catalog.CatalogosApiService
import ni.edu.uam.innovacion.data.remote.catalog.FacultadRequest
import ni.edu.uam.innovacion.data.remote.catalog.FacultadResponse
import ni.edu.uam.innovacion.data.remote.catalog.FuenteProyectoRequest
import ni.edu.uam.innovacion.data.remote.catalog.FuenteProyectoResponse
import ni.edu.uam.innovacion.data.remote.catalog.RolParticipacionRequest
import ni.edu.uam.innovacion.data.remote.catalog.RolParticipacionResponse
import ni.edu.uam.innovacion.data.remote.catalog.RolRequest
import ni.edu.uam.innovacion.data.remote.catalog.RolResponse

class CatalogosRepository(
    private val api: CatalogosApiService,
    private val tokenStore: AuthTokenStore
) {
    suspend fun listarAmbitosActividad(): ApiResult<List<AmbitoActividadResponse>> =
        safeApiCall(tokenStore) { api.listarAmbitosActividad() }

    suspend fun listarAmbitosActividadActivos(): ApiResult<List<AmbitoActividadResponse>> =
        safeApiCall(tokenStore) { api.listarAmbitosActividadActivos() }

    suspend fun listarAmbitosRequierenCategoria(): ApiResult<List<AmbitoActividadResponse>> =
        safeApiCall(tokenStore) { api.listarAmbitosRequierenCategoria() }

    suspend fun listarAmbitosNoRequierenCategoria(): ApiResult<List<AmbitoActividadResponse>> =
        safeApiCall(tokenStore) { api.listarAmbitosNoRequierenCategoria() }

    suspend fun obtenerAmbitoActividad(id: Long): ApiResult<AmbitoActividadResponse> =
        safeApiCall(tokenStore) { api.obtenerAmbitoActividad(id) }

    suspend fun crearAmbitoActividad(
        request: AmbitoActividadRequest
    ): ApiResult<AmbitoActividadResponse> =
        safeApiCall(tokenStore) { api.crearAmbitoActividad(request) }

    suspend fun actualizarAmbitoActividad(
        id: Long,
        request: AmbitoActividadRequest
    ): ApiResult<AmbitoActividadResponse> =
        safeApiCall(tokenStore) { api.actualizarAmbitoActividad(id, request) }

    suspend fun activarAmbitoActividad(id: Long): ApiResult<AmbitoActividadResponse> =
        safeApiCall(tokenStore) { api.activarAmbitoActividad(id) }

    suspend fun inactivarAmbitoActividad(id: Long): ApiResult<AmbitoActividadResponse> =
        safeApiCall(tokenStore) { api.inactivarAmbitoActividad(id) }

    suspend fun archivarAmbitoActividad(id: Long): ApiResult<AmbitoActividadResponse> =
        safeApiCall(tokenStore) { api.archivarAmbitoActividad(id) }

    suspend fun listarFacultades(): ApiResult<List<FacultadResponse>> =
        safeApiCall(tokenStore) { api.listarFacultades() }

    suspend fun listarFacultadesActivas(): ApiResult<List<FacultadResponse>> =
        safeApiCall(tokenStore) { api.listarFacultadesActivas() }

    suspend fun obtenerFacultad(id: Long): ApiResult<FacultadResponse> =
        safeApiCall(tokenStore) { api.obtenerFacultad(id) }

    suspend fun crearFacultad(request: FacultadRequest): ApiResult<FacultadResponse> =
        safeApiCall(tokenStore) { api.crearFacultad(request) }

    suspend fun actualizarFacultad(
        id: Long,
        request: FacultadRequest
    ): ApiResult<FacultadResponse> =
        safeApiCall(tokenStore) { api.actualizarFacultad(id, request) }

    suspend fun activarFacultad(id: Long): ApiResult<FacultadResponse> =
        safeApiCall(tokenStore) { api.activarFacultad(id) }

    suspend fun inactivarFacultad(id: Long): ApiResult<FacultadResponse> =
        safeApiCall(tokenStore) { api.inactivarFacultad(id) }

    suspend fun archivarFacultad(id: Long): ApiResult<FacultadResponse> =
        safeApiCall(tokenStore) { api.archivarFacultad(id) }

    suspend fun listarCarreras(): ApiResult<List<CarreraResponse>> =
        safeApiCall(tokenStore) { api.listarCarreras() }

    suspend fun listarCarrerasActivas(): ApiResult<List<CarreraResponse>> =
        safeApiCall(tokenStore) { api.listarCarrerasActivas() }

    suspend fun listarCarrerasPorFacultad(idFacultad: Long): ApiResult<List<CarreraResponse>> =
        safeApiCall(tokenStore) { api.listarCarrerasPorFacultad(idFacultad) }

    suspend fun listarCarrerasActivasPorFacultad(
        idFacultad: Long
    ): ApiResult<List<CarreraResponse>> =
        safeApiCall(tokenStore) { api.listarCarrerasActivasPorFacultad(idFacultad) }

    suspend fun obtenerCarrera(id: Long): ApiResult<CarreraResponse> =
        safeApiCall(tokenStore) { api.obtenerCarrera(id) }

    suspend fun crearCarrera(request: CarreraRequest): ApiResult<CarreraResponse> =
        safeApiCall(tokenStore) { api.crearCarrera(request) }

    suspend fun actualizarCarrera(
        id: Long,
        request: CarreraRequest
    ): ApiResult<CarreraResponse> =
        safeApiCall(tokenStore) { api.actualizarCarrera(id, request) }

    suspend fun activarCarrera(id: Long): ApiResult<CarreraResponse> =
        safeApiCall(tokenStore) { api.activarCarrera(id) }

    suspend fun inactivarCarrera(id: Long): ApiResult<CarreraResponse> =
        safeApiCall(tokenStore) { api.inactivarCarrera(id) }

    suspend fun archivarCarrera(id: Long): ApiResult<CarreraResponse> =
        safeApiCall(tokenStore) { api.archivarCarrera(id) }

    suspend fun listarCategoriasDiem(): ApiResult<List<CategoriaDiemResponse>> =
        safeApiCall(tokenStore) { api.listarCategoriasDiem() }

    suspend fun listarCategoriasDiemActivas(): ApiResult<List<CategoriaDiemResponse>> =
        safeApiCall(tokenStore) { api.listarCategoriasDiemActivas() }

    suspend fun listarCategoriasDiemPorAmbito(
        idAmbitoActividad: Long
    ): ApiResult<List<CategoriaDiemResponse>> =
        safeApiCall(tokenStore) { api.listarCategoriasDiemPorAmbito(idAmbitoActividad) }

    suspend fun listarCategoriasDiemActivasPorAmbito(
        idAmbitoActividad: Long
    ): ApiResult<List<CategoriaDiemResponse>> =
        safeApiCall(tokenStore) { api.listarCategoriasDiemActivasPorAmbito(idAmbitoActividad) }

    suspend fun listarCategoriasDiemActivasDeAmbitosDiem(): ApiResult<List<CategoriaDiemResponse>> =
        safeApiCall(tokenStore) { api.listarCategoriasDiemActivasDeAmbitosDiem() }

    suspend fun obtenerCategoriaDiem(id: Long): ApiResult<CategoriaDiemResponse> =
        safeApiCall(tokenStore) { api.obtenerCategoriaDiem(id) }

    suspend fun crearCategoriaDiem(
        request: CategoriaDiemRequest
    ): ApiResult<CategoriaDiemResponse> =
        safeApiCall(tokenStore) { api.crearCategoriaDiem(request) }

    suspend fun actualizarCategoriaDiem(
        id: Long,
        request: CategoriaDiemRequest
    ): ApiResult<CategoriaDiemResponse> =
        safeApiCall(tokenStore) { api.actualizarCategoriaDiem(id, request) }

    suspend fun activarCategoriaDiem(id: Long): ApiResult<CategoriaDiemResponse> =
        safeApiCall(tokenStore) { api.activarCategoriaDiem(id) }

    suspend fun inactivarCategoriaDiem(id: Long): ApiResult<CategoriaDiemResponse> =
        safeApiCall(tokenStore) { api.inactivarCategoriaDiem(id) }

    suspend fun archivarCategoriaDiem(id: Long): ApiResult<CategoriaDiemResponse> =
        safeApiCall(tokenStore) { api.archivarCategoriaDiem(id) }

    suspend fun listarFuentesProyecto(): ApiResult<List<FuenteProyectoResponse>> =
        safeApiCall(tokenStore) { api.listarFuentesProyecto() }

    suspend fun listarFuentesProyectoActivas(): ApiResult<List<FuenteProyectoResponse>> =
        safeApiCall(tokenStore) { api.listarFuentesProyectoActivas() }

    suspend fun listarFuentesProyectoPorCategoria(
        categoria: String
    ): ApiResult<List<FuenteProyectoResponse>> =
        safeApiCall(tokenStore) { api.listarFuentesProyectoPorCategoria(categoria) }

    suspend fun listarFuentesProyectoActivasPorCategoria(
        categoria: String
    ): ApiResult<List<FuenteProyectoResponse>> =
        safeApiCall(tokenStore) { api.listarFuentesProyectoActivasPorCategoria(categoria) }

    suspend fun obtenerFuenteProyecto(id: Long): ApiResult<FuenteProyectoResponse> =
        safeApiCall(tokenStore) { api.obtenerFuenteProyecto(id) }

    suspend fun crearFuenteProyecto(
        request: FuenteProyectoRequest
    ): ApiResult<FuenteProyectoResponse> =
        safeApiCall(tokenStore) { api.crearFuenteProyecto(request) }

    suspend fun actualizarFuenteProyecto(
        id: Long,
        request: FuenteProyectoRequest
    ): ApiResult<FuenteProyectoResponse> =
        safeApiCall(tokenStore) { api.actualizarFuenteProyecto(id, request) }

    suspend fun activarFuenteProyecto(id: Long): ApiResult<FuenteProyectoResponse> =
        safeApiCall(tokenStore) { api.activarFuenteProyecto(id) }

    suspend fun inactivarFuenteProyecto(id: Long): ApiResult<FuenteProyectoResponse> =
        safeApiCall(tokenStore) { api.inactivarFuenteProyecto(id) }

    suspend fun archivarFuenteProyecto(id: Long): ApiResult<FuenteProyectoResponse> =
        safeApiCall(tokenStore) { api.archivarFuenteProyecto(id) }

    suspend fun listarRoles(): ApiResult<List<RolResponse>> =
        safeApiCall(tokenStore) { api.listarRoles() }

    suspend fun listarRolesActivos(): ApiResult<List<RolResponse>> =
        safeApiCall(tokenStore) { api.listarRolesActivos() }

    suspend fun obtenerRol(id: Long): ApiResult<RolResponse> =
        safeApiCall(tokenStore) { api.obtenerRol(id) }

    suspend fun crearRol(request: RolRequest): ApiResult<RolResponse> =
        safeApiCall(tokenStore) { api.crearRol(request) }

    suspend fun actualizarRol(
        id: Long,
        request: RolRequest
    ): ApiResult<RolResponse> =
        safeApiCall(tokenStore) { api.actualizarRol(id, request) }

    suspend fun activarRol(id: Long): ApiResult<RolResponse> =
        safeApiCall(tokenStore) { api.activarRol(id) }

    suspend fun inactivarRol(id: Long): ApiResult<RolResponse> =
        safeApiCall(tokenStore) { api.inactivarRol(id) }

    suspend fun archivarRol(id: Long): ApiResult<RolResponse> =
        safeApiCall(tokenStore) { api.archivarRol(id) }

    suspend fun listarRolesParticipacion(): ApiResult<List<RolParticipacionResponse>> =
        safeApiCall(tokenStore) { api.listarRolesParticipacion() }

    suspend fun listarRolesParticipacionActivos(): ApiResult<List<RolParticipacionResponse>> =
        safeApiCall(tokenStore) { api.listarRolesParticipacionActivos() }

    suspend fun obtenerRolParticipacion(id: Long): ApiResult<RolParticipacionResponse> =
        safeApiCall(tokenStore) { api.obtenerRolParticipacion(id) }

    suspend fun crearRolParticipacion(
        request: RolParticipacionRequest
    ): ApiResult<RolParticipacionResponse> =
        safeApiCall(tokenStore) { api.crearRolParticipacion(request) }

    suspend fun actualizarRolParticipacion(
        id: Long,
        request: RolParticipacionRequest
    ): ApiResult<RolParticipacionResponse> =
        safeApiCall(tokenStore) { api.actualizarRolParticipacion(id, request) }

    suspend fun activarRolParticipacion(id: Long): ApiResult<RolParticipacionResponse> =
        safeApiCall(tokenStore) { api.activarRolParticipacion(id) }

    suspend fun inactivarRolParticipacion(id: Long): ApiResult<RolParticipacionResponse> =
        safeApiCall(tokenStore) { api.inactivarRolParticipacion(id) }

    suspend fun archivarRolParticipacion(id: Long): ApiResult<RolParticipacionResponse> =
        safeApiCall(tokenStore) { api.archivarRolParticipacion(id) }
}
