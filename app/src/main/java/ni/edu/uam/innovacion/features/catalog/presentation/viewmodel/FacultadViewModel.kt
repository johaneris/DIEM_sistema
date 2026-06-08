package ni.edu.uam.innovacion.features.catalog.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.innovacion.features.catalog.data.model.*
import ni.edu.uam.innovacion.features.catalog.data.repository.FacultadRepository

class FacultadViewModel : ViewModel() {

    private val repository = FacultadRepository()

    private val _facultades = MutableStateFlow<List<FacultadResponse>>(emptyList())
    val facultades: StateFlow<List<FacultadResponse>> = _facultades

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun cargarFacultades() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _facultades.value = repository.listarFacultades()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun crearFacultad(nombre: String, codigo: String) {
        viewModelScope.launch {
            try {
                repository.crearFacultad(FacultadRequest(nombre, codigo))
                cargarFacultades()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun actualizarFacultad(id: Long, nombre: String, codigo: String) {
        viewModelScope.launch {
            try {
                repository.actualizarFacultad(id, FacultadRequest(nombre, codigo))
                cargarFacultades()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun activarFacultad(id: Long) {
        viewModelScope.launch {
            try {
                repository.activarFacultad(id)
                cargarFacultades()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun inactivarFacultad(id: Long) {
        viewModelScope.launch {
            try {
                repository.inactivarFacultad(id)
                cargarFacultades()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun archivarFacultad(id: Long) {
        viewModelScope.launch {
            try {
                repository.archivarFacultad(id)
                cargarFacultades()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}