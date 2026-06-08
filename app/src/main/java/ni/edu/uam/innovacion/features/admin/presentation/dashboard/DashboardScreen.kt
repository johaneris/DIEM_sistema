package ni.edu.uam.innovacion.features.admin.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Stars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ni.edu.uam.innovacion.features.admin.presentation.AdminDataUiState
import ni.edu.uam.innovacion.features.admin.presentation.AdminDataViewModel
import ni.edu.uam.innovacion.features.admin.presentation.components.AdminPageHeader
import ni.edu.uam.innovacion.features.admin.presentation.components.DetailRow
import ni.edu.uam.innovacion.features.admin.presentation.components.ErrorState
import ni.edu.uam.innovacion.features.admin.presentation.components.LoadingState
import ni.edu.uam.innovacion.features.admin.presentation.components.MetricCard
import ni.edu.uam.innovacion.features.admin.presentation.components.SectionSurface
import ni.edu.uam.innovacion.ui.theme.UamInnovationYellow
import ni.edu.uam.innovacion.ui.theme.UamTurquoise

@Composable
fun DashboardScreen(
    viewModel: AdminDataViewModel,
    onSessionExpired: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.refreshDashboardData() }
    LaunchedEffect(state.sessionExpired) {
        if (state.sessionExpired) onSessionExpired()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AdminPageHeader(
                title = "Dashboard",
                subtitle = "Resumen operativo con datos actuales del sistema",
                onRefresh = viewModel::refreshDashboardData,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        if (state.isLoading && state.usuarios.isEmpty() && state.actividades.isEmpty()) {
            item { LoadingState() }
        } else {
            item { DashboardMetrics(state) }
            state.errorMessage?.let { message ->
                item { ErrorState(message, viewModel::refreshDashboardData) }
            }
            item {
                SectionSurface(title = "Actividades por estado") {
                    val groups = state.actividades
                        .groupingBy { it.estado.replace('_', ' ') }
                        .eachCount()
                        .toList()
                        .sortedByDescending { it.second }
                    if (groups.isEmpty()) {
                        DetailRow("Sin actividades", "0")
                    } else {
                        groups.forEach { (label, count) -> DetailRow(label, count.toString()) }
                    }
                }
            }
            item {
                SectionSurface(title = "Perfiles registrados") {
                    DetailRow(
                        "Administradores",
                        state.usuarios.count { it.perfilAdministrador != null }.toString()
                    )
                    DetailRow("Mentores", state.usuarios.count { it.perfilMentor != null }.toString())
                    DetailRow("Docentes", state.usuarios.count { it.perfilDocente != null }.toString())
                    DetailRow(
                        "Estudiantes",
                        state.usuarios.count { it.perfilEstudiante != null }.toString()
                    )
                    DetailRow(
                        "Participantes externos",
                        state.usuarios.count { it.perfilParticipanteExterno != null }.toString()
                    )
                }
            }
            item {
                SectionSurface(title = "Catalogos") {
                    DetailRow("Ambitos de actividad", state.catalogos.ambitosActividad.size.toString())
                    DetailRow("Facultades", state.catalogos.facultades.size.toString())
                    DetailRow("Carreras", state.catalogos.carreras.size.toString())
                    DetailRow("Categorias DIEM", state.catalogos.categoriasDiem.size.toString())
                    DetailRow("Fuentes de proyecto", state.catalogos.fuentesProyecto.size.toString())
                    DetailRow("Roles", state.catalogos.roles.size.toString())
                    DetailRow(
                        "Roles de participacion",
                        state.catalogos.rolesParticipacion.size.toString()
                    )
                }
            }
        }
        item { Column(modifier = Modifier.padding(bottom = 20.dp)) {} }
    }
}

@Composable
private fun DashboardMetrics(state: AdminDataUiState) {
    val metrics = listOf(
        DashboardMetric("Usuarios registrados", state.usuariosRegistrados.toString(), Icons.Filled.Groups),
        DashboardMetric("Usuarios activos", state.usuariosActivos.toString(), Icons.Filled.PeopleAlt),
        DashboardMetric("Actividades totales", state.actividadesTotales.toString(), Icons.Filled.LocalActivity),
        DashboardMetric(
            "Actividades finalizadas",
            state.actividadesFinalizadas.toString(),
            Icons.Filled.AssignmentTurnedIn
        ),
        DashboardMetric(
            "Puntos base configurados",
            state.puntosBaseConfigurados.toString(),
            Icons.Filled.Stars,
            accentYellow = true
        )
    )
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val columns = if (maxWidth >= 720.dp) 3 else 2
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            metrics.chunked(columns).forEach { rowMetrics ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowMetrics.forEach { metric ->
                        MetricCard(
                            label = metric.label,
                            value = metric.value,
                            icon = metric.icon,
                            accent = if (metric.accentYellow) UamInnovationYellow else UamTurquoise,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(columns - rowMetrics.size) {
                        Column(modifier = Modifier.weight(1f)) {}
                    }
                }
            }
        }
    }
}

private data class DashboardMetric(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val accentYellow: Boolean = false
)
