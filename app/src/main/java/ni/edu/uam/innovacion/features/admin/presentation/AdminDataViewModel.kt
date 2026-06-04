package ni.edu.uam.innovacion.features.admin.presentation

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
import ni.edu.uam.innovacion.data.remote.activity.ActividadResponse
import ni.edu.uam.innovacion.data.remote.catalog.AmbitoActividadResponse
import ni.edu.uam.innovacion.data.remote.catalog.CarreraResponse
import ni.edu.uam.innovacion.data.remote.catalog.CategoriaDiemResponse
import ni.edu.uam.innovacion.data.remote.catalog.FacultadResponse
import ni.edu.uam.innovacion.data.remote.catalog.FuenteProyectoResponse
import ni.edu.uam.innovacion.data.remote.catalog.RolParticipacionResponse
import ni.edu.uam.innovacion.data.remote.catalog.RolResponse
import ni.edu.uam.innovacion.data.remote.user.UsuarioResponse
import ni.edu.uam.innovacion.data.repository.ActividadesRepository
import ni.edu.uam.innovacion.data.repository.CatalogosRepository
import ni.edu.uam.innovacion.data.repository.UsuariosRepository

data class CatalogosSnapshot(
    val ambitosActividad: List<AmbitoActividadResponse> = emptyList(),
    val facultades: List<FacultadResponse> = emptyList(),
    val carreras: List<CarreraResponse> = emptyList(),
    val categoriasDiem: List<CategoriaDiemResponse> = emptyList(),
    val fuentesProyecto: List<FuenteProyectoResponse> = emptyList(),
    val roles: List<RolResponse> = emptyList(),
    val rolesParticipacion: List<RolParticipacionResponse> = emptyList()
)

data class AdminDataUiState(
    val isLoading: Boolean = false,
    val usuarios: List<UsuarioResponse> = emptyList(),
    val actividades: List<ActividadResponse> = emptyList(),
    val catalogos: CatalogosSnapshot = CatalogosSnapshot(),
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
) {
    val participantesUnicos: Int = usuarios.size
    val actividadesRealizadas: Int = actividades.count { it.estado == "finalizada" }
    val puntosOtorgables: Int = actividades.sumOf { it.puntosBase }
    val issues: Int = listOfNotNull(errorMessage).size
}

class AdminDataViewModel(
    private val usuariosRepository: UsuariosRepository,
    private val catalogosRepository: CatalogosRepository,
    private val actividadesRepository: ActividadesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminDataUiState())
    val uiState = _uiState.asStateFlow()

    fun refreshDashboardData() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, sessionExpired = false) }
        viewModelScope.launch {
            val usuarios = usuariosRepository.listarUsuarios()
            val actividades = actividadesRepository.listarTodas()
            val ambitos = catalogosRepository.listarAmbitosActividad()
            val facultades = catalogosRepository.listarFacultades()
            val carreras = catalogosRepository.listarCarreras()
            val categorias = catalogosRepository.listarCategoriasDiem()
            val fuentes = catalogosRepository.listarFuentesProyecto()
            val roles = catalogosRepository.listarRoles()
            val rolesParticipacion = catalogosRepository.listarRolesParticipacion()

            val results = listOf(
                usuarios,
                actividades,
                ambitos,
                facultades,
                carreras,
                categorias,
                fuentes,
                roles,
                rolesParticipacion
            )
            val firstError = results.firstOrNull { it !is ApiResult.Success }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    usuarios = usuarios.getOrNull().orEmpty(),
                    actividades = actividades.getOrNull().orEmpty(),
                    catalogos = CatalogosSnapshot(
                        ambitosActividad = ambitos.getOrNull().orEmpty(),
                        facultades = facultades.getOrNull().orEmpty(),
                        carreras = carreras.getOrNull().orEmpty(),
                        categoriasDiem = categorias.getOrNull().orEmpty(),
                        fuentesProyecto = fuentes.getOrNull().orEmpty(),
                        roles = roles.getOrNull().orEmpty(),
                        rolesParticipacion = rolesParticipacion.getOrNull().orEmpty()
                    ),
                    errorMessage = firstError?.readableMessage(),
                    sessionExpired = results.any { result -> result.isSessionExpired() }
                )
            }
        }
    }

    companion object {
        fun factory(
            usuariosRepository: UsuariosRepository,
            catalogosRepository: CatalogosRepository,
            actividadesRepository: ActividadesRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AdminDataViewModel(
                    usuariosRepository,
                    catalogosRepository,
                    actividadesRepository
                ) as T
            }
        }
    }
}
