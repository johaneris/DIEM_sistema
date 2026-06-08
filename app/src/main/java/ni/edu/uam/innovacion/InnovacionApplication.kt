package ni.edu.uam.innovacion

import android.app.Application
import ni.edu.uam.innovacion.di.AppContainer

class InnovacionApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
