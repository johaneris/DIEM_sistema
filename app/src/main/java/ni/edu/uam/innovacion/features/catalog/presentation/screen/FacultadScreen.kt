package ni.edu.uam.innovacion.features.catalog.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultadScreen(onBack: () -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("TODOS") }
    var showDialog by remember { mutableStateOf(false) }

    val uamNavy = Color(0xFF003366)
    val uamTeal = Color(0xFF12A6AA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Facultades",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = uamNavy
                )
                Text(
                    text = "Administración de facultades del sistema",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = uamTeal),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nueva Facultad")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Filters Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar facultad...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("TODOS", "ACTIVO", "INACTIVO", "ARCHIVADO").forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    selectedStatus = status
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Table Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ID", modifier = Modifier.width(60.dp), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text("NOMBRE", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text("CÓDIGO", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text("ESTADO", modifier = Modifier.width(120.dp), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text("ACCIONES", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            }
        }

        // Table Body
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf(
                FacultadRowData(1, "Facultad de Ingeniería", "FIC", "ACTIVO"),
                FacultadRowData(2, "Facultad de Ciencias Económicas", "FCE", "ACTIVO"),
                FacultadRowData(3, "Facultad de Derecho", "FDC", "INACTIVO"),
                FacultadRowData(4, "Facultad de Medicina", "FAM", "ACTIVO")
            )) { facultad ->
                FacultadRow(facultad)
            }
        }
    }

    if (showDialog) {
        FacultadFormDialog(onDismiss = { showDialog = false })
    }
}

data class FacultadRowData(val id: Int, val nombre: String, val codigo: String, val estado: String)

@Composable
fun FacultadRow(facultad: FacultadRowData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "#${facultad.id}", modifier = Modifier.width(60.dp), fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = facultad.nombre, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
            Text(text = facultad.codigo, modifier = Modifier.width(100.dp), fontSize = 14.sp)
            
            StatusBadge(estado = facultad.estado, modifier = Modifier.width(120.dp))

            Row(modifier = Modifier.width(150.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = {}) { Icon(Icons.Outlined.Visibility, contentDescription = "Ver", tint = Color.Gray, modifier = Modifier.size(20.dp)) }
                IconButton(onClick = {}) { Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = Color.Gray, modifier = Modifier.size(20.dp)) }
                IconButton(onClick = {}) { Icon(Icons.Outlined.MoreVert, contentDescription = "Más", tint = Color.Gray, modifier = Modifier.size(20.dp)) }
            }
        }
    }
}

@Composable
fun StatusBadge(estado: String, modifier: Modifier = Modifier) {
    val color = when(estado) {
        "ACTIVO" -> Color(0xFF10B981)
        "INACTIVO" -> Color(0xFFEF4444)
        "ARCHIVADO" -> Color(0xFF64748B)
        else -> Color.Gray
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(end = 16.dp)
    ) {
        Text(
            text = estado,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultadFormDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Facultad", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Nombre de la Facultad") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Código") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), minLines = 3)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF12A6AA))) {
                Text("Guardar Facultad")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
