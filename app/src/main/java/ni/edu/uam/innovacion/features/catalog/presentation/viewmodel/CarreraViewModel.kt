package ni.edu.uam.innovacion.features.catalog.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.innovacion.features.catalog.data.model.*
import ni.edu.uam.innovacion.features.catalog.data.repository.CarreraRepository
import ni.edu.uam.innovacion.features.catalog.data.repository.FacultadRepository

class CarreraViewModel : ViewModel() {

    private val repository = CarreraRepository()
    private val facultadRepository = FacultadRepository()

    private val _carreras = MutableStateFlow<List<CarreraResponse>>(emptyList())
    val carreras: StateFlow<List<CarreraResponse>> = _carreras

    private val _facultades = MutableStateFlow<List<FacultadResponse>>(emptyList())
    val facultades: StateFlow<List<FacultadResponse>> = _facultades

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun cargarDatos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _carreras.value = repository.listarCarreras()
                _facultades.value = facultadRepository.listarFacultades()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun crearCarrera(nombre: String, codigo: String, facultadId: Long) {
        viewModelScope.launch {
            try {
                repository.crearCarrera(CarreraRequest(nombre, codigo, facultadId))
                cargarDatos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun actualizarCarrera(id: Long, nombre: String, codigo: String, facultadId: Long) {
        viewModelScope.launch {
            try {
                repository.actualizarCarrera(id, CarreraRequest(nombre, codigo, facultadId))
                cargarDatos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun activarCarrera(id: Long) {
        viewModelScope.launch {
            try {
                repository.activarCarrera(id)
                cargarDatos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun inactivarCarrera(id: Long) {
        viewModelScope.launch {
            try {
                repository.inactivarCarrera(id)
                cargarDatos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun archivarCarrera(id: Long) {
        viewModelScope.launch {
            try {
                repository.archivarCarrera(id)
                cargarDatos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}