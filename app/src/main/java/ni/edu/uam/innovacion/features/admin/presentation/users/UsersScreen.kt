package ni.edu.uam.innovacion.features.admin.presentation.users

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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ni.edu.uam.innovacion.data.remote.catalog.CarreraResponse
import ni.edu.uam.innovacion.data.remote.catalog.FacultadResponse
import ni.edu.uam.innovacion.data.remote.catalog.RolResponse
import ni.edu.uam.innovacion.data.remote.user.UsuarioResponse
import ni.edu.uam.innovacion.features.admin.presentation.components.AdminPageHeader
import ni.edu.uam.innovacion.features.admin.presentation.components.DetailRow
import ni.edu.uam.innovacion.features.admin.presentation.components.EmptyState
import ni.edu.uam.innovacion.features.admin.presentation.components.ErrorState
import ni.edu.uam.innovacion.features.admin.presentation.components.ItemDivider
import ni.edu.uam.innovacion.features.admin.presentation.components.LoadingState
import ni.edu.uam.innovacion.features.admin.presentation.components.SearchField
import ni.edu.uam.innovacion.features.admin.presentation.components.StatusPill
import ni.edu.uam.innovacion.ui.theme.UamTurquoise

@Composable
fun UsersScreen(
    viewModel: UsersViewModel,
    onSessionExpired: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var editingUser by remember { mutableStateOf<UsuarioResponse?>(null) }
    var changingPasswordFor by remember { mutableStateOf<UsuarioResponse?>(null) }
    var assigningRoleFor by remember { mutableStateOf<UsuarioResponse?>(null) }
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.load() }
    LaunchedEffect(state.sessionExpired) {
        if (state.sessionExpired) onSessionExpired()
    }

    val filteredUsers = state.usuarios.filter { user ->
        val query = state.search.trim()
        query.isBlank() ||
            user.nombreCompleto.contains(query, ignoreCase = true) ||
            user.correo.contains(query, ignoreCase = true) ||
            user.documento.contains(query, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AdminPageHeader(
                title = "Usuarios",
                subtitle = "Gestion de cuentas y roles del sistema",
                actionLabel = "Nuevo",
                actionIcon = Icons.Filled.PersonAdd,
                onAction = { showCreateDialog = true },
                onRefresh = viewModel::load,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        item {
            SearchField(
                value = state.search,
                onValueChange = viewModel::updateSearch,
                placeholder = "Buscar por nombre, correo o documento"
            )
        }
        state.successMessage?.let { message ->
            item { SuccessBanner(message = message, onDismiss = viewModel::clearMessages) }
        }
        state.errorMessage?.let { message ->
            item { ErrorState(message = message, onRetry = viewModel::load) }
        }
        if (state.isLoading && state.usuarios.isEmpty()) {
            item { LoadingState() }
        } else if (filteredUsers.isEmpty()) {
            item {
                EmptyState(
                    title = "Sin usuarios",
                    message = "No hay registros reales que coincidan con la busqueda."
                )
            }
        } else {
            items(filteredUsers, key = { it.idUsuario }) { user ->
                UserCard(
                    user = user,
                    onEdit = { editingUser = user },
                    onPassword = { changingPasswordFor = user },
                    onAssignRole = { assigningRoleFor = user }
                )
            }
        }
        item { Spacer(modifier = Modifier.padding(bottom = 12.dp)) }
    }

    if (showCreateDialog) {
        UserFormDialog(
            title = "Nuevo usuario",
            isWorking = state.isWorking,
            onDismiss = { showCreateDialog = false },
            onSubmit = {
                viewModel.crearUsuario(it)
                showCreateDialog = false
            }
        )
    }
    editingUser?.let { user ->
        UserFormDialog(
            title = "Editar usuario",
            user = user,
            isWorking = state.isWorking,
            onDismiss = { editingUser = null },
            onSubmit = {
                viewModel.actualizarUsuario(user.idUsuario, it)
                editingUser = null
            }
        )
    }
    changingPasswordFor?.let { user ->
        PasswordDialog(
            user = user,
            isWorking = state.isWorking,
            onDismiss = { changingPasswordFor = null },
            onSubmit = {
                viewModel.cambiarContrasena(user.idUsuario, it)
                changingPasswordFor = null
            }
        )
    }
    assigningRoleFor?.let { user ->
        RoleDialog(
            user = user,
            roles = state.roles,
            isWorking = state.isWorking,
            onDismiss = { assigningRoleFor = null },
            onSubmit = {
                viewModel.asignarRol(user.idUsuario, it)
                assigningRoleFor = null
            }
        )
    }
}

@Composable
fun ProfilesScreen(
    kind: ProfileKind,
    viewModel: UsersViewModel,
    onSessionExpired: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showCreateProfile by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(kind) { viewModel.load() }
    LaunchedEffect(state.sessionExpired) {
        if (state.sessionExpired) onSessionExpired()
    }

    val profiles = state.usuarios
        .filter { it.hasProfile(kind) }
        .filter { user ->
            val query = state.search.trim()
            query.isBlank() ||
                user.nombreCompleto.contains(query, ignoreCase = true) ||
                user.correo.contains(query, ignoreCase = true) ||
                user.documento.contains(query, ignoreCase = true)
        }
    val eligibleUsers = state.usuarios.filter { !it.hasProfile(kind) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AdminPageHeader(
                title = kind.title,
                subtitle = "Listado derivado desde usuarios con perfil real asociado",
                actionLabel = "Crear perfil",
                actionIcon = Icons.Filled.Badge,
                onAction = { showCreateProfile = true },
                onRefresh = viewModel::load,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        item {
            SearchField(
                value = state.search,
                onValueChange = viewModel::updateSearch,
                placeholder = "Buscar perfil por usuario"
            )
        }
        state.successMessage?.let { message ->
            item { SuccessBanner(message = message, onDismiss = viewModel::clearMessages) }
        }
        state.errorMessage?.let { message ->
            item { ErrorState(message = message, onRetry = viewModel::load) }
        }
        if (state.isLoading && state.usuarios.isEmpty()) {
            item { LoadingState() }
        } else if (profiles.isEmpty()) {
            item {
                EmptyState(
                    title = "Sin perfiles",
                    message = "Aun no existen usuarios con este perfil en las respuestas reales."
                )
            }
        } else {
            items(profiles, key = { it.idUsuario }) { user ->
                ProfileCard(user = user, kind = kind)
            }
        }
    }

    if (showCreateProfile) {
        ProfileFormDialog(
            kind = kind,
            users = eligibleUsers,
            carreras = state.carreras,
            facultades = state.facultades,
            isWorking = state.isWorking,
            onDismiss = { showCreateProfile = false },
            onSubmit = { idUsuario, form ->
                viewModel.crearPerfil(kind, idUsuario, form)
                showCreateProfile = false
            }
        )
    }
}

@Composable
private fun UserCard(
    user: UsuarioResponse,
    onEdit: () -> Unit,
    onPassword: () -> Unit,
    onAssignRole: () -> Unit
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
                        text = user.nombreCompleto,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = user.correo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                StatusPill(status = user.estado)
            }
            DetailRow("Documento", user.documento)
            DetailRow("Roles", user.roles.joinToString { it.nombre }.ifBlank { "Sin roles" })
            ItemDivider()
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TextButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Editar")
                }
                TextButton(onClick = onPassword) {
                    Icon(Icons.Filled.Key, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Clave")
                }
                TextButton(onClick = onAssignRole) {
                    Icon(Icons.Filled.Security, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Rol")
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(user: UsuarioResponse, kind: ProfileKind) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(user.nombreCompleto, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(user.correo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            when (kind) {
                ProfileKind.Administrador -> {
                    val profile = user.perfilAdministrador
                    DetailRow("Cargo", profile?.cargo)
                    DetailRow("Nivel de acceso", profile?.nivelAcceso)
                }

                ProfileKind.Mentor -> {
                    val profile = user.perfilMentor
                    DetailRow("Area", profile?.areaExperiencia)
                    DetailRow("Especialidad", profile?.especialidad)
                    DetailRow("Institucion", profile?.institucion)
                }

                ProfileKind.Docente -> {
                    val profile = user.perfilDocente
                    DetailRow("Area academica", profile?.areaAcademica)
                    DetailRow("Cargo", profile?.cargo)
                    DetailRow("Titulo", profile?.tituloUniversitario)
                }

                ProfileKind.Estudiante -> {
                    val profile = user.perfilEstudiante
                    DetailRow("CIF", profile?.cif)
                    DetailRow("Correo institucional", profile?.correoInstitucional)
                    DetailRow("Carrera principal", profile?.idCarreraPrincipal?.toString())
                }

                ProfileKind.Externo -> {
                    val profile = user.perfilParticipanteExterno
                    DetailRow("Ocupacion", profile?.ocupacion)
                    DetailRow("Institucion", profile?.institucionProcedencia)
                }
            }
        }
    }
}

@Composable
private fun UserFormDialog(
    title: String,
    isWorking: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (UsuarioFormState) -> Unit,
    user: UsuarioResponse? = null
) {
    var form by remember(user?.idUsuario) {
        mutableStateOf(
            UsuarioFormState(
                nombreCompleto = user?.nombreCompleto.orEmpty(),
                documento = user?.documento.orEmpty(),
                telefono = user?.telefono.orEmpty(),
                correo = user?.correo.orEmpty(),
                sexo = user?.sexo.orEmpty(),
                tallaCamisa = user?.tallaCamisa.orEmpty()
            )
        )
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onSubmit(form) }, enabled = !isWorking) {
                if (isWorking) CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                Text("Guardar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DialogTextField("Nombre completo", form.nombreCompleto) {
                    form = form.copy(nombreCompleto = it)
                }
                DialogTextField("Documento", form.documento) { form = form.copy(documento = it) }
                DialogTextField("Correo", form.correo) { form = form.copy(correo = it) }
                if (user == null) {
                    DialogTextField("Contrasena", form.contrasena) {
                        form = form.copy(contrasena = it)
                    }
                }
                DialogTextField("Telefono", form.telefono) { form = form.copy(telefono = it) }
                DialogTextField("Sexo", form.sexo) { form = form.copy(sexo = it) }
                DialogTextField("Talla camisa", form.tallaCamisa) {
                    form = form.copy(tallaCamisa = it)
                }
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun PasswordDialog(
    user: UsuarioResponse,
    isWorking: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onSubmit(password) }, enabled = !isWorking) { Text("Actualizar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        title = { Text("Cambiar contrasena") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(user.nombreCompleto, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Nueva contrasena") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun RoleDialog(
    user: UsuarioResponse,
    roles: List<RolResponse>,
    isWorking: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var selectedRole by rememberSaveable { mutableStateOf(roles.firstOrNull()?.nombre.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onSubmit(selectedRole) }, enabled = !isWorking && selectedRole.isNotBlank()) {
                Text("Asignar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        title = { Text("Asignar rol") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(user.nombreCompleto, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (roles.isEmpty()) {
                    DialogTextField("Rol", selectedRole) { selectedRole = it }
                } else {
                    DropdownSelector(
                        label = "Rol",
                        value = selectedRole,
                        options = roles.map { it.nombre },
                        onSelected = { selectedRole = it }
                    )
                }
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun ProfileFormDialog(
    kind: ProfileKind,
    users: List<UsuarioResponse>,
    carreras: List<CarreraResponse>,
    facultades: List<FacultadResponse>,
    isWorking: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (Long, ProfileFormState) -> Unit
) {
    var selectedUserId by rememberSaveable { mutableStateOf(users.firstOrNull()?.idUsuario) }
    var form by remember { mutableStateOf(ProfileFormState()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { selectedUserId?.let { onSubmit(it, form) } },
                enabled = !isWorking && selectedUserId != null
            ) {
                Text("Crear")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        title = { Text("Crear perfil ${kind.title.lowercase()}") },
        text = {
            Column(
                modifier = Modifier.heightIn(max = 520.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (users.isEmpty()) {
                    Text("No hay usuarios disponibles sin este perfil.")
                } else {
                    DropdownSelector(
                        label = "Usuario",
                        value = users.firstOrNull { it.idUsuario == selectedUserId }?.nombreCompleto.orEmpty(),
                        options = users.map { "${it.idUsuario} - ${it.nombreCompleto}" },
                        onSelected = { selected ->
                            selectedUserId = selected.substringBefore(" - ").toLongOrNull()
                        }
                    )
                    when (kind) {
                        ProfileKind.Administrador -> {
                            DialogTextField("Cargo", form.cargo) { form = form.copy(cargo = it) }
                            DialogTextField("Nivel acceso", form.nivelAcceso) {
                                form = form.copy(nivelAcceso = it)
                            }
                        }

                        ProfileKind.Estudiante -> {
                            DialogTextField("CIF", form.cif) { form = form.copy(cif = it) }
                            DialogTextField("Correo institucional", form.correoInstitucional) {
                                form = form.copy(correoInstitucional = it)
                            }
                            DropdownSelector(
                                label = "Carrera principal",
                                value = carreras.firstOrNull {
                                    it.id.toString() == form.idCarreraPrincipal
                                }?.nombre.orEmpty(),
                                options = carreras.map { "${it.id} - ${it.nombre}" },
                                onSelected = {
                                    form = form.copy(idCarreraPrincipal = it.substringBefore(" - "))
                                }
                            )
                        }

                        ProfileKind.Docente -> {
                            DialogTextField("Area academica", form.areaAcademica) {
                                form = form.copy(areaAcademica = it)
                            }
                            DialogTextField("Cargo", form.cargo) { form = form.copy(cargo = it) }
                            DialogTextField("Grado academico", form.gradoAcademico) {
                                form = form.copy(gradoAcademico = it)
                            }
                            DialogTextField("Titulo universitario", form.tituloUniversitario) {
                                form = form.copy(tituloUniversitario = it)
                            }
                            DropdownSelector(
                                label = "Facultad",
                                value = facultades.firstOrNull {
                                    it.id.toString() == form.idFacultad
                                }?.nombre.orEmpty(),
                                options = facultades.map { "${it.id} - ${it.nombre}" },
                                onSelected = { form = form.copy(idFacultad = it.substringBefore(" - ")) }
                            )
                        }

                        ProfileKind.Mentor -> {
                            DialogTextField("Area experiencia", form.areaExperiencia) {
                                form = form.copy(areaExperiencia = it)
                            }
                            DialogTextField("Especialidad", form.especialidad) {
                                form = form.copy(especialidad = it)
                            }
                            DialogTextField("Institucion", form.institucion) {
                                form = form.copy(institucion = it)
                            }
                            DialogTextField("Tipo acompanamiento", form.tipoAcompanamiento) {
                                form = form.copy(tipoAcompanamiento = it)
                            }
                            DialogTextField("Grado academico", form.gradoAcademico) {
                                form = form.copy(gradoAcademico = it)
                            }
                            DialogTextField("Titulo universitario", form.tituloUniversitario) {
                                form = form.copy(tituloUniversitario = it)
                            }
                        }

                        ProfileKind.Externo -> {
                            DialogTextField("Ocupacion", form.ocupacion) {
                                form = form.copy(ocupacion = it)
                            }
                            DialogTextField("Institucion procedencia", form.institucionProcedencia) {
                                form = form.copy(institucionProcedencia = it)
                            }
                        }
                    }
                }
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun DialogTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
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

private fun UsuarioResponse.hasProfile(kind: ProfileKind): Boolean =
    when (kind) {
        ProfileKind.Administrador -> perfilAdministrador != null
        ProfileKind.Mentor -> perfilMentor != null
        ProfileKind.Docente -> perfilDocente != null
        ProfileKind.Estudiante -> perfilEstudiante != null
        ProfileKind.Externo -> perfilParticipanteExterno != null
    }
