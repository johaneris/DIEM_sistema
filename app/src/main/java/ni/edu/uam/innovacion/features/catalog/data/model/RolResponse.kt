package ni.edu.uam.innovacion.features.catalog.data.model

data class RolResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val estado: String
)