package ni.edu.uam.innovacion.features.admin.presentation.activities

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
import ni.edu.uam.innovacion.data.remote.activity.ActividadRequest
import ni.edu.uam.innovacion.data.remote.activity.ActividadResponse
import ni.edu.uam.innovacion.data.remote.catalog.AmbitoActividadResponse
import ni.edu.uam.innovacion.data.remote.catalog.CategoriaDiemResponse
import ni.edu.uam.innovacion.data.remote.user.UsuarioResponse
import ni.edu.uam.innovacion.data.repository.ActividadesRepository
import ni.edu.uam.innovacion.data.repository.CatalogosRepository
import ni.edu.uam.innovacion.data.repository.UsuariosRepository

data class ActivityFormState(
    val idAmbitoActividad: String = "",
    val idCategoriaDiem: String = "",
    val idResponsableUsuario: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val fechaInicio: String = "",
    val fechaFin: String = "",
    val modalidad: String = "presencial",
    val cupoMaximo: String = "",
    val ubicacion: String = "",
    val responsableNombre: String = "",
    val puntosBase: String = "0"
)

data class ActivitiesUiState(
    val isLoading: Boolean = false,
    val isWorking: Boolean = false,
    val actividades: List<ActividadResponse> = emptyList(),
    val ambitos: List<AmbitoActividadResponse> = emptyList(),
    val categorias: List<CategoriaDiemResponse> = emptyList(),
    val usuarios: List<UsuarioResponse> = emptyList(),
    val search: String = "",
    val estadoFilter: String = "todos",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val sessionExpired: Boolean = false
)

class ActivitiesViewModel(
    private val actividadesRepository: ActividadesRepository,
    private val catalogosRepository: CatalogosRepository,
    private val usuariosRepository: UsuariosRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ActivitiesUiState())
    val uiState = _uiState.asStateFlow()

    fun load() {
        _uiState.update {
            it.copy(isLoading = true, errorMessage = null, sessionExpired = false)
        }
        viewModelScope.launch {
            val actividades = actividadesRepository.listarTodas()
            val ambitos = catalogosRepository.listarAmbitosActividadActivos()
            val categorias = catalogosRepository.listarCategoriasDiemActivas()
            val usuarios = usuariosRepository.listarUsuarios()
            val results = listOf(actividades, ambitos, categorias, usuarios)
            val firstError = results.firstOrNull { it !is ApiResult.Success }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    actividades = actividades.getOrNull().orEmpty(),
                    ambitos = ambitos.getOrNull().orEmpty(),
                    categorias = categorias.getOrNull().orEmpty(),
                    usuarios = usuarios.getOrNull().orEmpty(),
                    errorMessage = firstError?.readableMessage(),
                    sessionExpired = results.any { result -> result.isSessionExpired() }
                )
            }
        }
    }

    fun updateSearch(value: String) {
        _uiState.update { it.copy(search = value) }
    }

    fun updateEstadoFilter(value: String) {
        _uiState.update { it.copy(estadoFilter = value) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun crear(form: ActivityFormState) {
        val request = form.toRequestOrError()
        if (request == null) return
        mutate("Actividad creada correctamente.") { actividadesRepository.crear(request) }
    }

    fun actualizar(idActividad: Long, form: ActivityFormState) {
        val request = form.toRequestOrError()
        if (request == null) return
        mutate("Actividad actualizada correctamente.") {
            actividadesRepository.actualizar(idActividad, request)
        }
    }

    fun publicar(idActividad: Long) {
        mutate("Actividad publicada correctamente.") { actividadesRepository.publicar(idActividad) }
    }

    fun iniciar(idActividad: Long) {
        mutate("Actividad iniciada correctamente.") { actividadesRepository.iniciar(idActividad) }
    }

    fun finalizar(idActividad: Long) {
        mutate("Actividad finalizada correctamente.") { actividadesRepository.finalizar(idActividad) }
    }

    fun cancelar(idActividad: Long) {
        mutate("Actividad cancelada correctamente.") { actividadesRepository.cancelar(idActividad) }
    }

    fun archivar(idActividad: Long) {
        mutate("Actividad archivada correctamente.") { actividadesRepository.archivar(idActividad) }
    }

    private fun ActivityFormState.toRequestOrError(): ActividadRequest? {
        val ambitoId = idAmbitoActividad.toLongOrNull()
        if (nombre.isBlank() || fechaInicio.isBlank() || ambitoId == null) {
            _uiState.update {
                it.copy(errorMessage = "Complete nombre, fecha de inicio y ambito de actividad.")
            }
            return null
        }
        return ActividadRequest(
            idAmbitoActividad = ambitoId,
            idCategoriaDiem = idCategoriaDiem.toLongOrNull(),
            idResponsableUsuario = idResponsableUsuario.toLongOrNull(),
            nombre = nombre.trim(),
            descripcion = descripcion.trim().ifBlank { null },
            fechaInicio = fechaInicio.trim(),
            fechaFin = fechaFin.trim().ifBlank { null },
            modalidad = modalidad.trim().ifBlank { "presencial" },
            cupoMaximo = cupoMaximo.toIntOrNull(),
            ubicacion = ubicacion.trim().ifBlank { null },
            responsableNombre = responsableNombre.trim().ifBlank { null },
            puntosBase = puntosBase.toIntOrNull()
        )
    }

    private fun mutate(successMessage: String, block: suspend () -> ApiResult<*>) {
        _uiState.update {
            it.copy(isWorking = true, errorMessage = null, successMessage = null, sessionExpired = false)
        }
        viewModelScope.launch {
            val result = block()
            if (result is ApiResult.Success) {
                _uiState.update { it.copy(isWorking = false, successMessage = successMessage) }
                load()
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
        fun factory(
            actividadesRepository: ActividadesRepository,
            catalogosRepository: CatalogosRepository,
            usuariosRepository: UsuariosRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ActivitiesViewModel(
                    actividadesRepository,
                    catalogosRepository,
                    usuariosRepository
                ) as T
            }
        }
    }
}
