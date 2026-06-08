package ni.edu.uam.innovacion.features.admin.presentation.users

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
import ni.edu.uam.innovacion.data.remote.catalog.CarreraResponse
import ni.edu.uam.innovacion.data.remote.catalog.FacultadResponse
import ni.edu.uam.innovacion.data.remote.catalog.RolResponse
import ni.edu.uam.innovacion.data.remote.user.ActualizarUsuarioRequest
import ni.edu.uam.innovacion.data.remote.user.CambiarContrasenaRequest
import ni.edu.uam.innovacion.data.remote.user.CrearUsuarioRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilAdministradorRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilDocenteRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilEstudianteRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilMentorRequest
import ni.edu.uam.innovacion.data.remote.user.PerfilParticipanteExternoRequest
import ni.edu.uam.innovacion.data.remote.user.UsuarioResponse
import ni.edu.uam.innovacion.data.repository.CatalogosRepository
import ni.edu.uam.innovacion.data.repository.UsuariosRepository

enum class ProfileKind(val title: String, val roleName: String) {
    Administrador("Administradores", "administrador"),
    Mentor("Mentores", "mentor"),
    Docente("Docentes", "docente"),
    Estudiante("Estudiantes", "estudiante"),
    Externo("Participantes externos", "participante_externo")
}

data class UsuarioFormState(
    val nombreCompleto: String = "",
    val documento: String = "",
    val telefono: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val sexo: String = "",
    val tallaCamisa: String = ""
)

data class ProfileFormState(
    val cargo: String = "",
    val nivelAcceso: String = "total",
    val cif: String = "",
    val correoInstitucional: String = "",
    val idCarreraPrincipal: String = "",
    val areaAcademica: String = "",
    val gradoAcademico: String = "",
    val tituloUniversitario: String = "",
    val idFacultad: String = "",
    val areaExperiencia: String = "",
    val especialidad: String = "",
    val institucion: String = "",
    val tipoAcompanamiento: String = "",
    val ocupacion: String = "",
    val institucionProcedencia: String = ""
)

data class UsersUiState(
    val isLoading: Boolean = false,
    val isWorking: Boolean = false,
    val usuarios: List<UsuarioResponse> = emptyList(),
    val roles: List<RolResponse> = emptyList(),
    val carreras: List<CarreraResponse> = emptyList(),
    val facultades: List<FacultadResponse> = emptyList(),
    val search: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val sessionExpired: Boolean = false
)

class UsersViewModel(
    private val usuariosRepository: UsuariosRepository,
    private val catalogosRepository: CatalogosRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState = _uiState.asStateFlow()

    fun load() {
        _uiState.update {
            it.copy(isLoading = true, errorMessage = null, sessionExpired = false)
        }
        viewModelScope.launch {
            val usuarios = usuariosRepository.listarUsuarios()
            val roles = catalogosRepository.listarRolesActivos()
            val carreras = catalogosRepository.listarCarrerasActivas()
            val facultades = catalogosRepository.listarFacultadesActivas()
            val results = listOf(usuarios, roles, carreras, facultades)
            val firstError = results.firstOrNull { it !is ApiResult.Success }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    usuarios = usuarios.getOrNull().orEmpty(),
                    roles = roles.getOrNull().orEmpty(),
                    carreras = carreras.getOrNull().orEmpty(),
                    facultades = facultades.getOrNull().orEmpty(),
                    errorMessage = firstError?.readableMessage(),
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

    fun crearUsuario(form: UsuarioFormState) {
        if (form.nombreCompleto.isBlank() || form.documento.isBlank() ||
            form.correo.isBlank() || form.contrasena.isBlank()
        ) {
            _uiState.update { it.copy(errorMessage = "Complete nombre, documento, correo y contrasena.") }
            return
        }
        mutate("Usuario creado correctamente.") {
            usuariosRepository.crearUsuario(
                CrearUsuarioRequest(
                    nombreCompleto = form.nombreCompleto.trim(),
                    documento = form.documento.trim(),
                    telefono = form.telefono.trim().ifBlank { null },
                    correo = form.correo.trim(),
                    contrasena = form.contrasena,
                    sexo = form.sexo.trim().ifBlank { null },
                    tallaCamisa = form.tallaCamisa.trim().ifBlank { null }
                )
            )
        }
    }

    fun actualizarUsuario(idUsuario: Long, form: UsuarioFormState) {
        if (form.nombreCompleto.isBlank() || form.documento.isBlank() || form.correo.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Complete nombre, documento y correo.") }
            return
        }
        mutate("Usuario actualizado correctamente.") {
            usuariosRepository.actualizarUsuario(
                idUsuario,
                ActualizarUsuarioRequest(
                    nombreCompleto = form.nombreCompleto.trim(),
                    documento = form.documento.trim(),
                    telefono = form.telefono.trim().ifBlank { null },
                    correo = form.correo.trim(),
                    sexo = form.sexo.trim().ifBlank { null },
                    tallaCamisa = form.tallaCamisa.trim().ifBlank { null }
                )
            )
        }
    }

    fun cambiarContrasena(idUsuario: Long, contrasena: String) {
        if (contrasena.length < 6) {
            _uiState.update { it.copy(errorMessage = "La contrasena debe tener al menos 6 caracteres.") }
            return
        }
        mutate("Contrasena actualizada correctamente.") {
            usuariosRepository.cambiarContrasena(idUsuario, CambiarContrasenaRequest(contrasena))
        }
    }

    fun asignarRol(idUsuario: Long, nombreRol: String) {
        if (nombreRol.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Seleccione un rol para asignar.") }
            return
        }
        mutate("Rol asignado correctamente.") {
            usuariosRepository.asignarRol(idUsuario, nombreRol)
        }
    }

    fun crearPerfil(kind: ProfileKind, idUsuario: Long, form: ProfileFormState) {
        val parsedCarrera = form.idCarreraPrincipal.toLongOrNull()
        val parsedFacultad = form.idFacultad.toLongOrNull()
        when (kind) {
            ProfileKind.Administrador -> {
                if (form.cargo.isBlank()) {
                    _uiState.update { it.copy(errorMessage = "Ingrese el cargo administrativo.") }
                    return
                }
                mutate("Perfil administrador creado correctamente.") {
                    usuariosRepository.crearPerfilAdministrador(
                        idUsuario,
                        PerfilAdministradorRequest(
                            cargo = form.cargo.trim(),
                            nivelAcceso = form.nivelAcceso.trim().ifBlank { "total" }
                        )
                    )
                }
            }

            ProfileKind.Estudiante -> {
                if (form.cif.isBlank() || parsedCarrera == null) {
                    _uiState.update { it.copy(errorMessage = "Ingrese CIF y carrera principal valida.") }
                    return
                }
                mutate("Perfil estudiante creado correctamente.") {
                    usuariosRepository.crearPerfilEstudiante(
                        idUsuario,
                        PerfilEstudianteRequest(
                            cif = form.cif.trim(),
                            correoInstitucional = form.correoInstitucional.trim().ifBlank { null },
                            idCarreraPrincipal = parsedCarrera
                        )
                    )
                }
            }

            ProfileKind.Docente -> {
                mutate("Perfil docente creado correctamente.") {
                    usuariosRepository.crearPerfilDocente(
                        idUsuario,
                        PerfilDocenteRequest(
                            areaAcademica = form.areaAcademica.trim().ifBlank { null },
                            cargo = form.cargo.trim().ifBlank { null },
                            gradoAcademico = form.gradoAcademico.trim().ifBlank { null },
                            tituloUniversitario = form.tituloUniversitario.trim().ifBlank { null },
                            idFacultad = parsedFacultad
                        )
                    )
                }
            }

            ProfileKind.Mentor -> {
                mutate("Perfil mentor creado correctamente.") {
                    usuariosRepository.crearPerfilMentor(
                        idUsuario,
                        PerfilMentorRequest(
                            areaExperiencia = form.areaExperiencia.trim().ifBlank { null },
                            especialidad = form.especialidad.trim().ifBlank { null },
                            institucion = form.institucion.trim().ifBlank { null },
                            tipoAcompanamiento = form.tipoAcompanamiento.trim().ifBlank { null },
                            gradoAcademico = form.gradoAcademico.trim().ifBlank { null },
                            tituloUniversitario = form.tituloUniversitario.trim().ifBlank { null }
                        )
                    )
                }
            }

            ProfileKind.Externo -> {
                mutate("Perfil externo creado correctamente.") {
                    usuariosRepository.crearPerfilParticipanteExterno(
                        idUsuario,
                        PerfilParticipanteExternoRequest(
                            ocupacion = form.ocupacion.trim().ifBlank { null },
                            institucionProcedencia = form.institucionProcedencia.trim().ifBlank { null }
                        )
                    )
                }
            }
        }
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
            usuariosRepository: UsuariosRepository,
            catalogosRepository: CatalogosRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UsersViewModel(usuariosRepository, catalogosRepository) as T
            }
        }
    }
}
