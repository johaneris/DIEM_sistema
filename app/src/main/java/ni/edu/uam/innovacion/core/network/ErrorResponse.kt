package ni.edu.uam.innovacion.core.network

data class ErrorResponse(
    val timestamp: String? = null,
    val status: Int? = null,
    val error: String? = null,
    val message: String? = null,
    val details: Map<String, Any?> = emptyMap()
)
