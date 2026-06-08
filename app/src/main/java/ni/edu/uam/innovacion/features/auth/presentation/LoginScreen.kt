package ni.edu.uam.innovacion.features.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.innovacion.R
import ni.edu.uam.innovacion.di.appContainer
import ni.edu.uam.innovacion.ui.theme.UamGray
import ni.edu.uam.innovacion.ui.theme.UamLightBackground
import ni.edu.uam.innovacion.ui.theme.UamTextDark
import ni.edu.uam.innovacion.ui.theme.UamTurquoise
import ni.edu.uam.innovacion.ui.theme.UamWhite

@Composable
fun LoginRoute(
    onAuthenticated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container = LocalContext.current.appContainer
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.factory(container.authRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkSession()
    }

    LaunchedEffect(uiState.user) {
        if (uiState.user != null) {
            onAuthenticated()
        }
    }

    LoginScreen(
        uiState = uiState,
        onLogin = viewModel::login,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onLogin: (correo: String, contrasena: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = UamLightBackground
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(UamLightBackground)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_diem),
                    contentDescription = "Logo Direccion de Innovacion y Emprendimiento UAM",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(28.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = UamWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Acceso administrativo",
                                color = UamTextDark,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Sistema DIEM/UAM",
                                color = UamGray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = correo,
                            onValueChange = { correo = it },
                            enabled = !uiState.isLoading,
                            singleLine = true,
                            label = { Text("Correo") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = null
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(8.dp),
                            colors = loginTextFieldColors()
                        )

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = contrasena,
                            onValueChange = { contrasena = it },
                            enabled = !uiState.isLoading,
                            singleLine = true,
                            label = { Text("Contrasena") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible },
                                    enabled = !uiState.isLoading
                                ) {
                                    Icon(
                                        imageVector = if (passwordVisible) {
                                            Icons.Filled.VisibilityOff
                                        } else {
                                            Icons.Filled.Visibility
                                        },
                                        contentDescription = if (passwordVisible) {
                                            "Ocultar contrasena"
                                        } else {
                                            "Mostrar contrasena"
                                        }
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    onLogin(correo, contrasena)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            colors = loginTextFieldColors()
                        )

                        uiState.errorMessage?.let { message ->
                            LoginErrorMessage(message = message)
                        }

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            enabled = !uiState.isLoading,
                            onClick = {
                                focusManager.clearFocus()
                                onLogin(correo, contrasena)
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = UamTurquoise,
                                contentColor = UamWhite
                            ),
                            contentPadding = PaddingValues(horizontal = 18.dp)
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = UamWhite,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Iniciar sesion",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Direccion de Innovacion y Emprendimiento",
                    color = UamGray,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LoginErrorMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFF3F1),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = message,
            color = Color(0xFF9F2E24),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun loginTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = UamTurquoise,
    unfocusedBorderColor = Color(0xFFD8E6E9),
    focusedLabelColor = UamTurquoise,
    unfocusedLabelColor = UamGray,
    focusedLeadingIconColor = UamTurquoise,
    unfocusedLeadingIconColor = UamGray,
    focusedTrailingIconColor = UamTurquoise,
    unfocusedTrailingIconColor = UamGray,
    focusedTextColor = UamTextDark,
    unfocusedTextColor = UamTextDark,
    cursorColor = UamTurquoise
)
