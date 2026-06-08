package ni.edu.uam.innovacion.features.dashboard.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen() {
    val uamTeal = Color(0xFF00ADB5)
    val uamNavy = Color(0xFF0F172A)
    val uamGold = Color(0xFFFFC107)
    val contentBg = Color(0xFFFBFDFF)

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(contentBg).padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        // --- KEY METRICS (KPIs) ---
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ModernKpiCard(modifier = Modifier.weight(1f), title = "Participantes", value = "1,247", icon = Icons.Outlined.Group, color = uamTeal)
                ModernKpiCard(modifier = Modifier.weight(1f), title = "Inscripciones", value = "3,892", icon = Icons.AutoMirrored.Outlined.Assignment, color = Color(0xFF3B82F6))
                ModernKpiCard(modifier = Modifier.weight(1f), title = "Actividades", value = "64", icon = Icons.Outlined.Bolt, color = Color(0xFF10B981))
                ModernKpiCard(modifier = Modifier.weight(1f), title = "Puntos", value = "48,230", icon = Icons.Outlined.Stars, color = uamGold)
            }
        }

        // --- SECONDARY METRICS ---
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ModernKpiCard(modifier = Modifier.weight(1f), title = "Facultades", value = "8", icon = Icons.Outlined.AccountBalance, color = Color(0xFF6366F1))
                ModernKpiCard(modifier = Modifier.weight(1f), title = "Carreras", value = "24", icon = Icons.Outlined.School, color = Color(0xFFF43F5E))
                Box(modifier = Modifier.weight(2f)) // Space for balance
            }
        }

        // --- CHARTS AREA ---
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                // Analytics Card
                DashboardCardPremium(
                    modifier = Modifier.weight(2.4f),
                    title = "Participación y Registro",
                    subtitle = "Comparativa mensual de estudiantes e inscritos"
                ) {
                    MockTrendChartPremium(uamTeal, uamGold)
                }

                // Summary Card
                DashboardCardPremium(
                    modifier = Modifier.weight(1f),
                    title = "Tipos de Actividad",
                    subtitle = "Desglose por categoría"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        ProgressIndicatorPremium("Talleres", 0.75f, uamTeal)
                        ProgressIndicatorPremium("Conferencias", 0.55f, Color(0xFF3B82F6))
                        ProgressIndicatorPremium("Mentorías", 0.35f, Color(0xFF6366F1))
                        ProgressIndicatorPremium("Otros", 0.20f, Color(0xFFCBD5E1))
                    }
                }
            }
        }

        // --- INFORMATION PANELS ---
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                // System Activity
                DashboardCardPremium(
                    modifier = Modifier.weight(1.4f),
                    title = "Actividad Reciente",
                    subtitle = "Eventos procesados en las últimas horas"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ActivityRowPremium("Nueva Inscripción", "Facultad de Ingeniería", "Hace 5m")
                        ActivityRowPremium("Puntos Asignados", "Proyecto Eco-System", "Hace 22m")
                        ActivityRowPremium("Módulo Actualizado", "Catálogo de Carreras", "Hace 1h")
                        ActivityRowPremium("Sistema Sincronizado", "Base de datos UAM", "Hace 3h")
                    }
                }

                // Quick Launch
                DashboardCardPremium(
                    modifier = Modifier.weight(1f),
                    title = "Accesos Directos",
                    subtitle = "Acciones administrativas rápidas"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickLaunchButton(Icons.Outlined.Download, "Exportar Reporte Mensual")
                        QuickLaunchButton(Icons.Outlined.AdminPanelSettings, "Gestión de Permisos")
                        QuickLaunchButton(Icons.Outlined.Refresh, "Forzar Sincronización")
                    }
                }
            }
        }
    }
}

@Composable
fun ModernKpiCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Surface(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black.copy(alpha = 0.04f)),
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(color.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = color)
            }
            Column {
                Text(text = title.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A), letterSpacing = (-0.5).sp)
            }
        }
    }
}

@Composable
fun DashboardCardPremium(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(28.dp), ambientColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(28.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(32.dp)) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
            Text(subtitle, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF94A3B8))
            Spacer(modifier = Modifier.height(32.dp))
            content()
        }
    }
}

@Composable
fun MockTrendChartPremium(color1: Color, color2: Color) {
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        val width = size.width
        val height = size.height
        
        // Grid lines
        for (i in 0..4) {
            val y = height * (i / 4f)
            drawLine(color = Color(0xFFF1F5F9), start = androidx.compose.ui.geometry.Offset(0f, y), end = androidx.compose.ui.geometry.Offset(width, y), strokeWidth = 1.dp.toPx())
        }

        val p1 = listOf(0.2f, 0.45f, 0.4f, 0.7f, 0.6f, 0.85f, 0.8f)
        val p2 = listOf(0.1f, 0.3f, 0.25f, 0.5f, 0.45f, 0.65f, 0.6f)

        fun drawSmoothLine(points: List<Float>, color: Color, gradient: Boolean) {
            val path = androidx.compose.ui.graphics.Path()
            val step = width / (points.size - 1)
            points.forEachIndexed { i, v ->
                val x = i * step
                val y = height - (v * height)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            if (gradient) {
                val fill = androidx.compose.ui.graphics.Path().apply { addPath(path); lineTo(width, height); lineTo(0f, height); close() }
                drawPath(fill, brush = Brush.verticalGradient(listOf(color.copy(alpha = 0.12f), Color.Transparent)))
            }
            drawPath(path, color, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
        }

        drawSmoothLine(p1, color1, true)
        drawSmoothLine(p2, color2, false)
    }
}

@Composable
fun ProgressIndicatorPremium(label: String, progress: Float, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
            Text("${(progress * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
        }
        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = color,
            trackColor = Color(0xFFF1F5F9)
        )
    }
}

@Composable
fun ActivityRowPremium(title: String, desc: String, time: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF00ADB5)))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Text(desc, fontSize = 12.sp, color = Color(0xFF64748B))
        }
        Text(time, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF94A3B8))
    }
}

@Composable
fun QuickLaunchButton(icon: ImageVector, label: String) {
    Surface(
        onClick = {},
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF64748B))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
        }
    }
}
