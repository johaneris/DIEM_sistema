package ni.edu.uam.innovacion.features.admin.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AdminDestination(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val parent: String? = null
) {
    data object Dashboard : AdminDestination("admin/dashboard", "Dashboard", Icons.Filled.Dashboard)
    data object Usuarios : AdminDestination("admin/usuarios", "Usuarios", Icons.Filled.Groups)

    data object PerfilAdministrador : AdminDestination(
        "admin/perfiles/administrador",
        "Administradores",
        Icons.Filled.ManageAccounts,
        parent = "Perfiles"
    )

    data object PerfilMentor : AdminDestination(
        "admin/perfiles/mentor",
        "Mentores",
        Icons.Filled.Person,
        parent = "Perfiles"
    )

    data object PerfilDocente : AdminDestination(
        "admin/perfiles/docente",
        "Docentes",
        Icons.Filled.School,
        parent = "Perfiles"
    )

    data object PerfilEstudiante : AdminDestination(
        "admin/perfiles/estudiante",
        "Estudiantes",
        Icons.Filled.MenuBook,
        parent = "Perfiles"
    )

    data object PerfilExterno : AdminDestination(
        "admin/perfiles/externo",
        "Participantes externos",
        Icons.Filled.Badge,
        parent = "Perfiles"
    )

    data object Actividades : AdminDestination("admin/actividades", "Actividades", Icons.Filled.CalendarMonth)
    data object Participacion : AdminDestination("admin/participacion", "Participacion", Icons.Filled.Assignment)
    data object Reportes : AdminDestination("admin/reportes", "Reportes", Icons.Filled.BarChart)
    data object Constancia : AdminDestination("admin/constancia", "Constancia", Icons.Filled.Description)

    data object Ambitos : AdminDestination(
        "admin/catalogos/ambitos",
        "Ambitos de actividad",
        Icons.Filled.Category,
        parent = "Catalogos"
    )

    data object Facultades : AdminDestination(
        "admin/catalogos/facultades",
        "Facultades",
        Icons.Filled.Category,
        parent = "Catalogos"
    )

    data object Carreras : AdminDestination(
        "admin/catalogos/carreras",
        "Carreras",
        Icons.Filled.Category,
        parent = "Catalogos"
    )

    data object CategoriasDiem : AdminDestination(
        "admin/catalogos/categorias-diem",
        "Categorias DIEM",
        Icons.Filled.Category,
        parent = "Catalogos"
    )

    data object FuentesProyecto : AdminDestination(
        "admin/catalogos/fuentes-proyecto",
        "Fuentes de proyecto",
        Icons.Filled.Category,
        parent = "Catalogos"
    )

    data object Roles : AdminDestination(
        "admin/catalogos/roles",
        "Roles",
        Icons.Filled.Category,
        parent = "Catalogos"
    )

    data object RolesParticipacion : AdminDestination(
        "admin/catalogos/roles-participacion",
        "Roles de participacion",
        Icons.Filled.LocalActivity,
        parent = "Catalogos"
    )

    companion object {
        val profileItems = listOf(
            PerfilAdministrador,
            PerfilMentor,
            PerfilDocente,
            PerfilEstudiante,
            PerfilExterno
        )

        val catalogItems = listOf(
            Ambitos,
            Facultades,
            Carreras,
            CategoriasDiem,
            FuentesProyecto,
            Roles,
            RolesParticipacion
        )

        val topLevelItems = listOf(
            Dashboard,
            Usuarios,
            Actividades,
            Participacion,
            Reportes,
            Constancia
        )

        val all = listOf(Dashboard, Usuarios) + profileItems +
            listOf(Actividades, Participacion, Reportes, Constancia) + catalogItems
    }
}
