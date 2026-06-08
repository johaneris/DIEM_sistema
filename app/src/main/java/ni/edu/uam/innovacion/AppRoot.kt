package ni.edu.uam.innovacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.innovacion.di.appContainer
import ni.edu.uam.innovacion.features.admin.presentation.AdminRoot
import ni.edu.uam.innovacion.features.auth.presentation.LoginScreen
import ni.edu.uam.innovacion.features.auth.presentation.LoginViewModel

@Composable
fun AppRoot() {
    val container = LocalContext.current.appContainer
    val sessionViewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.factory(container.authRepository)
    )
    val sessionState by sessionViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        sessionViewModel.checkSession()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            sessionState.isLoading && !sessionState.sessionChecked -> SessionLoading()
            sessionState.user == null -> LoginScreen(
                uiState = sessionState,
                onLogin = sessionViewModel::login
            )
            else -> AdminRoot(
                currentUser = sessionState.user,
                isLoggingOut = sessionState.isLoggingOut,
                logoutError = sessionState.logoutError,
                onLogout = sessionViewModel::logout,
                onDismissLogoutError = sessionViewModel::clearLogoutError,
                onSessionExpired = sessionViewModel::markSessionExpired
            )
        }
    }
}

@Composable
private fun SessionLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
