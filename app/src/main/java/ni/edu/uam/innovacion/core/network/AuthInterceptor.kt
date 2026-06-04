package ni.edu.uam.innovacion.core.network

import kotlinx.coroutines.runBlocking
import ni.edu.uam.innovacion.core.session.AuthTokenStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStore: AuthTokenStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = runBlocking { tokenStore.getAccessToken() }

        val requestBuilder = original.newBuilder()
            .header("Accept", "application/json")

        if (original.body != null) {
            requestBuilder.header("Content-Type", "application/json")
        }

        if (!token.isNullOrBlank() && original.header("Authorization").isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
