package ni.edu.uam.innovacion.features.catalog.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.innovacion.features.catalog.data.model.RolResponse
import ni.edu.uam.innovacion.features.catalog.data.repository.RolRepository

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
}