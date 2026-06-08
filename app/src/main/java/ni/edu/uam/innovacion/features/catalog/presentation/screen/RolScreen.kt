package ni.edu.uam.innovacion.features.catalog.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.innovacion.features.catalog.data.model.RolResponse
import ni.edu.uam.innovacion.features.catalog.presentation.viewmodel.RolViewModel

@Composable
fun RolScreen(
    viewModel: RolViewModel = viewModel()
) {
    val roles by viewModel.roles.collectAsState()
    val error by viewModel.error.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var selectedRol by remember { mutableStateOf<RolResponse?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val uamTeal = Color(0xFF12A6AA)
    val uamNavy = Color(0xFF003366)
    val backgroundGray = Color(0xFFF8FAFC)

    LaunchedEffect(Unit) {
        viewModel.cargarRoles()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGray)
    ) {
        // Cabecera Profesional
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Catálogo de Roles",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = uamNavy
                )
                Text(
                    text = "Administración de roles institucionales y permisos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar rol...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.width(350.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            selectedRol = null
                            showForm = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = uamTeal),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Nuevo Rol", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Box(modifier = Modifier.padding(24.dp)) {
            error?.let {
                Text(text = "Error: $it", color = Color.Red, modifier = Modifier.padding(bottom = 16.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ROL Y DESCRIPCIÓN", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.Gray)
                        Text("ESTADO", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.Gray)
                        Text("ACCIONES", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.Gray)
                    }
                }

                val filteredList = roles.filter { it.nombre.contains(searchQuery, ignoreCase = true) }

                items(filteredList) { rol ->
                    RolRow(
                        rol = rol,
                        onEdit = {
                            selectedRol = rol
                            showForm = true
                        },
                        onToggleStatus = {
                            if (rol.estado == "ACTIVO") viewModel.inactivarRol(rol.id)
                            else viewModel.activarRol(rol.id)
                        },
                        onArchive = { viewModel.archivarRol(rol.id) }
                    )
                }
            }
        }
    }

    if (showForm) {
        RolFormDialog(
            rol = selectedRol,
            onDismiss = { showForm = false },
            onSave = { nombre, descripcion ->
                if (selectedRol == null) {
                    viewModel.crearRol(nombre, descripcion)
                } else {
                    viewModel.actualizarRol(selectedRol!!.id, nombre, descripcion)
                }
                showForm = false
            }
        )
    }
}

@Composable
fun RolRow(
    rol: RolResponse,
    onEdit: () -> Unit,
    onToggleStatus: () -> Unit,
    onArchive: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.5f)) {
                Text(text = rol.nombre, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Text(text = rol.descripcion ?: "Sin descripción", color = Color.Gray, fontSize = 13.sp)
            }

            Box(modifier = Modifier.weight(1f)) {
                StatusBadge(rol.estado)
            }

            Box(modifier = Modifier.weight(0.5f)) {
                var showMenu by remember { mutableStateOf(false) }
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray)
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(text = { Text("Editar") }, onClick = { onEdit(); showMenu = false })
                    DropdownMenuItem(
                        text = { Text(if (rol.estado == "ACTIVO") "Inactivar" else "Activar") },
                        onClick = { onToggleStatus(); showMenu = false }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(text = { Text("Archivar", color = Color.Red) }, onClick = { onArchive(); showMenu = false })
                }
            }
        }
    }
}

@Composable
fun RolFormDialog(
    rol: RolResponse?,
    onDismiss: () -> Unit,
    onSave: (String, String?) -> Unit
) {
    var nombre by remember { mutableStateOf(rol?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(rol?.descripcion ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (rol == null) "Nuevo Rol" else "Editar Rol", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Rol") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(nombre, descripcion.ifBlank { null }) },
                enabled = nombre.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF12A6AA)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
