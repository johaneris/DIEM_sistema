package ni.edu.uam.innovacion.features.admin.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ni.edu.uam.innovacion.R
import ni.edu.uam.innovacion.data.remote.auth.AuthenticatedUserResponse
import ni.edu.uam.innovacion.di.appContainer
import ni.edu.uam.innovacion.features.admin.navigation.AdminDestination
import ni.edu.uam.innovacion.features.admin.presentation.activities.ActivitiesScreen
import ni.edu.uam.innovacion.features.admin.presentation.activities.ActivitiesViewModel
import ni.edu.uam.innovacion.features.admin.presentation.catalogs.CatalogKind
import ni.edu.uam.innovacion.features.admin.presentation.catalogs.CatalogsScreen
import ni.edu.uam.innovacion.features.admin.presentation.catalogs.CatalogsViewModel
import ni.edu.uam.innovacion.features.admin.presentation.components.EmptyState
import ni.edu.uam.innovacion.features.admin.presentation.dashboard.DashboardScreen
import ni.edu.uam.innovacion.features.admin.presentation.users.ProfileKind
import ni.edu.uam.innovacion.features.admin.presentation.users.ProfilesScreen
import ni.edu.uam.innovacion.features.admin.presentation.users.UsersScreen
import ni.edu.uam.innovacion.features.admin.presentation.users.UsersViewModel
import ni.edu.uam.innovacion.ui.theme.UamTurquoise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRoot(
    currentUser: AuthenticatedUserResponse?,
    isLoggingOut: Boolean,
    logoutError: String?,
    onLogout: () -> Unit,
    onDismissLogoutError: () -> Unit,
    onSessionExpired: () -> Unit
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLogoutConfirm by rememberSaveable { mutableStateOf(false) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentDestination = AdminDestination.all.firstOrNull { it.route == currentRoute }
        ?: AdminDestination.Dashboard

    LaunchedEffect(logoutError) {
        if (logoutError != null) showLogoutConfirm = false
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val permanentDrawer = maxWidth >= 840.dp
        val content: @Composable () -> Unit = {
            AdminScaffold(
                navController = navController,
                currentDestination = currentDestination,
                showMenu = !permanentDrawer,
                onMenuClick = { scope.launch { drawerState.open() } },
                onSessionExpired = onSessionExpired
            )
        }

        if (permanentDrawer) {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet(modifier = Modifier.width(292.dp)) {
                        AdminDrawerContent(
                            currentUser = currentUser,
                            currentRoute = currentRoute,
                            onNavigate = { destination -> navigate(navController, destination) },
                            onLogout = { showLogoutConfirm = true }
                        )
                    }
                },
                content = content
            )
        } else {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(modifier = Modifier.width(292.dp)) {
                        AdminDrawerContent(
                            currentUser = currentUser,
                            currentRoute = currentRoute,
                            onNavigate = { destination ->
                                navigate(navController, destination)
                                scope.launch { drawerState.close() }
                            },
                            onLogout = {
                                scope.launch { drawerState.close() }
                                showLogoutConfirm = true
                            }
                        )
                    }
                },
                content = content
            )
        }
    }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { if (!isLoggingOut) showLogoutConfirm = false },
            title = { Text("Cerrar sesion") },
            text = {
                Text("Se revocara el token activo en el servidor. Desea continuar?")
            },
            confirmButton = {
                Button(onClick = onLogout, enabled = !isLoggingOut) {
                    if (isLoggingOut) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Cerrar sesion")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutConfirm = false },
                    enabled = !isLoggingOut
                ) { Text("Cancelar") }
            },
            shape = RoundedCornerShape(8.dp)
        )
    }

    logoutError?.let { message ->
        AlertDialog(
            onDismissRequest = onDismissLogoutError,
            title = { Text("No se pudo cerrar sesion") },
            text = { Text("$message\n\nLa sesion se conserva para poder reintentar la revocacion.") },
            confirmButton = {
                Button(onClick = onLogout, enabled = !isLoggingOut) { Text("Reintentar") }
            },
            dismissButton = {
                TextButton(onClick = onDismissLogoutError) { Text("Volver") }
            },
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminScaffold(
    navController: NavHostController,
    currentDestination: AdminDestination,
    showMenu: Boolean,
    onMenuClick: () -> Unit,
    onSessionExpired: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        currentDestination.title,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (showMenu) {
                        IconButton(onClick = onMenuClick) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir navegacion")
                        }
                    }
                },
                actions = {
                    Text(
                        "ADMIN",
                        modifier = Modifier
                            .background(UamTurquoise.copy(alpha = 0.13f), RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        color = UamTurquoise,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        AdminNavHost(
            navController = navController,
            onSessionExpired = onSessionExpired,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun AdminNavHost(
    navController: NavHostController,
    onSessionExpired: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container = LocalContext.current.appContainer
    NavHost(
        navController = navController,
        startDestination = AdminDestination.Dashboard.route,
        modifier = modifier
    ) {
        composable(AdminDestination.Dashboard.route) {
            val vm: AdminDataViewModel = viewModel(
                factory = AdminDataViewModel.factory(
                    container.usuariosRepository,
                    container.catalogosRepository,
                    container.actividadesRepository
                )
            )
            DashboardScreen(vm, onSessionExpired)
        }
        composable(AdminDestination.Usuarios.route) {
            UsersScreen(usersViewModel(container), onSessionExpired)
        }
        composable(AdminDestination.PerfilAdministrador.route) {
            ProfilesScreen(ProfileKind.Administrador, usersViewModel(container), onSessionExpired)
        }
        composable(AdminDestination.PerfilMentor.route) {
            ProfilesScreen(ProfileKind.Mentor, usersViewModel(container), onSessionExpired)
        }
        composable(AdminDestination.PerfilDocente.route) {
            ProfilesScreen(ProfileKind.Docente, usersViewModel(container), onSessionExpired)
        }
        composable(AdminDestination.PerfilEstudiante.route) {
            ProfilesScreen(ProfileKind.Estudiante, usersViewModel(container), onSessionExpired)
        }
        composable(AdminDestination.PerfilExterno.route) {
            ProfilesScreen(ProfileKind.Externo, usersViewModel(container), onSessionExpired)
        }
        composable(AdminDestination.Actividades.route) {
            val vm: ActivitiesViewModel = viewModel(
                factory = ActivitiesViewModel.factory(
                    container.actividadesRepository,
                    container.catalogosRepository,
                    container.usuariosRepository
                )
            )
            ActivitiesScreen(vm, onSessionExpired)
        }
        composable(AdminDestination.Participacion.route) {
            EmptyFeaturePage("Participacion", "Aun no hay un servicio de participacion disponible.")
        }
        composable(AdminDestination.Reportes.route) {
            EmptyFeaturePage("Reportes", "Aun no hay un servicio de reportes disponible.")
        }
        composable(AdminDestination.Constancia.route) {
            EmptyFeaturePage("Constancia", "Aun no hay un servicio de constancias disponible.")
        }
        catalogDestination(AdminDestination.Ambitos, CatalogKind.Ambitos, container, onSessionExpired)
        catalogDestination(AdminDestination.Facultades, CatalogKind.Facultades, container, onSessionExpired)
        catalogDestination(AdminDestination.Carreras, CatalogKind.Carreras, container, onSessionExpired)
        catalogDestination(AdminDestination.CategoriasDiem, CatalogKind.CategoriasDiem, container, onSessionExpired)
        catalogDestination(AdminDestination.FuentesProyecto, CatalogKind.FuentesProyecto, container, onSessionExpired)
        catalogDestination(AdminDestination.Roles, CatalogKind.Roles, container, onSessionExpired)
        catalogDestination(
            AdminDestination.RolesParticipacion,
            CatalogKind.RolesParticipacion,
            container,
            onSessionExpired
        )
    }
}

@Composable
private fun AdminDrawerContent(
    currentUser: AuthenticatedUserResponse?,
    currentRoute: String?,
    onNavigate: (AdminDestination) -> Unit,
    onLogout: () -> Unit
) {
    var profilesExpanded by rememberSaveable { mutableStateOf(true) }
    var catalogsExpanded by rememberSaveable { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        DrawerHeader(currentUser)
        HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            DrawerItem(AdminDestination.Dashboard, currentRoute, onNavigate)
            DrawerItem(AdminDestination.Usuarios, currentRoute, onNavigate)
            DrawerGroupHeader(
                label = "Perfiles",
                icon = Icons.Filled.People,
                expanded = profilesExpanded,
                onClick = { profilesExpanded = !profilesExpanded }
            )
            if (profilesExpanded) {
                AdminDestination.profileItems.forEach { destination ->
                    DrawerItem(destination, currentRoute, onNavigate, nested = true)
                }
            }
            DrawerItem(AdminDestination.Actividades, currentRoute, onNavigate)
            DrawerItem(AdminDestination.Participacion, currentRoute, onNavigate)
            DrawerItem(AdminDestination.Reportes, currentRoute, onNavigate)
            DrawerItem(AdminDestination.Constancia, currentRoute, onNavigate)
            DrawerGroupHeader(
                label = "Catalogos",
                icon = Icons.Filled.Category,
                expanded = catalogsExpanded,
                onClick = { catalogsExpanded = !catalogsExpanded }
            )
            if (catalogsExpanded) {
                AdminDestination.catalogItems.forEach { destination ->
                    DrawerItem(destination, currentRoute, onNavigate, nested = true)
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp))
        NavigationDrawerItem(
            label = { Text("Cerrar sesion", fontWeight = FontWeight.Medium) },
            selected = false,
            onClick = onLogout,
            icon = { Icon(Icons.Filled.Logout, contentDescription = null) },
            modifier = Modifier.padding(10.dp),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun DrawerHeader(currentUser: AuthenticatedUserResponse?) {
    Column(
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo_diem),
            contentDescription = "DIEM UAM",
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            contentScale = ContentScale.Fit
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(UamTurquoise),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    currentUser?.nombreCompleto?.firstOrNull()?.uppercase() ?: "A",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    currentUser?.nombreCompleto ?: "Administrador",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    currentUser?.correo.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DrawerItem(
    destination: AdminDestination,
    currentRoute: String?,
    onNavigate: (AdminDestination) -> Unit,
    nested: Boolean = false
) {
    NavigationDrawerItem(
        label = {
            Text(
                destination.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        selected = currentRoute == destination.route,
        onClick = { onNavigate(destination) },
        icon = {
            if (nested) Icon(Icons.Filled.ChevronLeft, contentDescription = null)
            else Icon(destination.icon, contentDescription = null)
        },
        modifier = if (nested) Modifier.padding(start = 22.dp) else Modifier,
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun DrawerGroupHeader(
    label: String,
    icon: ImageVector,
    expanded: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) },
        badge = {
            Icon(
                if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Contraer" else "Expandir"
            )
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun EmptyFeaturePage(title: String, message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            "Modulo visible sin datos simulados",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        EmptyState(title = "Servicio no disponible", message = message, modifier = Modifier.weight(1f))
    }
}

private fun navigate(navController: NavHostController, destination: AdminDestination) {
    navController.navigate(destination.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(AdminDestination.Dashboard.route) { saveState = true }
    }
}

@Composable
private fun usersViewModel(container: ni.edu.uam.innovacion.di.AppContainer): UsersViewModel =
    viewModel(factory = UsersViewModel.factory(container.usuariosRepository, container.catalogosRepository))

private fun androidx.navigation.NavGraphBuilder.catalogDestination(
    destination: AdminDestination,
    kind: CatalogKind,
    container: ni.edu.uam.innovacion.di.AppContainer,
    onSessionExpired: () -> Unit
) {
    composable(destination.route) {
        val vm: CatalogsViewModel = viewModel(factory = CatalogsViewModel.factory(container.catalogosRepository))
        CatalogsScreen(kind, vm, onSessionExpired)
    }
}
