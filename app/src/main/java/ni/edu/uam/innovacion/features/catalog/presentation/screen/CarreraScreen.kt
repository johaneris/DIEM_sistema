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
fun CarreraScreen(onBack: () -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFacultad by remember { mutableStateOf("TODAS LAS FACULTADES") }
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
                    text = "Carreras",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = uamNavy
                )
                Text(
                    text = "Administración de carreras académicas por facultad",
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
                Text("Nueva Carrera")
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
                    placeholder = { Text("Buscar carrera...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedFacultad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Facultad") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true).fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("TODAS LAS FACULTADES", "Ingeniería", "Ciencias Económicas", "Derecho", "Medicina").forEach { facultad ->
                            DropdownMenuItem(
                                text = { Text(facultad) },
                                onClick = {
                                    selectedFacultad = facultad
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
                Text("ID", modifier = Modifier.width(50.dp), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text("NOMBRE", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text("CÓDIGO", modifier = Modifier.width(90.dp), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text("FACULTAD", modifier = Modifier.width(180.dp), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text("ESTADO", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Text("ACCIONES", modifier = Modifier.width(140.dp), fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            }
        }

        // Table Body
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf(
                CarreraRowData(1, "Ingeniería en Sistemas", "IS", "Facultad de Ingeniería", "ACTIVO"),
                CarreraRowData(2, "Marketing", "MKT", "Facultad de Ciencias Económicas", "ACTIVO"),
                CarreraRowData(3, "Derecho", "DER", "Facultad de Derecho", "ACTIVO"),
                CarreraRowData(4, "Medicina General", "MED", "Facultad de Medicina", "INACTIVO")
            )) { carrera ->
                CarreraRow(carrera)
            }
        }
    }

    if (showDialog) {
        CarreraFormDialog(onDismiss = { showDialog = false })
    }
}

data class CarreraRowData(val id: Int, val nombre: String, val codigo: String, val facultad: String, val estado: String)

@Composable
fun CarreraRow(carrera: CarreraRowData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "#${carrera.id}", modifier = Modifier.width(50.dp), fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = carrera.nombre, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
            Text(text = carrera.codigo, modifier = Modifier.width(90.dp), fontSize = 14.sp)
            Text(text = carrera.facultad, modifier = Modifier.width(180.dp), fontSize = 13.sp, color = Color.Gray)
            
            StatusBadge(estado = carrera.estado, modifier = Modifier.width(100.dp))

            Row(modifier = Modifier.width(140.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = {}) { Icon(Icons.Outlined.Visibility, contentDescription = "Ver", tint = Color.Gray, modifier = Modifier.size(20.dp)) }
                IconButton(onClick = {}) { Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = Color.Gray, modifier = Modifier.size(20.dp)) }
                IconButton(onClick = {}) { Icon(Icons.Outlined.MoreVert, contentDescription = "Más", tint = Color.Gray, modifier = Modifier.size(20.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarreraFormDialog(onDismiss: () -> Unit) {
    var selectedFacultadForm by remember { mutableStateOf("Seleccionar Facultad") }
    var expandedForm by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Carrera", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Nombre de la Carrera") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Código") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                
                ExposedDropdownMenuBox(
                    expanded = expandedForm,
                    onExpandedChange = { expandedForm = !expandedForm }
                ) {
                    OutlinedTextField(
                        value = selectedFacultadForm,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Facultad") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedForm) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true).fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedForm,
                        onDismissRequest = { expandedForm = false }
                    ) {
                        listOf("Ingeniería", "Ciencias Económicas", "Derecho", "Medicina").forEach { facultad ->
                            DropdownMenuItem(
                                text = { Text(facultad) },
                                onClick = {
                                    selectedFacultadForm = facultad
                                    expandedForm = false
                                }
                            )
                        }
                    }
                }
                
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), minLines = 2)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF12A6AA))) {
                Text("Guardar Carrera")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
