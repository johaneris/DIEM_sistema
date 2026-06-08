package ni.edu.uam.innovacion.di

import android.content.Context
import ni.edu.uam.innovacion.InnovacionApplication

val Context.appContainer: AppContainer
    get() = (applicationContext as InnovacionApplication).container
