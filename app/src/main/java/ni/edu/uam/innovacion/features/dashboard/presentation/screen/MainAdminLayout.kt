package ni.edu.uam.innovacion.features.dashboard.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.innovacion.features.catalog.presentation.screen.CatalogosScreen
import ni.edu.uam.innovacion.features.catalog.presentation.screen.CarreraScreen
import ni.edu.uam.innovacion.features.catalog.presentation.screen.FacultadScreen
import ni.edu.uam.innovacion.features.catalog.presentation.screen.RolScreen

enum class AdminSection {
    DASHBOARD, CATALOGOS, ROLES_DETAIL, FACULTADES_DETAIL, CARRERAS_DETAIL,
    USUARIOS, ACTIVIDADES, INSCRIPCIONES, PARTICIPACION, PUNTOS, MENTORIAS, REPORTES, CERTIFICADOS
}

data class SidebarItemData(
    val section: AdminSection,
    val title: String,
    val icon: ImageVector
)

val sidebarItems = listOf(
    SidebarItemData(AdminSection.DASHBOARD, "Dashboard", Icons.Outlined.SpaceDashboard),
    SidebarItemData(AdminSection.CATALOGOS, "Catálogos", Icons.Outlined.TableChart),
    SidebarItemData(AdminSection.USUARIOS, "Usuarios", Icons.Outlined.Group),
    SidebarItemData(AdminSection.ACTIVIDADES, "Actividades", Icons.Outlined.Bolt),
    SidebarItemData(AdminSection.INSCRIPCIONES, "Inscripciones", Icons.AutoMirrored.Outlined.Assignment),
    SidebarItemData(AdminSection.PARTICIPACION, "Participación", Icons.Outlined.TaskAlt),
    SidebarItemData(AdminSection.PUNTOS, "Puntos", Icons.Outlined.Stars),
    SidebarItemData(AdminSection.MENTORIAS, "Mentorías", Icons.Outlined.TipsAndUpdates),
    SidebarItemData(AdminSection.REPORTES, "Reportes", Icons.Outlined.InsertChartOutlined),
    SidebarItemData(AdminSection.CERTIFICADOS, "Certificados", Icons.Outlined.Verified)
)

@Composable
fun MainAdminLayout() {
    var currentSection by remember { mutableStateOf(AdminSection.DASHBOARD) }
    var isSidebarCollapsed by remember { mutableStateOf(true) }
    
    val sidebarWidth by animateDpAsState(
        targetValue = if (isSidebarCollapsed) 88.dp else 280.dp,
        animationSpec = tween(durationMillis = 350),
        label = "sidebarWidth"
    )

    // REFINED COLOR PALETTE (Clean, Modern, Trustworthy)
    val uamNavy = Color(0xFF050B18) // Deepest Navy
    val uamTeal = Color(0xFF00ADB5) // Vibrant Modern Teal
    val sidebarBg = uamNavy
    val contentBg = Color(0xFFFBFDFF) // Soft Blue-White Background

    Row(modifier = Modifier.fillMaxSize().background(contentBg)) {
        // --- CLEAN SIDEBAR ---
        Surface(
            modifier = Modifier.width(sidebarWidth).fillMaxHeight(),
            color = sidebarBg,
            shadowElevation = 0.dp
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                // Branding Header
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSidebarCollapsed) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = uamTeal.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("U", color = uamTeal, fontWeight = FontWeight.Black, fontSize = 24.sp)
                            }
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Surface(modifier = Modifier.size(40.dp), shape = RoundedCornerShape(12.dp), color = uamTeal) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("U", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("UAM", color = Color.White, fontWeight = FontWeight.Black, fontSize = 22.sp, letterSpacing = 1.sp)
                                Text("INNOVACIÓN", color = uamTeal, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.5.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Modern Navigation Menu
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sidebarItems.forEach { item ->
                        val isSelected = when(currentSection) {
                            AdminSection.ROLES_DETAIL, AdminSection.FACULTADES_DETAIL, AdminSection.CARRERAS_DETAIL -> item.section == AdminSection.CATALOGOS
                            else -> currentSection == item.section
                        }
                        SidebarNavButton(item = item, isSelected = isSelected, isCollapsed = isSidebarCollapsed, uamTeal = uamTeal) {
                            currentSection = item.section
                        }
                    }
                }

                // Clean Bottom Profile
                Surface(
                    modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
                    color = Color.White.copy(alpha = 0.03f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {
                        if (isSidebarCollapsed) {
                            Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = uamTeal.copy(alpha = 0.2f)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("AD", color = uamTeal, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = uamTeal) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("AD", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Admin System", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("Online", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- CONTENT AREA ---
        Column(modifier = Modifier.weight(1f)) {
            // Elegant TopBar
            Surface(modifier = Modifier.fillMaxWidth().height(80.dp), color = Color.Transparent) {
                Row(modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            onClick = { isSidebarCollapsed = !isSidebarCollapsed },
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 1.dp
                        ) {
                            Box(modifier = Modifier.padding(10.dp)) {
                                Icon(
                                    imageVector = if (isSidebarCollapsed) Icons.AutoMirrored.Outlined.MenuOpen else Icons.Outlined.Menu, 
                                    contentDescription = null, 
                                    tint = Color(0xFF1E293B),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(32.dp))
                        Text(
                            text = when(currentSection) {
                                AdminSection.DASHBOARD -> "Dashboard Overview"
                                AdminSection.CATALOGOS -> "System Catalogs"
                                else -> "Administration"
                            },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(20.dp)) {
                            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFF10B981), CircleShape))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Live Sync", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            }
                        }
                    }
                }
            }

            // Screen Container
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentSection) {
                    AdminSection.DASHBOARD -> DashboardScreen()
                    AdminSection.CATALOGOS -> CatalogosScreen(
                        onEditFacultades = { currentSection = AdminSection.FACULTADES_DETAIL },
                        onEditCarreras = { currentSection = AdminSection.CARRERAS_DETAIL },
                        onEditRoles = { currentSection = AdminSection.ROLES_DETAIL }
                    )
                    AdminSection.FACULTADES_DETAIL -> FacultadScreen(onBack = { currentSection = AdminSection.CATALOGOS })
                    AdminSection.CARRERAS_DETAIL -> CarreraScreen(onBack = { currentSection = AdminSection.CATALOGOS })
                    AdminSection.ROLES_DETAIL -> RolScreen()
                    else -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Módulo en desarrollo", color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SidebarNavButton(
    item: SidebarItemData,
    isSelected: Boolean,
    isCollapsed: Boolean,
    uamTeal: Color,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(if (isSelected) uamTeal else Color.Transparent, label = "bg")
    val contentColor by animateColorAsState(if (isSelected) Color.White else Color(0xFF94A3B8), label = "content")

    Surface(
        modifier = Modifier.fillMaxWidth().height(52.dp).clickable { onClick() },
        color = bgColor,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (isCollapsed) 0.dp else 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isCollapsed) Arrangement.Center else Arrangement.Start
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
            if (!isCollapsed) {
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = item.title,
                    color = contentColor,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}
