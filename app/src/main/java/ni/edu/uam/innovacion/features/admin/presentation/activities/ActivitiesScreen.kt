package ni.edu.uam.innovacion.features.admin.presentation.activities

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ni.edu.uam.innovacion.data.remote.activity.ActividadResponse
import ni.edu.uam.innovacion.data.remote.catalog.AmbitoActividadResponse
import ni.edu.uam.innovacion.data.remote.catalog.CategoriaDiemResponse
import ni.edu.uam.innovacion.data.remote.user.UsuarioResponse
import ni.edu.uam.innovacion.features.admin.presentation.components.AdminPageHeader
import ni.edu.uam.innovacion.features.admin.presentation.components.DetailRow
import ni.edu.uam.innovacion.features.admin.presentation.components.EmptyState
import ni.edu.uam.innovacion.features.admin.presentation.components.ErrorState
import ni.edu.uam.innovacion.features.admin.presentation.components.LoadingState
import ni.edu.uam.innovacion.features.admin.presentation.components.SearchField
import ni.edu.uam.innovacion.features.admin.presentation.components.StatusPill
import ni.edu.uam.innovacion.ui.theme.UamTurquoise

@Composable
fun ActivitiesScreen(
    viewModel: ActivitiesViewModel,
    onSessionExpired: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }
    var editingActivity by remember { mutableStateOf<ActividadResponse?>(null) }

    LaunchedEffect(Unit) { viewModel.load() }
    LaunchedEffect(state.sessionExpired) {
        if (state.sessionExpired) onSessionExpired()
    }

    val filtered = state.actividades
        .filter {
            state.estadoFilter == "todos" || it.estado.equals(state.estadoFilter, ignoreCase = true)
        }
        .filter {
            val query = state.search.trim()
            query.isBlank() ||
                it.nombre.contains(query, ignoreCase = true) ||
                it.nombreAmbitoActividad.contains(query, ignoreCase = true) ||
                it.modalidad.contains(query, ignoreCase = true)
        }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AdminPageHeader(
                title = "Actividades",
                subtitle = "Gestion de actividades y estados operativos",
                actionLabel = "Nueva",
                actionIcon = Icons.Filled.Add,
                onAction = { showCreateDialog = true },
                onRefresh = viewModel::load,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        item {
            SearchField(
                value = state.search,
                onValueChange = viewModel::updateSearch,
                placeholder = "Buscar actividad, ambito o modalidad"
            )
        }
        item {
            EstadoFilters(
                selected = state.estadoFilter,
                estados = listOf("todos") + state.actividades.map { it.estado }.distinct().sorted(),
                onSelected = viewModel::updateEstadoFilter
            )
        }
        state.successMessage?.let { message ->
            item { SuccessBanner(message = message, onDismiss = viewModel::clearMessages) }
        }
        state.errorMessage?.let { message ->
            item { ErrorState(message = message, onRetry = viewModel::load) }
        }
        if (state.isLoading && state.actividades.isEmpty()) {
            item { LoadingState() }
        } else if (filtered.isEmpty()) {
            item {
                EmptyState(
                    title = "Sin actividades",
                    message = "No hay actividades reales que coincidan con el filtro actual."
                )
            }
        } else {
            items(filtered, key = { it.idActividad }) { actividad ->
                ActivityCard(
                    actividad = actividad,
                    onEdit = { editingActivity = actividad },
                    onPublicar = { viewModel.publicar(actividad.idActividad) },
                    onIniciar = { viewModel.iniciar(actividad.idActividad) },
                    onFinalizar = { viewModel.finalizar(actividad.idActividad) },
                    onCancelar = { viewModel.cancelar(actividad.idActividad) },
                    onArchivar = { viewModel.archivar(actividad.idActividad) }
                )
            }
        }
    }

    if (showCreateDialog) {
        ActivityFormDialog(
            title = "Nueva actividad",
            ambitos = state.ambitos,
            categorias = state.categorias,
            usuarios = state.usuarios,
            isWorking = state.isWorking,
            onDismiss = { showCreateDialog = false },
            onSubmit = {
                viewModel.crear(it)
                showCreateDialog = false
            }
        )
    }
    editingActivity?.let { activity ->
        ActivityFormDialog(
            title = "Editar actividad",
            activity = activity,
            ambitos = state.ambitos,
            categorias = state.categorias,
            usuarios = state.usuarios,
            isWorking = state.isWorking,
            onDismiss = { editingActivity = null },
            onSubmit = {
                viewModel.actualizar(activity.idActividad, it)
                editingActivity = null
            }
        )
    }
}

@Composable
private fun EstadoFilters(
    selected: String,
    estados: List<String>,
    onSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        estados.chunked(3).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                rowItems.forEach { estado ->
                    val active = estado.equals(selected, ignoreCase = true)
                    if (active) {
                        Button(
                            onClick = { onSelected(estado) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(estado.replace('_', ' '), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    } else {
                        OutlinedButton(
                            onClick = { onSelected(estado) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(estado.replace('_', ' '), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
                repeat(3 - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun ActivityCard(
    actividad: ActividadResponse,
    onEdit: () -> Unit,
    onPublicar: () -> Unit,
    onIniciar: () -> Unit,
    onFinalizar: () -> Unit,
    onCancelar: () -> Unit,
    onArchivar: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        actividad.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        actividad.nombreAmbitoActividad,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusPill(actividad.estado)
            }
            DetailRow("Modalidad", actividad.modalidad)
            DetailRow("Inicio", actividad.fechaInicio)
            DetailRow("Fin", actividad.fechaFin)
            DetailRow("Responsable", actividad.nombreResponsableUsuario ?: actividad.responsableNombre)
            DetailRow("Puntos base", actividad.puntosBase.toString())
            ActivityActions(
                estado = actividad.estado,
                onEdit = onEdit,
                onPublicar = onPublicar,
                onIniciar = onIniciar,
                onFinalizar = onFinalizar,
                onCancelar = onCancelar,
                onArchivar = onArchivar
            )
        }
    }
}

@Composable
private fun ActivityActions(
    estado: String,
    onEdit: () -> Unit,
    onPublicar: () -> Unit,
    onIniciar: () -> Unit,
    onFinalizar: () -> Unit,
    onCancelar: () -> Unit,
    onArchivar: () -> Unit
) {
    val actions = buildList {
        add(ActivityUiAction("Editar", Icons.Filled.Edit, onEdit))
        when (estado.lowercase()) {
            "borrador" -> {
                add(ActivityUiAction("Publicar", Icons.Filled.Publish, onPublicar))
                add(ActivityUiAction("Cancelar", Icons.Filled.Cancel, onCancelar))
            }
            "publicada" -> {
                add(ActivityUiAction("Iniciar", Icons.Filled.PlayArrow, onIniciar))
                add(ActivityUiAction("Finalizar", Icons.Filled.Flag, onFinalizar))
                add(ActivityUiAction("Cancelar", Icons.Filled.Cancel, onCancelar))
            }
            "en_curso" -> {
                add(ActivityUiAction("Finalizar", Icons.Filled.Flag, onFinalizar))
                add(ActivityUiAction("Cancelar", Icons.Filled.Cancel, onCancelar))
            }
            "finalizada", "cancelada" -> {
                add(ActivityUiAction("Archivar", Icons.Filled.Archive, onArchivar))
            }
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        actions.chunked(2).forEach { rowActions ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                rowActions.forEach { action ->
                    TextButton(
                        onClick = action.onClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(action.icon, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text(action.label, maxLines = 1)
                    }
                }
                repeat(2 - rowActions.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun ActivityFormDialog(
    title: String,
    ambitos: List<AmbitoActividadResponse>,
    categorias: List<CategoriaDiemResponse>,
    usuarios: List<UsuarioResponse>,
    isWorking: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (ActivityFormState) -> Unit,
    activity: ActividadResponse? = null
) {
    var form by remember(activity?.idActividad, ambitos.firstOrNull()?.id) {
        mutableStateOf(
            activity?.toFormState() ?: ActivityFormState(
                idAmbitoActividad = ambitos.firstOrNull()?.id?.toString().orEmpty(),
                idCategoriaDiem = categorias.firstOrNull()?.id?.toString().orEmpty()
            )
        )
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onSubmit(form) }, enabled = !isWorking) {
                Text("Guardar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 560.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DropdownSelector(
                    label = "Ambito",
                    value = ambitos.firstOrNull { it.id.toString() == form.idAmbitoActividad }?.nombre.orEmpty(),
                    options = ambitos.map { "${it.id} - ${it.nombre}" },
                    onSelected = { form = form.copy(idAmbitoActividad = it.substringBefore(" - ")) }
                )
                DropdownSelector(
                    label = "Categoria DIEM",
                    value = categorias.firstOrNull { it.id.toString() == form.idCategoriaDiem }?.nombre.orEmpty(),
                    options = listOf("Sin categoria") + categorias.map { "${it.id} - ${it.nombre}" },
                    onSelected = {
                        form = form.copy(
                            idCategoriaDiem = if (it == "Sin categoria") "" else it.substringBefore(" - ")
                        )
                    }
                )
                DialogTextField("Nombre", form.nombre) { form = form.copy(nombre = it) }
                DialogTextField("Descripcion", form.descripcion) { form = form.copy(descripcion = it) }
                DialogTextField("Fecha inicio ISO", form.fechaInicio) { form = form.copy(fechaInicio = it) }
                DialogTextField("Fecha fin ISO", form.fechaFin) { form = form.copy(fechaFin = it) }
                DialogTextField("Modalidad", form.modalidad) { form = form.copy(modalidad = it) }
                DialogTextField("Cupo maximo", form.cupoMaximo) { form = form.copy(cupoMaximo = it) }
                DialogTextField("Ubicacion", form.ubicacion) { form = form.copy(ubicacion = it) }
                DropdownSelector(
                    label = "Responsable usuario",
                    value = usuarios.firstOrNull {
                        it.idUsuario.toString() == form.idResponsableUsuario
                    }?.nombreCompleto.orEmpty(),
                    options = listOf("Sin responsable") + usuarios.map { "${it.idUsuario} - ${it.nombreCompleto}" },
                    onSelected = {
                        form = form.copy(
                            idResponsableUsuario = if (it == "Sin responsable") "" else it.substringBefore(" - ")
                        )
                    }
                )
                DialogTextField("Responsable texto", form.responsableNombre) {
                    form = form.copy(responsableNombre = it)
                }
                DialogTextField("Puntos base", form.puntosBase) { form = form.copy(puntosBase = it) }
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun DialogTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = false,
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun DropdownSelector(
    label: String,
    value: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = UamTurquoise)
                Text(value.ifBlank { "Seleccionar" }, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SuccessBanner(message: String, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = UamTurquoise.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(message, color = UamTurquoise, fontWeight = FontWeight.Medium)
            TextButton(onClick = onDismiss) { Text("Ocultar") }
        }
    }
}

private fun ActividadResponse.toFormState(): ActivityFormState =
    ActivityFormState(
        idAmbitoActividad = idAmbitoActividad.toString(),
        idCategoriaDiem = idCategoriaDiem?.toString().orEmpty(),
        idResponsableUsuario = idResponsableUsuario?.toString().orEmpty(),
        nombre = nombre,
        descripcion = descripcion.orEmpty(),
        fechaInicio = fechaInicio,
        fechaFin = fechaFin.orEmpty(),
        modalidad = modalidad,
        cupoMaximo = cupoMaximo?.toString().orEmpty(),
        ubicacion = ubicacion.orEmpty(),
        responsableNombre = responsableNombre.orEmpty(),
        puntosBase = puntosBase.toString()
    )

private data class ActivityUiAction(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)
