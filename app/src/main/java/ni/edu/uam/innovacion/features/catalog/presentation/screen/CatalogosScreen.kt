package ni.edu.uam.innovacion.features.catalog.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CatalogosScreen(
    onEditFacultades: () -> Unit = {},
    onEditCarreras: () -> Unit = {},
    onEditRoles: () -> Unit = {}
) {
    val uamNavy = Color(0xFF003366)
    val backgroundGray = Color(0xFFF1F5F9)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGray)
            .padding(horizontal = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Column {
            Text(
                text = "Administración de Catálogos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = uamNavy,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Módulos de configuración maestro para el sistema",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            item {
                CatalogOverviewCard(
                    title = "Facultades",
                    description = "Gestión de facultades, códigos y estados institucionales.",
                    icon = Icons.Outlined.Category,
                    accentColor = Color(0xFF12A6AA),
                    onClick = onEditFacultades
                )
            }
            item {
                CatalogOverviewCard(
                    title = "Carreras",
                    description = "Administración de programas académicos vinculados a facultades.",
                    icon = Icons.Outlined.Category,
                    accentColor = Color(0xFF3B82F6),
                    onClick = onEditCarreras
                )
            }
            item {
                CatalogOverviewCard(
                    title = "Roles de Usuario",
                    description = "Configuración de roles y permisos del sistema.",
                    icon = Icons.Outlined.Category,
                    accentColor = Color(0xFF8B5CF6),
                    onClick = onEditRoles
                )
            }
            
            item {
                Text(
                    text = "Otros Catálogos",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            
            val extraCatalogs = listOf(
                "Ámbitos de actividad", "Categorías DIEM", "Fuentes de proyecto", 
                "Roles de participación", "Roles de proyecto"
            )
            
            items(extraCatalogs.size) { index ->
                CatalogOverviewCard(
                    title = extraCatalogs[index],
                    description = "Configuración del catálogo ${extraCatalogs[index].lowercase()}.",
                    icon = Icons.Outlined.Category,
                    accentColor = Color.LightGray,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun CatalogOverviewCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp), ambientColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}
