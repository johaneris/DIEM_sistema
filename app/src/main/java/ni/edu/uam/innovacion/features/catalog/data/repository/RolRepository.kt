package ni.edu.uam.innovacion.features.catalog.data.repository

import ni.edu.uam.innovacion.core.network.RetrofitClient
import ni.edu.uam.innovacion.features.catalog.data.model.RolResponse
import ni.edu.uam.innovacion.features.catalog.data.remote.CatalogApiService

class RolRepository {
    private val api = RetrofitClient.instance.create(CatalogApiService::class.java)

    suspend fun listarRoles(): List<RolResponse> {
        return api.listarRoles()
    }
}