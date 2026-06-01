package ni.edu.uam.innovacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ni.edu.uam.innovacion.features.user.presentation.screen.InnovacionUamPrototypeScreen
import ni.edu.uam.innovacion.ui.theme.Sistema_Innovacion_FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sistema_Innovacion_FrontendTheme {
                InnovacionUamPrototypeScreen()
            }
        }
    }
}
