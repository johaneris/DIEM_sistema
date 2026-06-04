package ni.edu.uam.innovacion.data.remote.catalog

data class AmbitoActividadRequest(
    val nombre: String,
    val descripcion: String?,
    val requiereCategoria: Boolean
)

data class AmbitoActividadResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val requiereCategoria: Boolean,
    val estado: String
)

data class FacultadRequest(
    val nombre: String,
    val descripcion: String?,
    val codigo: String
)

data class FacultadResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val codigo: String,
    val estado: String
)

data class CarreraRequest(
    val nombre: String,
    val descripcion: String?,
    val codigo: String,
    val idFacultad: Long
)

data class CarreraResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val codigo: String,
    val estado: String,
    val idFacultad: Long,
    val nombreFacultad: String?,
    val codigoFacultad: String?
)

data class CategoriaDiemRequest(
    val nombre: String,
    val descripcion: String?,
    val criteriosPuntuacion: String?,
    val idAmbitoActividad: Long
)

data class CategoriaDiemResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val criteriosPuntuacion: String?,
    val estado: String,
    val idAmbitoActividad: Long,
    val nombreAmbitoActividad: String?,
    val requiereCategoriaAmbito: Boolean
)

data class FuenteProyectoRequest(
    val nombre: String,
    val descripcion: String?,
    val categoria: String
)

data class FuenteProyectoResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val categoria: String,
    val estado: String
)

data class RolRequest(
    val nombre: String,
    val descripcion: String?
)

data class RolResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val estado: String
)

data class RolParticipacionRequest(
    val nombre: String,
    val descripcion: String?
)

data class RolParticipacionResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val estado: String
)
