package ni.edu.uam.innovacion.data.remote.activity

data class ActividadRequest(
    val idAmbitoActividad: Long,
    val idCategoriaDiem: Long?,
    val idResponsableUsuario: Long?,
    val nombre: String,
    val descripcion: String?,
    val fechaInicio: String,
    val fechaFin: String?,
    val modalidad: String,
    val cupoMaximo: Int?,
    val ubicacion: String?,
    val responsableNombre: String?,
    val puntosBase: Int?
)

data class ActividadResponse(
    val idActividad: Long,
    val idAmbitoActividad: Long,
    val nombreAmbitoActividad: String,
    val requiereCategoriaAmbito: Boolean,
    val idCategoriaDiem: Long?,
    val nombreCategoriaDiem: String?,
    val idAdministradorCreador: Long,
    val nombreAdministradorCreador: String?,
    val idResponsableUsuario: Long?,
    val nombreResponsableUsuario: String?,
    val nombre: String,
    val descripcion: String?,
    val fechaInicio: String,
    val fechaFin: String?,
    val modalidad: String,
    val estado: String,
    val cupoMaximo: Int?,
    val ubicacion: String?,
    val responsableNombre: String?,
    val puntosBase: Int,
    val creadoEn: String,
    val actualizadoEn: String?
)
