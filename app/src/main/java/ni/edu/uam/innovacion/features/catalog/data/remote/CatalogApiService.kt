package ni.edu.uam.innovacion.features.catalog.data.remote

import ni.edu.uam.innovacion.features.catalog.data.model.RolResponse
import retrofit2.http.GET

interface CatalogApiService {
    @GET("api/admin/catalog/roles")
    suspend fun listarRoles(): List<RolResponse>
}