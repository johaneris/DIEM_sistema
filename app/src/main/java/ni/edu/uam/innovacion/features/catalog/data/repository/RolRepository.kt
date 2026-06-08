package ni.edu.uam.innovacion.features.catalog.data.repository

import ni.edu.uam.innovacion.core.network.RetrofitClient
import ni.edu.uam.innovacion.features.catalog.data.model.RolResponse
import ni.edu.uam.innovacion.features.catalog.data.remote.CatalogApiService
import ni.edu.uam.innovacion.features.catalog.data.model.RolRequest

class RolRepository {
    private val api = RetrofitClient.instance.create(CatalogApiService::class.java)

    suspend fun listarRoles(): List<RolResponse> {
        return api.listarRoles()
    }

    suspend fun crearRol(request: RolRequest): RolResponse {
        return api.crearRol(request)
    }

    suspend fun actualizarRol(id: Long, request: RolRequest): RolResponse {
        return api.actualizarRol(id, request)
    }

    suspend fun activarRol(id: Long): RolResponse {
        return api.activarRol(id)
    }

    suspend fun inactivarRol(id: Long): RolResponse {
        return api.inactivarRol(id)
    }

    suspend fun archivarRol(id: Long): RolResponse {
        return api.archivarRol(id)
    }
}