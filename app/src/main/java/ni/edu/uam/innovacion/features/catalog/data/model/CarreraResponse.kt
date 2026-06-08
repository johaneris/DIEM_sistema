package ni.edu.uam.innovacion.features.catalog.data.model

data class CarreraResponse(
    val id: Long,
    val nombre: String,
    val codigo: String,
    val facultadId: Long,
    val facultadNombre: String?,
    val estado: String
)