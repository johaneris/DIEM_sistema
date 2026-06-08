package ni.edu.uam.innovacion.di

import android.content.Context
import ni.edu.uam.innovacion.core.network.ApiClient
import ni.edu.uam.innovacion.core.session.AuthTokenStore
import ni.edu.uam.innovacion.data.remote.activity.ActividadesApiService
import ni.edu.uam.innovacion.data.remote.auth.AuthApiService
import ni.edu.uam.innovacion.data.remote.catalog.CatalogosApiService
import ni.edu.uam.innovacion.data.remote.user.UsuariosApiService
import ni.edu.uam.innovacion.data.repository.ActividadesRepository
import ni.edu.uam.innovacion.data.repository.AuthRepository
import ni.edu.uam.innovacion.data.repository.CatalogosRepository
import ni.edu.uam.innovacion.data.repository.UsuariosRepository

class AppContainer(context: Context) {
    private val tokenStore = AuthTokenStore(context.applicationContext)
    private val retrofit = ApiClient.createRetrofit(tokenStore)

    private val authApi: AuthApiService = retrofit.create(AuthApiService::class.java)
    private val usuariosApi: UsuariosApiService = retrofit.create(UsuariosApiService::class.java)
    private val catalogosApi: CatalogosApiService = retrofit.create(CatalogosApiService::class.java)
    private val actividadesApi: ActividadesApiService = retrofit.create(ActividadesApiService::class.java)

    val authRepository = AuthRepository(authApi, tokenStore)
    val usuariosRepository = UsuariosRepository(usuariosApi, tokenStore)
    val catalogosRepository = CatalogosRepository(catalogosApi, tokenStore)
    val actividadesRepository = ActividadesRepository(actividadesApi, tokenStore)
}
