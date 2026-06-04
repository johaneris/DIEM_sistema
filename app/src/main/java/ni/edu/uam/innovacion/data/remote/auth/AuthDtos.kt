package ni.edu.uam.innovacion.data.remote.auth

data class LoginRequest(
    val correo: String,
    val contrasena: String
)

data class LoginResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val usuario: AuthenticatedUserResponse
)

data class AuthenticatedUserResponse(
    val idUsuario: Long,
    val nombreCompleto: String,
    val correo: String,
    val estado: String,
    val roles: List<String>
)
