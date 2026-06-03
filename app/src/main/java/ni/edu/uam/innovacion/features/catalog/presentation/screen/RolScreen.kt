package ni.edu.uam.innovacion.features.catalog.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.innovacion.features.catalog.presentation.viewmodel.RolViewModel

@Composable
fun RolScreen(
    viewModel: RolViewModel = viewModel()
) {
    val roles by viewModel.roles.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarRoles()
    }

    val teal = Color(0xFF12A6AA)
    val lightBackground = Color(0xFFEAF7F7)
    val darkText = Color(0xFF3F3F3F)
    val mutedText = Color(0xFF777777)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(teal)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Roles",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Catálogo de roles institucionales",
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Listado de Roles",
                color = darkText,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Roles registrados en el sistema",
                color = mutedText
            )

            Spacer(modifier = Modifier.height(16.dp))

            error?.let {
                Text(
                    text = "Error: $it",
                    color = Color.Red
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(roles) { rol ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD9F4F4)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = rol.nombre.take(1).uppercase(),
                                    color = teal,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = rol.nombre.replace("_", " ").replaceFirstChar { it.uppercase() },
                                    color = darkText,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = rol.descripcion ?: "Sin descripción",
                                    color = mutedText
                                )

                                Text(
                                    text = if (rol.activo) "Activo" else "Inactivo",
                                    color = teal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}