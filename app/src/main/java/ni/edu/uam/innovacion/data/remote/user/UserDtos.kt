package ni.edu.uam.innovacion.data.remote.user

import ni.edu.uam.innovacion.data.remote.catalog.RolResponse

data class CrearUsuarioRequest(
    val nombreCompleto: String,
    val documento: String,
    val telefono: String?,
    val correo: String,
    val contrasena: String,
    val sexo: String?,
    val tallaCamisa: String?
)

data class ActualizarUsuarioRequest(
    val nombreCompleto: String,
    val documento: String,
    val telefono: String?,
    val correo: String,
    val sexo: String?,
    val tallaCamisa: String?
)

data class CambiarContrasenaRequest(
    val contrasena: String
)

data class UsuarioResponse(
    val idUsuario: Long,
    val nombreCompleto: String,
    val documento: String,
    val telefono: String?,
    val correo: String,
    val sexo: String?,
    val tallaCamisa: String?,
    val estado: String,
    val fechaRegistro: String,
    val ultimoAcceso: String?,
    val roles: List<RolResponse>,
    val perfilEstudiante: PerfilEstudianteResponse?,
    val perfilAdministrador: PerfilAdministradorResponse?,
    val perfilDocente: PerfilDocenteResponse?,
    val perfilMentor: PerfilMentorResponse?,
    val perfilParticipanteExterno: PerfilParticipanteExternoResponse?
)

data class PerfilEstudianteRequest(
    val cif: String,
    val correoInstitucional: String?,
    val idCarreraPrincipal: Long
)

data class PerfilEstudianteResponse(
    val idUsuario: Long,
    val cif: String,
    val correoInstitucional: String?,
    val idCarreraPrincipal: Long,
    val dobleTitular: Boolean
)

data class DobleTitulacionRequest(
    val idCarreraSecundaria: Long
)

data class DobleTitulacionResponse(
    val idDobleTitulacion: Long,
    val idEstudiante: Long,
    val idCarreraSecundaria: Long,
    val nombreCarreraSecundaria: String,
    val codigoCarreraSecundaria: String,
    val idFacultadCarreraSecundaria: Long,
    val nombreFacultadCarreraSecundaria: String,
    val fechaRegistro: String,
    val estado: String
)

data class PerfilAdministradorRequest(
    val cargo: String,
    val nivelAcceso: String
)

data class PerfilAdministradorResponse(
    val idUsuario: Long,
    val cargo: String,
    val nivelAcceso: String
)

data class PerfilDocenteRequest(
    val areaAcademica: String?,
    val cargo: String?,
    val gradoAcademico: String?,
    val tituloUniversitario: String?,
    val idFacultad: Long?
)

data class PerfilDocenteResponse(
    val idUsuario: Long,
    val areaAcademica: String?,
    val cargo: String?,
    val gradoAcademico: String?,
    val tituloUniversitario: String?,
    val idFacultad: Long?
)

data class PerfilMentorRequest(
    val areaExperiencia: String?,
    val especialidad: String?,
    val institucion: String?,
    val tipoAcompanamiento: String?,
    val gradoAcademico: String?,
    val tituloUniversitario: String?
)

data class PerfilMentorResponse(
    val idUsuario: Long,
    val areaExperiencia: String?,
    val especialidad: String?,
    val institucion: String?,
    val tipoAcompanamiento: String?,
    val gradoAcademico: String?,
    val tituloUniversitario: String?
)

data class PerfilParticipanteExternoRequest(
    val ocupacion: String?,
    val institucionProcedencia: String?
)

data class PerfilParticipanteExternoResponse(
    val idUsuario: Long,
    val ocupacion: String?,
    val institucionProcedencia: String?
)
