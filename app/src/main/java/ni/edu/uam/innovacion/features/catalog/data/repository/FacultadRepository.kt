package ni.edu.uam.innovacion.features.catalog.data.repository

import ni.edu.uam.innovacion.core.network.RetrofitClient
import ni.edu.uam.innovacion.features.catalog.data.model.*
import ni.edu.uam.innovacion.features.catalog.data.remote.CatalogApiService

class FacultadRepository {
    private val api = RetrofitClient.instance.create(CatalogApiService::class.java)

    suspend fun listarFacultades(): List<FacultadResponse> = api.listarFacultades()

    suspend fun crearFacultad(request: FacultadRequest): FacultadResponse = api.crearFacultad(request)

    suspend fun actualizarFacultad(id: Long, request: FacultadRequest): FacultadResponse = api.actualizarFacultad(id, request)

    suspend fun activarFacultad(id: Long): FacultadResponse = api.activarFacultad(id)

    suspend fun inactivarFacultad(id: Long): FacultadResponse = api.inactivarFacultad(id)

    suspend fun archivarFacultad(id: Long): FacultadResponse = api.archivarFacultad(id)
}