package ni.edu.uam.innovacion.features.admin.presentation.catalogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ni.edu.uam.innovacion.features.admin.presentation.components.AdminPageHeader
import ni.edu.uam.innovacion.features.admin.presentation.components.DetailRow
import ni.edu.uam.innovacion.features.admin.presentation.components.EmptyState
import ni.edu.uam.innovacion.features.admin.presentation.components.ErrorState
import ni.edu.uam.innovacion.features.admin.presentation.components.LoadingState
import ni.edu.uam.innovacion.features.admin.presentation.components.SearchField
import ni.edu.uam.innovacion.features.admin.presentation.components.StatusPill
import ni.edu.uam.innovacion.ui.theme.UamTurquoise

@Composable
fun CatalogsScreen(
    kind: CatalogKind,
    viewModel: CatalogsViewModel,
    onSessionExpired: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<CatalogItemUi?>(null) }

    LaunchedEffect(kind) { viewModel.load(kind) }
    LaunchedEffect(state.sessionExpired) {
        if (state.sessionExpired) onSessionExpired()
    }

    val filtered = state.items.filter {
        val query = state.search.trim()
        query.isBlank() ||
            it.nombre.contains(query, ignoreCase = true) ||
            it.descripcion.orEmpty().contains(query, ignoreCase = true) ||
            it.codigo.orEmpty().contains(query, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AdminPageHeader(
                title = kind.title,
                subtitle = kind.subtitle,
                actionLabel = "Nuevo",
                actionIcon = Icons.Filled.Add,
                onAction = { showCreateDialog = true },
                onRefresh = { viewModel.load(kind) },
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        item {
            SearchField(
                value = state.search,
                onValueChange = viewModel::updateSearch,
                placeholder = "Buscar en ${kind.title.lowercase()}"
            )
        }
        state.successMessage?.let { message ->
            item { SuccessBanner(message = message, onDismiss = viewModel::clearMessages) }
        }
        state.errorMessage?.let { message ->
            item { ErrorState(message = message, onRetry = { viewModel.load(kind) }) }
        }
        if (state.isLoading && state.items.isEmpty()) {
            item { LoadingState() }
        } else if (filtered.isEmpty()) {
            item {
                EmptyState(
                    title = "Sin registros",
                    message = "Este catalogo aun no tiene datos reales disponibles."
                )
            }
        } else {
            items(filtered, key = { it.id }) { item ->
                CatalogItemCard(
                    item = item,
                    onEdit = { editingItem = item },
                    onActivate = { viewModel.activar(kind, item.id) },
                    onInactivate = { viewModel.inactivar(kind, item.id) },
                    onArchive = { viewModel.archivar(kind, item.id) }
                )
            }
        }
    }

    if (showCreateDialog) {
        CatalogFormDialog(
            kind = kind,
            relations = state.relations,
            isWorking = state.isWorking,
            onDismiss = { showCreateDialog = false },
            onSubmit = {
                viewModel.crear(kind, it)
                showCreateDialog = false
            }
        )
    }
    editingItem?.let { item ->
        CatalogFormDialog(
            kind = kind,
            item = item,
            relations = state.relations,
            isWorking = state.isWorking,
            onDismiss = { editingItem = null },
            onSubmit = {
                viewModel.actualizar(kind, item.id, it)
                editingItem = null
            }
        )
    }
}

@Composable
private fun CatalogItemCard(
    item: CatalogItemUi,
    onEdit: () -> Unit,
    onActivate: () -> Unit,
    onInactivate: () -> Unit,
    onArchive: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    item.descripcion?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                StatusPill(item.estado)
            }
            DetailRow("Codigo", item.codigo)
            DetailRow("Relacion", item.parentName)
            DetailRow("Categoria", item.categoria)
            DetailRow("Criterios", item.criterios)
            if (item.requiereCategoria) DetailRow("Requiere categoria", "Si")
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onEdit, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.Edit, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Editar")
                    }
                    if (item.estado.equals("activo", true) || item.estado.equals("activa", true)) {
                        TextButton(onClick = onInactivate, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Filled.ToggleOff, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Inactivar")
                        }
                    } else {
                        TextButton(onClick = onActivate, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Filled.ToggleOn, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Activar")
                        }
                    }
                }
                if (!item.estado.startsWith("archivad", ignoreCase = true)) {
                    TextButton(onClick = onArchive, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Filled.Archive, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Archivar")
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogFormDialog(
    kind: CatalogKind,
    relations: List<CatalogRelationOption>,
    isWorking: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (CatalogFormState) -> Unit,
    item: CatalogItemUi? = null
) {
    var form by remember(item?.id, relations.firstOrNull()?.id) {
        mutableStateOf(
            item?.toFormState() ?: CatalogFormState(
                parentId = relations.firstOrNull()?.id?.toString().orEmpty()
            )
        )
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onSubmit(form) }, enabled = !isWorking) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        title = { Text(if (item == null) "Nuevo registro" else "Editar registro") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DialogTextField("Nombre", form.nombre) { form = form.copy(nombre = it) }
                DialogTextField("Descripcion", form.descripcion) { form = form.copy(descripcion = it) }
                if (kind == CatalogKind.Facultades || kind == CatalogKind.Carreras) {
                    DialogTextField("Codigo", form.codigo) { form = form.copy(codigo = it) }
                }
                if (kind == CatalogKind.Carreras || kind == CatalogKind.CategoriasDiem) {
                    DropdownSelector(
                        label = if (kind == CatalogKind.Carreras) "Facultad" else "Ambito",
                        value = relations.firstOrNull { it.id.toString() == form.parentId }?.nombre.orEmpty(),
                        options = relations.map { "${it.id} - ${it.nombre}" },
                        onSelected = { form = form.copy(parentId = it.substringBefore(" - ")) }
                    )
                }
                if (kind == CatalogKind.CategoriasDiem) {
                    DialogTextField("Criterios puntuacion", form.criterios) {
                        form = form.copy(criterios = it)
                    }
                }
                if (kind == CatalogKind.FuentesProyecto) {
                    DialogTextField("Categoria", form.categoria) { form = form.copy(categoria = it) }
                }
                if (kind == CatalogKind.Ambitos) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = form.requiereCategoria,
                            onCheckedChange = { form = form.copy(requiereCategoria = it) }
                        )
                        Text("Requiere categoria DIEM")
                    }
                }
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

private fun CatalogItemUi.toFormState(): CatalogFormState =
    CatalogFormState(
        nombre = nombre,
        descripcion = descripcion.orEmpty(),
        codigo = codigo.orEmpty(),
        parentId = parentId?.toString().orEmpty(),
        categoria = categoria.orEmpty(),
        criterios = criterios.orEmpty(),
        requiereCategoria = requiereCategoria
    )
