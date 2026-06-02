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
import androidx.compose.material3.HorizontalDivider
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
    var selectedTab by remember { mutableStateOf(MainTab.Panel) }
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
                    position = "Coordinadora de innovación",
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
                MainTab.Panel -> AdminDashboardScreen(
                    users = users,
                    roles = roles,
                    events = events
                )

                MainTab.Usuarios -> UsersScreen(
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
                        events.add(0, "Usuario ${input.name} registrado en el sistema")
                    },
                    onChangeStatus = { id, status ->
                        users.updateUser(id) { it.copy(status = status) }
                        events.add(0, "Estado de usuario actualizado a $status")
                    }
                )

                MainTab.Roles -> RolesScreen(
                    users = users,
                    roles = roles,
                    onCreateRole = { role ->
                        roles.add(role)
                        events.add(0, "Nuevo rol ${role.name} creado")
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

                MainTab.Perfiles -> ProfilesScreen(
                    users = users,
                    onCreateStudentProfile = { userId, profile ->
                        users.updateUser(userId) { it.copy(studentProfile = profile) }
                        val userName = users.firstOrNull { it.id == userId }?.name.orEmpty()
                        events.add(0, "Perfil de estudiante completado para $userName")
                    },
                    onCreateAdminProfile = { userId, profile ->
                        users.updateUser(userId) { it.copy(adminProfile = profile) }
                        val userName = users.firstOrNull { it.id == userId }?.name.orEmpty()
                        events.add(0, "Perfil administrativo completado para $userName")
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
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(UamWhite),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "UAM",
                        color = UamTurquoise,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black
                    )
                }
                Column {
                    Text(
                        text = "Innovación UAM",
                        color = UamWhite,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Dirección de Innovación y Emprendimiento",
                        color = UamWhite.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                text = "Gestión integral de participantes, roles y perfiles institucionales para programas de innovación.",
                color = UamWhite.copy(alpha = 0.95f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
            )
        }
    }
}

@Composable
private fun InnovationBottomBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    NavigationBar(
        containerColor = UamWhite,
        tonalElevation = 12.dp
    ) {
        MainTab.entries.forEach { tab ->
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
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
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
            title = "Panel de Control",
            subtitle = "Resumen ejecutivo de la gestión de usuarios y roles."
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(
                label = "Participantes",
                value = users.size.toString(),
                caption = "Usuarios únicos",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                label = "Asignaciones",
                value = users.sumOf { it.roles.size }.toString(),
                caption = "Roles activos",
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(
                label = "Perfiles",
                value = users.count { it.studentProfile != null || it.adminProfile != null }.toString(),
                caption = "Completados",
                modifier = Modifier.weight(1f),
                accent = UamInnovationYellow
            )
            MetricCard(
                label = "Catálogo",
                value = roles.size.toString(),
                caption = "Roles definidos",
                modifier = Modifier.weight(1f),
                accent = UamGray
            )
        }
        
        SectionCard {
            SectionTitle(
                title = "Actividad Reciente",
                subtitle = "Últimas acciones realizadas en la plataforma."
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                events.take(5).forEachIndexed { index, event ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (index == 0) UamTurquoise else UamGray.copy(alpha = 0.5f))
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = event,
                            color = UamTextDark,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (index < 4 && index < events.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 20.dp),
                            thickness = 0.5.dp,
                            color = UamGray.copy(alpha = 0.1f)
                        )
                    }
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
            title = "Registro de Participantes",
            subtitle = "Ingrese los datos básicos para dar de alta a un nuevo miembro."
        )
        SectionCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                BaseTextField("Nombre completo", name, { name = it })
                BaseTextField("Documento de identidad", document, { document = it })
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BaseTextField("Teléfono", phone, { phone = it }, keyboardType = KeyboardType.Phone, modifier = Modifier.weight(1f))
                    BaseTextField("Correo electrónico", email, { email = it }, keyboardType = KeyboardType.Email, modifier = Modifier.weight(1.5f))
                }
                BaseTextField(
                    label = "Contraseña",
                    value = password,
                    onValueChange = { password = it },
                    keyboardType = KeyboardType.Password,
                    password = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BaseTextField("Sexo", sex, { sex = it }, modifier = Modifier.weight(1f))
                    BaseTextField("Talla de camisa", shirt, { shirt = it }, modifier = Modifier.weight(1f))
                }
                
                Button(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                    onClick = {
                        message = when {
                            name.isBlank() || document.isBlank() || email.isBlank() || password.length < 6 ->
                                "Por favor, complete todos los campos obligatorios."

                            users.any { it.email.equals(email.trim(), ignoreCase = true) } ->
                                "El correo electrónico ya se encuentra registrado."

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
                                name = ""; document = ""; phone = ""; email = ""; password = ""; sex = ""; shirt = ""
                                "Usuario registrado correctamente."
                            }
                        }
                    }
                ) {
                    Text("Registrar Usuario", fontWeight = FontWeight.Bold)
                }
                
                message?.let { StatusMessage(text = it) }
            }
        }

        SectionTitle(
            title = "Listado de Usuarios",
            subtitle = "Gestión de estado y visualización de perfiles registrados."
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
            title = "Configuración de Roles",
            subtitle = "Administre el catálogo de roles disponibles en el sistema."
        )
        SectionCard {
            roles.forEachIndexed { index, role ->
                RoleRow(role = role)
                if (index < roles.size - 1) {
                    HorizontalDivider(color = UamLightBackground, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }

        SectionCard {
            SectionTitle(title = "Crear Nuevo Rol", subtitle = "Defina un nuevo perfil de acceso.")
            BaseTextField("Nombre del rol", roleName, { roleName = it })
            BaseTextField("Descripción", roleDescription, { roleDescription = it })
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                onClick = {
                    val normalized = roleName.trim().lowercase()
                    if (normalized.isNotBlank()) {
                        onCreateRole(RoleUi(normalized, roleDescription.trim()))
                        roleName = ""; roleDescription = ""
                        message = "Rol creado exitosamente."
                    }
                }
            ) {
                Text("Añadir al Catálogo")
            }
        }

        SectionCard {
            SectionTitle(title = "Asignación de Roles", subtitle = "Vincule usuarios con roles específicos.")
            
            Text("Seleccionar Usuario", style = MaterialTheme.typography.labelLarge, color = UamGray)
            SelectableChipRow(
                items = users.map { it.id to it.name },
                selected = selectedUserId,
                onSelected = { selectedUserId = it }
            )
            
            Text("Seleccionar Rol", style = MaterialTheme.typography.labelLarge, color = UamGray, modifier = Modifier.padding(top = 8.dp))
            SelectableTextRow(
                items = roles.map { it.name },
                selected = selectedRole,
                onSelected = { selectedRole = it }
            )
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                    enabled = selectedUser != null && selectedRole.isNotBlank(),
                    onClick = { selectedUser?.let { onAssignRole(it.id, selectedRole) } }
                ) {
                    Text("Asignar")
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, UamGray.copy(alpha = 0.5f)),
                    enabled = selectedUser?.roles?.contains(selectedRole) == true,
                    onClick = { selectedUser?.let { onDeactivateRole(it.id, selectedRole) } }
                ) {
                    Text("Remover", color = UamTextDark)
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
            title = "Perfiles Institucionales",
            subtitle = "Complete la información específica según el rol del usuario."
        )
        SectionCard {
            Text("Usuario actual", style = MaterialTheme.typography.labelLarge, color = UamGray)
            SelectableChipRow(
                items = users.map { it.id to it.name },
                selected = selectedUserId,
                onSelected = { selectedUserId = it }
            )
            selectedUser?.let { user ->
                Spacer(Modifier.height(8.dp))
                UserCardCompact(user = user)
            }
        }

        if (hasStudentRole) {
            SectionCard {
                SectionTitle(title = "Perfil de Estudiante", subtitle = "Datos académicos obligatorios.")
                BaseTextField("CIF", cif, { cif = it })
                BaseTextField("Correo Institucional", institutionalEmail, { institutionalEmail = it }, KeyboardType.Email)
                BaseTextField("Código de Carrera", careerId, { careerId = it }, KeyboardType.Number)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Doble Titulación", color = UamTextDark)
                    Switch(checked = dualDegree, onCheckedChange = { dualDegree = it })
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedUser != null && selectedUser.studentProfile == null,
                    colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                    onClick = {
                        if (cif.isNotBlank()) {
                            selectedUser?.let {
                                onCreateStudentProfile(it.id, StudentProfileUi(cif.trim(), institutionalEmail.trim(), careerId.toLongOrNull(), dualDegree))
                            }
                            cif = ""; institutionalEmail = ""; careerId = ""; dualDegree = false
                            message = "Perfil académico guardado."
                        }
                    }
                ) {
                    Text("Guardar Perfil Académico")
                }
                selectedUser?.studentProfile?.let {
                    ProfileSummary(
                        title = "Información Académica Activa",
                        lines = listOf("CIF: ${it.cif}", "Email: ${it.institutionalEmail ?: "N/A"}", "Carrera ID: ${it.careerId ?: "Pendiente"}")
                    )
                }
            }
        }

        if (hasAdminRole) {
            SectionCard {
                SectionTitle(title = "Perfil Administrativo", subtitle = "Información de cargo y accesos.")
                BaseTextField("Cargo / Posición", position, { position = it })
                BaseTextField("Nivel de Acceso", accessLevel, { accessLevel = it })
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedUser != null && selectedUser.adminProfile == null,
                    colors = ButtonDefaults.buttonColors(containerColor = UamTurquoise),
                    onClick = {
                        if (position.isNotBlank()) {
                            selectedUser?.let {
                                onCreateAdminProfile(it.id, AdminProfileUi(position.trim(), accessLevel.trim()))
                            }
                            position = ""; accessLevel = "total"
                            message = "Perfil administrativo guardado."
                        }
                    }
                ) {
                    Text("Guardar Perfil Administrativo")
                }
                selectedUser?.adminProfile?.let {
                    ProfileSummary(
                        title = "Información Administrativa Activa",
                        lines = listOf("Cargo: ${it.position}", "Acceso: ${it.accessLevel}")
                    )
                }
            }
        }
        
        if (!hasStudentRole && !hasAdminRole) {
            StatusMessage("Asigne un rol (Estudiante o Administrador) para habilitar perfiles.")
        }
        
        message?.let { StatusMessage(it) }
    }
}

@Composable
private fun ScreenSurface(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = UamWhite),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = title,
            color = UamTextDark,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            color = UamGray,
            style = MaterialTheme.typography.bodySmall
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = UamWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, UamLightBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value.take(2),
                    color = accent,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = value,
                color = UamTextDark,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )
            Text(
                text = label,
                color = UamTextDark,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
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
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(UamTurquoise.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.initials(),
                    color = UamTurquoise,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    color = UamTextDark,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
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
            }
            StatusBadge(user.status)
        }
        RoleBadges(user.roles)
        actions?.invoke()
    }
}

@Composable
private fun UserCardCompact(user: UserUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(UamLightBackground)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(UamTurquoise),
            contentAlignment = Alignment.Center
        ) {
            Text(user.initials(), color = UamWhite, style = MaterialTheme.typography.labelSmall)
        }
        Text(user.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RoleRow(role: RoleUi) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(UamTurquoise.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = role.name.take(1).uppercase(),
                color = UamTurquoise,
                fontWeight = FontWeight.Black
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(role.name.replaceFirstChar { it.uppercase() }, color = UamTextDark, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text(role.description, color = UamGray, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun RoleBadges(roles: Set<String>) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (roles.isEmpty()) {
            TagPill("Sin roles asignados", UamGray)
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
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text, 
            color = if (color == UamInnovationYellow) UamTextDark else color, 
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
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
    TagPill(text = status.uppercase(), color = color)
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
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
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
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
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
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.5.dp, if (selected) UamTurquoise else UamLightBackground),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = background),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = foreground, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun BaseTextField(
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
        textStyle = MaterialTheme.typography.bodyMedium,
        visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun StatusMessage(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(UamInnovationYellow.copy(alpha = 0.15f))
            .padding(12.dp),
        text = text,
        color = UamTextDark,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ProfileSummary(title: String, lines: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(UamLightBackground)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(title, color = UamTextDark, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        lines.forEach { line ->
            Text(line, color = UamGray, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun NavGlyph(text: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(if (selected) UamTurquoise else UamLightBackground.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) UamWhite else UamGray,
            fontWeight = FontWeight.Black,
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

private enum class MainTab(
    val label: String,
    val glyph: String
) {
    Panel("Dashboard", "D"),
    Usuarios("Usuarios", "U"),
    Roles("Roles", "R"),
    Perfiles("Perfiles", "P")
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
