package pl.skomunikacja.synclyapp.helpers

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.skomunikacja.synclyapp.model.ApplicationSettings
import pl.skomunikacja.synclyapp.model.AuthenticationResponse
import pl.skomunikacja.synclyapp.model.Image
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.post.Post
import java.io.File

object ApplicationManager {
    private val gson = Gson()
    private const val SETTINGS_FILE_NAME = "SynclyAppSettings.json";

    private lateinit var settingsFile: File

    private val _authenticationData = MutableStateFlow<AuthenticationResponse?>(null)
    val authenticationData: StateFlow<AuthenticationResponse?> = _authenticationData.asStateFlow()

    fun initialize(context: Context) {
        settingsFile = File(context.filesDir, SETTINGS_FILE_NAME)

        println("INITIALIZATION SETTINGS FILE!")

        if (settingsFile.exists()) {
            try {
                val json = settingsFile.readText()
                val settings = gson.fromJson(json, ApplicationSettings::class.java)
                _authenticationData.value = settings.authenticationData
            } catch (ex: Exception) {
                println("Failed to read settings: ${ex.message}")
            }
        } else {
            try {
                val defaultSettings = ApplicationSettings(
                    authenticationData = null,
                )
                settingsFile.writeText(gson.toJson(defaultSettings))
            } catch (ex: Exception) {
                println("Failed to create settings file: ${ex.message}")
            }
        }
    }

    private fun saveToFile() {
        val settings = ApplicationSettings(
            authenticationData = _authenticationData.value,
        )
        settingsFile.writeText(gson.toJson(settings))

        println("DATA SAVED!")
    }

    fun changeAuthenticationData(data: AuthenticationResponse) {
        _authenticationData.value = data
        saveToFile()
    }


    fun changeAvatarInAuthenticationData(avatar: Image?) {
        _authenticationData.update {
            it?.copy(
                avatar = avatar
            )
        }
    }

    fun logoutUser() {
        _authenticationData.value = null
        saveToFile()
    }
}