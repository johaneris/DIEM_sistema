package ni.edu.uam.innovacion.features.catalog.data.repository

import ni.edu.uam.innovacion.core.network.RetrofitClient
import ni.edu.uam.innovacion.features.catalog.data.model.*
import ni.edu.uam.innovacion.features.catalog.data.remote.CatalogApiService

class CarreraRepository {
    private val api = RetrofitClient.instance.create(CatalogApiService::class.java)

    suspend fun listarCarreras(): List<CarreraResponse> = api.listarCarreras()

    suspend fun listarCarrerasPorFacultad(idFacultad: Long): List<CarreraResponse> = api.listarCarrerasPorFacultad(idFacultad)

    suspend fun crearCarrera(request: CarreraRequest): CarreraResponse = api.crearCarrera(request)

    suspend fun actualizarCarrera(id: Long, request: CarreraRequest): CarreraResponse = api.actualizarCarrera(id, request)

    suspend fun activarCarrera(id: Long): CarreraResponse = api.activarCarrera(id)

    suspend fun inactivarCarrera(id: Long): CarreraResponse = api.inactivarCarrera(id)

    suspend fun archivarCarrera(id: Long): CarreraResponse = api.archivarCarrera(id)
}