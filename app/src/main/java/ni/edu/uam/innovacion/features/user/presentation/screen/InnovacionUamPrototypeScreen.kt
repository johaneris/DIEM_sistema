package ni.edu.uam.innovacion.features.user.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ni.edu.uam.innovacion.ui.theme.Sistema_Innovacion_FrontendTheme
import ni.edu.uam.innovacion.ui.theme.UamGray
import ni.edu.uam.innovacion.ui.theme.UamInnovationYellow
import ni.edu.uam.innovacion.ui.theme.UamLightBackground
import ni.edu.uam.innovacion.ui.theme.UamTextDark
import ni.edu.uam.innovacion.ui.theme.UamTurquoise
import ni.edu.uam.innovacion.ui.theme.UamTurquoiseAccent
import ni.edu.uam.innovacion.ui.theme.UamTurquoiseSecondary
import ni.edu.uam.innovacion.ui.theme.UamWhite

@Composable
fun InnovacionUamPrototypeScreen() {
    var selectedTab by remember { mutableStateOf(PrototypeTab.Panel) }
    var nextUserId by remember { mutableIntStateOf(4) }

    val roles = remember {
        mutableStateListOf(
            RoleUi("estudiante", "Usuario estudiante del sistema"),
            RoleUi("administrador", "Usuario con permisos administrativos"),
            RoleUi("docente", "Usuario docente"),
            RoleUi("mentor", "Usuario mentor"),
            RoleUi("participante_externo", "Usuario externo participante")
        )
    }
    val users = remember {
        mutableStateListOf(
            UserUi(
                id = 1,
                name = "Camila Ruiz",
                document = "001-010101-0001A",
                phone = "8888-0001",
                email = "camila.ruiz@uam.edu.ni",
                sex = "F",
                shirt = "M",
                roles = setOf("estudiante"),
                studentProfile = StudentProfileUi(
                    cif = "2026-0001",
                    institutionalEmail = "camila.ruiz@uam.edu.ni",
                    careerId = null,
                    dualDegree = false
                )
            ),
            UserUi(
                id = 2,
                name = "Diego Martinez",
                document = "001-020202-0002B",
                phone = "8888-0002",
                email = "diego.mentor@uam.edu.ni",
                sex = "M",
                shirt = "L",
                roles = setOf("mentor")
            ),
            UserUi(
                id = 3,
                name = "Adriana Solorzano",
                document = "001-030303-0003C",
                phone = "8888-0003",
                email = "innovacion@uam.edu.ni",
                sex = "F",
                shirt = "M",
                roles = setOf("administrador"),
                adminProfile = AdminProfileUi(
                    position = "Coordinadora de innovacion",
                    accessLevel = "total"
                )
            )
        )
    }
    val events = remember {
        mutableStateListOf(
            "Perfil estudiante creado para Camila Ruiz",
            "Rol mentor asignado a Diego Martinez",
            "Rol administrador activo para Adriana Solorzano"
        )
    }

    Scaffold(
        containerColor = UamLightBackground,
        bottomBar = {
            InnovationBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UamLightBackground)
                .padding(innerPadding)
        ) {
            AppHeader()
            when (selectedTab) {
                PrototypeTab.Panel -> AdminDashboardScreen(
                    users = users,
                    roles = roles,
                    events = events
                )

                PrototypeTab.Usuarios -> UsersScreen(
                    users = users,
                    onCreateUser = { input ->
                        users.add(
                            UserUi(
                                id = nextUserId++,
                                name = input.name,
                                document = input.document,
                                phone = input.phone,
                                email = input.email,
                                sex = input.sex,
                                shirt = input.shirt
                            )
                        )
                        events.add(0, "Usuario ${input.name} registrado desde la app")
                    },
                    onChangeStatus = { id, status ->
                        users.updateUser(id) { it.copy(status = status) }
                        events.add(0, "Estado de usuario actualizado a $status")
                    }
                )

                PrototypeTab.Roles -> RolesScreen(
                    users = users,
                    roles = roles,
                    onCreateRole = { role ->
                        roles.add(role)
                        events.add(0, "Rol ${role.name} creado para nuevos flujos")
                    },
                    onAssignRole = { userId, roleName ->
                        val userName = users.firstOrNull { it.id == userId }?.name.orEmpty()
                        users.updateUser(userId) { it.copy(roles = it.roles + roleName) }
                        events.add(0, "Rol $roleName asignado a $userName")
                    },
                    onDeactivateRole = { userId, roleName ->
                        val userName = users.firstOrNull { it.id == userId }?.name.orEmpty()
                        users.updateUser(userId) { it.copy(roles = it.roles - roleName) }
                        events.add(0, "Rol $roleName desactivado para $userName")
                    }
                )

                PrototypeTab.Perfiles -> ProfilesScreen(
                    users = users,
                    onCreateStudentProfile = { userId, profile ->
                        users.updateUser(userId) { it.copy(studentProfile = profile) }
                        val userName = users.firstOrNull { it.id == userId }?.name.orEmpty()
                        events.add(0, "Perfil estudiante completado para $userName")
                    },
                    onCreateAdminProfile = { userId, profile ->
                        users.updateUser(userId) { it.copy(adminProfile = profile) }
                        val userName = users.firstOrNull { it.id == userId }?.name.orEmpty()
                        events.add(0, "Perfil administrador completado para $userName")
                    }
                )
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(UamTurquoise, UamTurquoiseSecondary, UamTurquoiseAccent)
                )
            )
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(UamWhite),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "UAM",
                        color = UamTurquoise,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black
                    )
                }
                Column {
                    Text(
                        text = "Innovacion UAM",
                        color = UamWhite,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Direccion de Innovacion y Emprendimiento",
                        color = UamWhite.copy(alpha = 0.86f),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                text = "Gestion unificada de participantes, roles y perfiles base para talleres, hackathones, rallies, mentorias y Programa PIA.",
                color = UamWhite.copy(alpha = 0.92f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun InnovationBottomBar(
    selectedTab: PrototypeTab,
    onTabSelected: (PrototypeTab) -> Unit
) {
    NavigationBar(
        containerColor = UamWhite,
        tonalElevation = 8.dp
    ) {
        PrototypeTab.entries.forEach { tab ->
            val selected = selectedTab == tab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    NavGlyph(
                        text = tab.glyph,
                        selected = selected
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
private fun AdminDashboardScreen(
    users: List<UserUi>,
    roles: List<RoleUi>,
    events: List<String>
) {
    ScreenSurface {
        SectionTitle(
            title = "Panel administrativo",
            subtitle = "Vista rapida para la Direccion antes de generar reportes anuales reales."
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(
                label = "Participantes unicos",
                value = users.size.toString(),
                caption = "Base sin duplicidad",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                label = "Roles activos",
                value = users.sumOf { it.roles.size }.toString(),
                caption = "Asignaciones vigentes",
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(
                label = "Perfiles listos",
                value = users.count { it.studentProfile != null || it.adminProfile != null }.toString(),
                caption = "Estudiante/admin",
                modifier = Modifier.weight(1f),
                accent = UamInnovationYellow
            )
            MetricCard(
                label = "Roles base",
                value = roles.size.toString(),
                caption = "Catalogo actual",
                modifier = Modifier.weight(1f),
                accent = UamGray
            )
        }
        SectionCard {
            SectionTitle(
                title = "Endpoints reflejados",
                subtitle = "Estas interfaces corresponden al modulo backend de usuarios, roles y perfiles."
            )
            EndpointPill("POST /api/usuarios")
            EndpointPill("GET /api/usuarios")
            EndpointPill("POST /api/usuarios/{id}/roles")
            EndpointPill("DELETE /api/usuarios/{id}/roles/{rol}")
            EndpointPill("POST /api/usuarios/{id}/perfiles/estudiante")
            EndpointPill("POST /api/usuarios/{id}/perfiles/administrador")
        }
        SectionCard {
            SectionTitle(
                title = "Actividad reciente",
                subtitle = "Simulacion local de movimientos administrativos."
            )
            events.take(5).forEach { event ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(UamInnovationYellow)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = event,
                        color = UamTextDark,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun UsersScreen(
    users: List<UserUi>,
    onCreateUser: (UserInput) -> Unit,
    onChangeStatus: (Int, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var document by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var shirt by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    ScreenSurface {
        SectionTitle(
            title = "Participantes y usuarios",
            subtitle = "Registro base para evitar duplicidad entre talleres, hackathones, rallies y mentorias."
        )
        SectionCard {
            EndpointPill("POST /api/usuarios")
            Spacer(Modifier.height(10.dp))
            PrototypeTextField("Nombre completo", name, { name = it })
            PrototypeTextField("Documento", document, { document = it })
            PrototypeTextField("Telefono", phone, { phone = it }, keyboardType = KeyboardType.Phone)
            PrototypeTextField("Correo", email, { email = it }, keyboardType = KeyboardType.Email)
            PrototypeTextField(
                label = "Contrasena",
                value = password,
                onValueChange = { password = it },
                keyboardType = KeyboardType.Password,
                password = true
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PrototypeTextField(
                    label = "Sexo",
                    value = sex,
                    onValueChange = { sex = it },
                    modifier = Modifier.weight(1f)
                )
                PrototypeTextField(
                    label = "Talla",
                    value = shirt,
                    onValueChange = { shirt = it },
                    modifier = Modifier.weight(1f)
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                onClick = {
                    message = when {
                        name.isBlank() || document.isBlank() || email.isBlank() || password.length < 6 ->
                            "Completa nombre, documento, correo y una contrasena de al menos 6 caracteres."

                        users.any { it.email.equals(email.trim(), ignoreCase = true) } ->
                            "Ya existe un usuario con ese correo."

                        users.any { it.document.equals(document.trim(), ignoreCase = true) } ->
                            "Ya existe un usuario con ese documento."

                        else -> {
                            onCreateUser(
                                UserInput(
                                    name = name.trim(),
                                    document = document.trim(),
                                    phone = phone.trim(),
                                    email = email.trim().lowercase(),
                                    sex = sex.trim(),
                                    shirt = shirt.trim()
                                )
                            )
                            name = ""
                            document = ""
                            phone = ""
                            email = ""
                            password = ""
                            sex = ""
                            shirt = ""
                            "Usuario registrado localmente. Listo para asignar roles."
                        }
                    }
                }
            ) {
                Text("Registrar usuario")
            }
            message?.let {
                HelperMessage(text = it)
            }
        }

        SectionTitle(
            title = "Usuarios registrados",
            subtitle = "Cambio de estado equivalente a PATCH /api/usuarios/{id}/estado."
        )
        users.forEach { user ->
            UserCard(
                user = user,
                actions = {
                    StatusActions(
                        current = user.status,
                        onChange = { onChangeStatus(user.id, it) }
                    )
                }
            )
        }
    }
}

@Composable
private fun RolesScreen(
    users: List<UserUi>,
    roles: SnapshotStateList<RoleUi>,
    onCreateRole: (RoleUi) -> Unit,
    onAssignRole: (Int, String) -> Unit,
    onDeactivateRole: (Int, String) -> Unit
) {
    var roleName by remember { mutableStateOf("") }
    var roleDescription by remember { mutableStateOf("") }
    var selectedUserId by remember { mutableIntStateOf(users.firstOrNull()?.id ?: 0) }
    var selectedRole by remember { mutableStateOf(roles.firstOrNull()?.name.orEmpty()) }
    var message by remember { mutableStateOf<String?>(null) }

    val selectedUser = users.firstOrNull { it.id == selectedUserId } ?: users.firstOrNull()

    ScreenSurface {
        SectionTitle(
            title = "Roles del sistema",
            subtitle = "Gestiona el catalogo y las asignaciones multiples por participante."
        )
        SectionCard {
            EndpointPill("GET /api/roles")
            Spacer(Modifier.height(10.dp))
            roles.forEach { role ->
                RoleRow(role = role)
            }
        }

        SectionCard {
            EndpointPill("POST /api/roles")
            Spacer(Modifier.height(10.dp))
            PrototypeTextField("Nombre del rol", roleName, { roleName = it })
            PrototypeTextField("Descripcion", roleDescription, { roleDescription = it })
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                onClick = {
                    val normalized = roleName.trim().lowercase()
                    message = when {
                        normalized.isBlank() -> "Escribe el nombre del rol."
                        roles.any { it.name.equals(normalized, ignoreCase = true) } ->
                            "Ese rol ya existe."

                        else -> {
                            onCreateRole(
                                RoleUi(
                                    name = normalized,
                                    description = roleDescription.trim().ifBlank { "Rol personalizado" }
                                )
                            )
                            selectedRole = normalized
                            roleName = ""
                            roleDescription = ""
                            "Rol creado y disponible para asignacion."
                        }
                    }
                }
            ) {
                Text("Crear rol")
            }
            message?.let { HelperMessage(it) }
        }

        SectionCard {
            SectionTitle(
                title = "Asignar rol",
                subtitle = "Equivale a POST /api/usuarios/{id}/roles."
            )
            EndpointPill("POST /api/usuarios/{id}/roles")
            Spacer(Modifier.height(12.dp))
            Text("Usuario", color = UamTextDark, fontWeight = FontWeight.SemiBold)
            SelectableChipRow(
                items = users.map { it.id to it.name },
                selected = selectedUserId,
                onSelected = { selectedUserId = it }
            )
            Spacer(Modifier.height(8.dp))
            Text("Rol", color = UamTextDark, fontWeight = FontWeight.SemiBold)
            SelectableTextRow(
                items = roles.map { it.name },
                selected = selectedRole,
                onSelected = { selectedRole = it }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                    enabled = selectedUser != null && selectedRole.isNotBlank(),
                    onClick = {
                        selectedUser?.let { onAssignRole(it.id, selectedRole) }
                    }
                ) {
                    Text("Asignar")
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, UamGray),
                    enabled = selectedUser?.roles?.contains(selectedRole) == true,
                    onClick = {
                        selectedUser?.let { onDeactivateRole(it.id, selectedRole) }
                    }
                ) {
                    Text("Desactivar", color = UamTextDark)
                }
            }
        }
    }
}

@Composable
private fun ProfilesScreen(
    users: List<UserUi>,
    onCreateStudentProfile: (Int, StudentProfileUi) -> Unit,
    onCreateAdminProfile: (Int, AdminProfileUi) -> Unit
) {
    var selectedUserId by remember { mutableIntStateOf(users.firstOrNull()?.id ?: 0) }
    var cif by remember { mutableStateOf("") }
    var institutionalEmail by remember { mutableStateOf("") }
    var careerId by remember { mutableStateOf("") }
    var dualDegree by remember { mutableStateOf(false) }
    var position by remember { mutableStateOf("") }
    var accessLevel by remember { mutableStateOf("total") }
    var message by remember { mutableStateOf<String?>(null) }

    val selectedUser = users.firstOrNull { it.id == selectedUserId } ?: users.firstOrNull()
    val hasStudentRole = selectedUser?.roles?.contains("estudiante") == true
    val hasAdminRole = selectedUser?.roles?.contains("administrador") == true

    ScreenSurface {
        SectionTitle(
            title = "Perfiles estudiante/admin",
            subtitle = "Perfiles basicos relacionados con los endpoints actuales del backend."
        )
        SectionCard {
            Text("Selecciona usuario", color = UamTextDark, fontWeight = FontWeight.SemiBold)
            SelectableChipRow(
                items = users.map { it.id to it.name },
                selected = selectedUserId,
                onSelected = { selectedUserId = it }
            )
            selectedUser?.let { user ->
                Spacer(Modifier.height(12.dp))
                UserCard(user = user)
            }
        }

        SectionCard {
            SectionTitle(
                title = "Perfil estudiante",
                subtitle = "Requiere rol estudiante activo."
            )
            EndpointPill("POST /api/usuarios/{id}/perfiles/estudiante")
            Spacer(Modifier.height(10.dp))
            PrototypeTextField("CIF", cif, { cif = it })
            PrototypeTextField("Correo institucional", institutionalEmail, { institutionalEmail = it }, KeyboardType.Email)
            PrototypeTextField("ID carrera principal (temporal)", careerId, { careerId = it }, KeyboardType.Number)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Doble titulacion", color = UamTextDark)
                Switch(checked = dualDegree, onCheckedChange = { dualDegree = it })
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedUser != null && hasStudentRole && selectedUser.studentProfile == null,
                colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                onClick = {
                    message = when {
                        cif.isBlank() -> "El CIF es obligatorio para crear perfil estudiante."
                        else -> {
                            selectedUser?.let {
                                onCreateStudentProfile(
                                    it.id,
                                    StudentProfileUi(
                                        cif = cif.trim(),
                                        institutionalEmail = institutionalEmail.trim().ifBlank { null },
                                        careerId = careerId.toLongOrNull(),
                                        dualDegree = dualDegree
                                    )
                                )
                            }
                            cif = ""
                            institutionalEmail = ""
                            careerId = ""
                            dualDegree = false
                            "Perfil estudiante creado."
                        }
                    }
                }
            ) {
                Text("Crear perfil estudiante")
            }
            if (!hasStudentRole) {
                HelperMessage("Asigna primero el rol estudiante desde la pestaña Roles.")
            }
            selectedUser?.studentProfile?.let {
                ProfileSummary(
                    title = "Perfil estudiante activo",
                    lines = listOf(
                        "CIF: ${it.cif}",
                        "Correo institucional: ${it.institutionalEmail ?: "Sin registrar"}",
                        "Carrera principal: ${it.careerId ?: "Pendiente de catalogo"}"
                    )
                )
            }
        }

        SectionCard {
            SectionTitle(
                title = "Perfil administrador",
                subtitle = "Requiere rol administrador activo."
            )
            EndpointPill("POST /api/usuarios/{id}/perfiles/administrador")
            Spacer(Modifier.height(10.dp))
            PrototypeTextField("Cargo", position, { position = it })
            PrototypeTextField("Nivel de acceso", accessLevel, { accessLevel = it })
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedUser != null && hasAdminRole && selectedUser.adminProfile == null,
                colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                onClick = {
                    message = when {
                        position.isBlank() || accessLevel.isBlank() ->
                            "Cargo y nivel de acceso son obligatorios."

                        else -> {
                            selectedUser?.let {
                                onCreateAdminProfile(
                                    it.id,
                                    AdminProfileUi(
                                        position = position.trim(),
                                        accessLevel = accessLevel.trim()
                                    )
                                )
                            }
                            position = ""
                            accessLevel = "total"
                            "Perfil administrador creado."
                        }
                    }
                }
            ) {
                Text("Crear perfil administrador")
            }
            if (!hasAdminRole) {
                HelperMessage("Asigna primero el rol administrador desde la pestaña Roles.")
            }
            selectedUser?.adminProfile?.let {
                ProfileSummary(
                    title = "Perfil administrador activo",
                    lines = listOf(
                        "Cargo: ${it.position}",
                        "Nivel de acceso: ${it.accessLevel}"
                    )
                )
            }
        }
        message?.let { HelperMessage(it) }
    }
}

@Composable
private fun ScreenSurface(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        content = content
    )
}

@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = UamWhite),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            color = UamTextDark,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = subtitle,
            color = UamGray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: String,
    caption: String,
    modifier: Modifier = Modifier,
    accent: Color = UamTurquoise
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = UamWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value.take(2),
                    color = accent,
                    fontWeight = FontWeight.Black
                )
            }
            Text(
                text = value,
                color = UamTextDark,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = label,
                color = UamTextDark,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = caption,
                color = UamGray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun UserCard(
    user: UserUi,
    actions: (@Composable () -> Unit)? = null
) {
    SectionCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(UamTurquoise.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.initials(),
                    color = UamTurquoise,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    color = UamTextDark,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.email,
                    color = UamGray,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Doc. ${user.document}",
                    color = UamGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            StatusBadge(user.status)
        }
        RoleBadges(user.roles)
        actions?.invoke()
    }
}

@Composable
private fun RoleRow(role: RoleUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(UamTurquoise.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = role.name.take(1).uppercase(),
                color = UamTurquoise,
                fontWeight = FontWeight.Black
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(role.name, color = UamTextDark, fontWeight = FontWeight.SemiBold)
            Text(role.description, color = UamGray, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun RoleBadges(roles: Set<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (roles.isEmpty()) {
            TagPill("sin rol", UamGray)
        } else {
            roles.sorted().forEach { role ->
                TagPill(role, if (role == "administrador") UamInnovationYellow else UamTurquoise)
            }
        }
    }
}

@Composable
private fun TagPill(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.16f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = text, color = if (color == UamInnovationYellow) UamTextDark else color, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun EndpointPill(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(UamLightBackground)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = UamTurquoise,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun StatusBadge(status: String) {
    val color = when (status) {
        "activo" -> UamTurquoise
        "suspendido" -> UamInnovationYellow
        else -> UamGray
    }
    TagPill(text = status, color = color)
}

@Composable
private fun StatusActions(
    current: String,
    onChange: (String) -> Unit
) {
    SelectableTextRow(
        items = listOf("activo", "inactivo", "suspendido"),
        selected = current,
        onSelected = onChange
    )
}

@Composable
private fun SelectableChipRow(
    items: List<Pair<Int, String>>,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SelectablePill(
                text = item.second,
                selected = item.first == selected,
                onClick = { onSelected(item.first) }
            )
        }
    }
}

@Composable
private fun SelectableTextRow(
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SelectablePill(
                text = item,
                selected = item == selected,
                onClick = { onSelected(item) }
            )
        }
    }
}

@Composable
private fun SelectablePill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) UamTurquoise else UamWhite
    val foreground = if (selected) UamWhite else UamTextDark
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (selected) UamTurquoise else UamLightBackground),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = background),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = foreground, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun PrototypeTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    password: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun HelperMessage(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(UamInnovationYellow.copy(alpha = 0.20f))
            .padding(10.dp),
        text = text,
        color = UamTextDark,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun ProfileSummary(title: String, lines: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(UamLightBackground)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(title, color = UamTextDark, fontWeight = FontWeight.SemiBold)
        lines.forEach { line ->
            Text(line, color = UamGray, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun NavGlyph(text: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(if (selected) UamTurquoise else UamLightBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) UamWhite else UamGray,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

private fun SnapshotStateList<UserUi>.updateUser(id: Int, transform: (UserUi) -> UserUi) {
    val index = indexOfFirst { it.id == id }
    if (index >= 0) {
        this[index] = transform(this[index])
    }
}

private fun UserUi.initials(): String {
    return name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "IU" }
}

private enum class PrototypeTab(
    val label: String,
    val glyph: String
) {
    Panel("Panel", "P"),
    Usuarios("Usuarios", "U"),
    Roles("Roles", "R"),
    Perfiles("Perfiles", "F")
}

private data class RoleUi(
    val name: String,
    val description: String
)

private data class UserInput(
    val name: String,
    val document: String,
    val phone: String,
    val email: String,
    val sex: String,
    val shirt: String
)

private data class UserUi(
    val id: Int,
    val name: String,
    val document: String,
    val phone: String,
    val email: String,
    val sex: String,
    val shirt: String,
    val status: String = "activo",
    val roles: Set<String> = emptySet(),
    val studentProfile: StudentProfileUi? = null,
    val adminProfile: AdminProfileUi? = null
)

private data class StudentProfileUi(
    val cif: String,
    val institutionalEmail: String?,
    val careerId: Long?,
    val dualDegree: Boolean
)

private data class AdminProfileUi(
    val position: String,
    val accessLevel: String
)

@Preview(showBackground = true)
@Composable
private fun InnovacionUamPrototypePreview() {
    Sistema_Innovacion_FrontendTheme(darkTheme = false) {
        InnovacionUamPrototypeScreen()
    }
}
