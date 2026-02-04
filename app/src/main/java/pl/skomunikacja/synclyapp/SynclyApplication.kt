package pl.skomunikacja.synclyapp

import android.app.Application
import pl.skomunikacja.synclyapp.helpers.ApplicationManager

class SynclyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ApplicationManager.initialize(this)
    }
}