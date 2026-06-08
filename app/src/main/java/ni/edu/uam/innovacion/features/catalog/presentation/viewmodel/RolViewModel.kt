package ni.edu.uam.innovacion.features.catalog.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.innovacion.features.catalog.data.model.RolResponse
import ni.edu.uam.innovacion.features.catalog.data.repository.RolRepository
import ni.edu.uam.innovacion.features.catalog.data.model.RolRequest

class RolViewModel : ViewModel() {

    private val repository = RolRepository()

    private val _roles = MutableStateFlow<List<RolResponse>>(emptyList())
    val roles: StateFlow<List<RolResponse>> = _roles

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarRoles() {
        viewModelScope.launch {
            try {
                _roles.value = repository.listarRoles()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun crearRol(nombre: String, descripcion: String?) {
        viewModelScope.launch {
            try {
                repository.crearRol(
                    RolRequest(
                        nombre = nombre,
                        descripcion = descripcion
                    )
                )
                cargarRoles()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun actualizarRol(id: Long, nombre: String, descripcion: String?) {
        viewModelScope.launch {
            try {
                repository.actualizarRol(
                    id = id,
                    request = RolRequest(
                        nombre = nombre,
                        descripcion = descripcion
                    )
                )
                cargarRoles()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun activarRol(id: Long) {
        viewModelScope.launch {
            try {
                repository.activarRol(id)
                cargarRoles()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun inactivarRol(id: Long) {
        viewModelScope.launch {
            try {
                repository.inactivarRol(id)
                cargarRoles()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun archivarRol(id: Long) {
        viewModelScope.launch {
            try {
                repository.archivarRol(id)
                cargarRoles()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}