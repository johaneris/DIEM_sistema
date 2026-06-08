package ni.edu.uam.innovacion.features.admin.presentation.catalogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.innovacion.core.network.ApiResult
import ni.edu.uam.innovacion.core.network.getOrNull
import ni.edu.uam.innovacion.core.network.isSessionExpired
import ni.edu.uam.innovacion.core.network.readableMessage
import ni.edu.uam.innovacion.data.remote.catalog.AmbitoActividadRequest
import ni.edu.uam.innovacion.data.remote.catalog.CarreraRequest
import ni.edu.uam.innovacion.data.remote.catalog.CategoriaDiemRequest
import ni.edu.uam.innovacion.data.remote.catalog.FacultadRequest
import ni.edu.uam.innovacion.data.remote.catalog.FuenteProyectoRequest
import ni.edu.uam.innovacion.data.remote.catalog.RolParticipacionRequest
import ni.edu.uam.innovacion.data.remote.catalog.RolRequest
import ni.edu.uam.innovacion.data.repository.CatalogosRepository

enum class CatalogKind(val title: String, val subtitle: String) {
    Ambitos("Ambitos de actividad", "Clasificacion principal para las actividades"),
    Facultades("Facultades", "Unidades academicas disponibles"),
    Carreras("Carreras", "Programas academicos relacionados a facultades"),
    CategoriasDiem("Categorias DIEM", "Categorias relacionadas a ambitos de actividad"),
    FuentesProyecto("Fuentes de proyecto", "Origenes y categorias de proyectos"),
    Roles("Roles", "Permisos y responsabilidades de usuarios"),
    RolesParticipacion("Roles de participacion", "Tipos de participacion en actividades")
}

data class CatalogItemUi(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val estado: String,
    val codigo: String? = null,
    val parentId: Long? = null,
    val parentName: String? = null,
    val categoria: String? = null,
    val criterios: String? = null,
    val requiereCategoria: Boolean = false
)

data class CatalogRelationOption(val id: Long, val nombre: String)

data class CatalogFormState(
    val nombre: String = "",
    val descripcion: String = "",
    val codigo: String = "",
    val parentId: String = "",
    val categoria: String = "",
    val criterios: String = "",
    val requiereCategoria: Boolean = false
)

data class CatalogsUiState(
    val kind: CatalogKind = CatalogKind.Ambitos,
    val isLoading: Boolean = false,
    val isWorking: Boolean = false,
    val items: List<CatalogItemUi> = emptyList(),
    val relations: List<CatalogRelationOption> = emptyList(),
    val search: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val sessionExpired: Boolean = false
)

class CatalogsViewModel(
    private val repository: CatalogosRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CatalogsUiState())
    val uiState = _uiState.asStateFlow()

    fun load(kind: CatalogKind) {
        _uiState.update {
            it.copy(kind = kind, isLoading = true, errorMessage = null, sessionExpired = false)
        }
        viewModelScope.launch {
            val itemsResult: ApiResult<out List<Any>> = when (kind) {
                CatalogKind.Ambitos -> repository.listarAmbitosActividad()
                CatalogKind.Facultades -> repository.listarFacultades()
                CatalogKind.Carreras -> repository.listarCarreras()
                CatalogKind.CategoriasDiem -> repository.listarCategoriasDiem()
                CatalogKind.FuentesProyecto -> repository.listarFuentesProyecto()
                CatalogKind.Roles -> repository.listarRoles()
                CatalogKind.RolesParticipacion -> repository.listarRolesParticipacion()
            }
            val relationsResult: ApiResult<out List<Any>>? = when (kind) {
                CatalogKind.Carreras -> repository.listarFacultadesActivas()
                CatalogKind.CategoriasDiem -> repository.listarAmbitosActividadActivos()
                else -> null
            }
            val results = listOfNotNull(itemsResult, relationsResult)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    items = itemsResult.getOrNull().orEmpty().mapNotNull { item -> item.toCatalogItem() },
                    relations = relationsResult?.getOrNull().orEmpty().mapNotNull { item ->
                        item.toRelationOption()
                    },
                    errorMessage = results.firstOrNull { result -> result !is ApiResult.Success }
                        ?.readableMessage(),
                    sessionExpired = results.any { result -> result.isSessionExpired() }
                )
            }
        }
    }

    fun updateSearch(value: String) {
        _uiState.update { it.copy(search = value) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun crear(kind: CatalogKind, form: CatalogFormState) {
        if (form.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingrese el nombre del registro.") }
            return
        }
        mutate(kind, "Registro creado correctamente.") {
            when (kind) {
                CatalogKind.Ambitos -> repository.crearAmbitoActividad(
                    AmbitoActividadRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.requiereCategoria
                    )
                )
                CatalogKind.Facultades -> repository.crearFacultad(
                    FacultadRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.codigo.trim()
                    )
                )
                CatalogKind.Carreras -> repository.crearCarrera(
                    CarreraRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.codigo.trim(),
                        requireParent(form)
                    )
                )
                CatalogKind.CategoriasDiem -> repository.crearCategoriaDiem(
                    CategoriaDiemRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.criterios.trim().ifBlank { null },
                        requireParent(form)
                    )
                )
                CatalogKind.FuentesProyecto -> repository.crearFuenteProyecto(
                    FuenteProyectoRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.categoria.trim()
                    )
                )
                CatalogKind.Roles -> repository.crearRol(
                    RolRequest(form.nombre.trim(), form.descripcion.trim().ifBlank { null })
                )
                CatalogKind.RolesParticipacion -> repository.crearRolParticipacion(
                    RolParticipacionRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null }
                    )
                )
            }
        }
    }

    fun actualizar(kind: CatalogKind, id: Long, form: CatalogFormState) {
        if (form.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingrese el nombre del registro.") }
            return
        }
        mutate(kind, "Registro actualizado correctamente.") {
            when (kind) {
                CatalogKind.Ambitos -> repository.actualizarAmbitoActividad(
                    id,
                    AmbitoActividadRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.requiereCategoria
                    )
                )
                CatalogKind.Facultades -> repository.actualizarFacultad(
                    id,
                    FacultadRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.codigo.trim()
                    )
                )
                CatalogKind.Carreras -> repository.actualizarCarrera(
                    id,
                    CarreraRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.codigo.trim(),
                        requireParent(form)
                    )
                )
                CatalogKind.CategoriasDiem -> repository.actualizarCategoriaDiem(
                    id,
                    CategoriaDiemRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.criterios.trim().ifBlank { null },
                        requireParent(form)
                    )
                )
                CatalogKind.FuentesProyecto -> repository.actualizarFuenteProyecto(
                    id,
                    FuenteProyectoRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null },
                        form.categoria.trim()
                    )
                )
                CatalogKind.Roles -> repository.actualizarRol(
                    id,
                    RolRequest(form.nombre.trim(), form.descripcion.trim().ifBlank { null })
                )
                CatalogKind.RolesParticipacion -> repository.actualizarRolParticipacion(
                    id,
                    RolParticipacionRequest(
                        form.nombre.trim(),
                        form.descripcion.trim().ifBlank { null }
                    )
                )
            }
        }
    }

    fun activar(kind: CatalogKind, id: Long) {
        mutate(kind, "Registro activado correctamente.") {
            when (kind) {
                CatalogKind.Ambitos -> repository.activarAmbitoActividad(id)
                CatalogKind.Facultades -> repository.activarFacultad(id)
                CatalogKind.Carreras -> repository.activarCarrera(id)
                CatalogKind.CategoriasDiem -> repository.activarCategoriaDiem(id)
                CatalogKind.FuentesProyecto -> repository.activarFuenteProyecto(id)
                CatalogKind.Roles -> repository.activarRol(id)
                CatalogKind.RolesParticipacion -> repository.activarRolParticipacion(id)
            }
        }
    }

    fun inactivar(kind: CatalogKind, id: Long) {
        mutate(kind, "Registro inactivado correctamente.") {
            when (kind) {
                CatalogKind.Ambitos -> repository.inactivarAmbitoActividad(id)
                CatalogKind.Facultades -> repository.inactivarFacultad(id)
                CatalogKind.Carreras -> repository.inactivarCarrera(id)
                CatalogKind.CategoriasDiem -> repository.inactivarCategoriaDiem(id)
                CatalogKind.FuentesProyecto -> repository.inactivarFuenteProyecto(id)
                CatalogKind.Roles -> repository.inactivarRol(id)
                CatalogKind.RolesParticipacion -> repository.inactivarRolParticipacion(id)
            }
        }
    }

    fun archivar(kind: CatalogKind, id: Long) {
        mutate(kind, "Registro archivado correctamente.") {
            when (kind) {
                CatalogKind.Ambitos -> repository.archivarAmbitoActividad(id)
                CatalogKind.Facultades -> repository.archivarFacultad(id)
                CatalogKind.Carreras -> repository.archivarCarrera(id)
                CatalogKind.CategoriasDiem -> repository.archivarCategoriaDiem(id)
                CatalogKind.FuentesProyecto -> repository.archivarFuenteProyecto(id)
                CatalogKind.Roles -> repository.archivarRol(id)
                CatalogKind.RolesParticipacion -> repository.archivarRolParticipacion(id)
            }
        }
    }

    private fun requireParent(form: CatalogFormState): Long =
        form.parentId.toLongOrNull() ?: throw IllegalArgumentException("Seleccione una relacion valida.")

    private fun mutate(kind: CatalogKind, successMessage: String, block: suspend () -> ApiResult<*>) {
        _uiState.update {
            it.copy(isWorking = true, errorMessage = null, successMessage = null, sessionExpired = false)
        }
        viewModelScope.launch {
            val result = try {
                block()
            } catch (error: IllegalArgumentException) {
                _uiState.update { it.copy(isWorking = false, errorMessage = error.message) }
                return@launch
            }
            if (result is ApiResult.Success) {
                _uiState.update { it.copy(isWorking = false, successMessage = successMessage) }
                load(kind)
            } else {
                _uiState.update {
                    it.copy(
                        isWorking = false,
                        errorMessage = result.readableMessage(),
                        sessionExpired = result.isSessionExpired()
                    )
                }
            }
        }
    }

    companion object {
        fun factory(repository: CatalogosRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CatalogsViewModel(repository) as T
                }
            }
    }
}

private fun Any.toCatalogItem(): CatalogItemUi? =
    when (this) {
        is ni.edu.uam.innovacion.data.remote.catalog.AmbitoActividadResponse -> CatalogItemUi(
            id, nombre, descripcion, estado, requiereCategoria = requiereCategoria
        )
        is ni.edu.uam.innovacion.data.remote.catalog.FacultadResponse -> CatalogItemUi(
            id, nombre, descripcion, estado, codigo = codigo
        )
        is ni.edu.uam.innovacion.data.remote.catalog.CarreraResponse -> CatalogItemUi(
            id, nombre, descripcion, estado, codigo, idFacultad, nombreFacultad
        )
        is ni.edu.uam.innovacion.data.remote.catalog.CategoriaDiemResponse -> CatalogItemUi(
            id, nombre, descripcion, estado,
            parentId = idAmbitoActividad,
            parentName = nombreAmbitoActividad,
            criterios = criteriosPuntuacion,
            requiereCategoria = requiereCategoriaAmbito
        )
        is ni.edu.uam.innovacion.data.remote.catalog.FuenteProyectoResponse -> CatalogItemUi(
            id, nombre, descripcion, estado, categoria = categoria
        )
        is ni.edu.uam.innovacion.data.remote.catalog.RolResponse -> CatalogItemUi(
            id, nombre, descripcion, estado
        )
        is ni.edu.uam.innovacion.data.remote.catalog.RolParticipacionResponse -> CatalogItemUi(
            id, nombre, descripcion, estado
        )
        else -> null
    }

private fun Any.toRelationOption(): CatalogRelationOption? =
    when (this) {
        is ni.edu.uam.innovacion.data.remote.catalog.FacultadResponse ->
            CatalogRelationOption(id, nombre)
        is ni.edu.uam.innovacion.data.remote.catalog.AmbitoActividadResponse ->
            CatalogRelationOption(id, nombre)
        else -> null
    }
