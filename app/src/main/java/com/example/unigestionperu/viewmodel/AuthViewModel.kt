package com.example.unigestionperu.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu.data.local.AppDatabase
import com.example.unigestionperu.data.repository.AuthRepository
import com.example.unigestionperu.model.RolUsuario
import com.example.unigestionperu.state.AuthUiState
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuthRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AuthRepository(db.usuarioDao())
    }

    var uiState by mutableStateOf(AuthUiState())
        private set

    var selectedRol by mutableStateOf(RolUsuario.ESTUDIANTE)
    var username by mutableStateOf("estudiante")
    var password by mutableStateOf("estudiante")

    // Campos para la pantalla de registro
    var regNombre by mutableStateOf("")
    var regCorreo by mutableStateOf("")
    var regUsername by mutableStateOf("")
    var regClave by mutableStateOf("")
    var regRol by mutableStateOf(RolUsuario.ESTUDIANTE)
    var regMensajeError by mutableStateOf<String?>(null)
    var regMensajeExito by mutableStateOf<String?>(null)

    fun selectRol(rol: RolUsuario) {
        selectedRol = rol
        when (rol) {
            RolUsuario.ESTUDIANTE -> {
                username = "estudiante"
                password = "estudiante"
            }
            RolUsuario.DOCENTE -> {
                username = "docente"
                password = "docente"
            }
            RolUsuario.ADMINISTRATIVO -> {
                username = "admin"
                password = "admin"
            }
        }
    }

    fun cargarUsuarioPorId(id: Int) {
        viewModelScope.launch {
            val usuario = repository.getUsuarioById(id)
            usuario?.let {
                uiState = uiState.copy(usuarioLogueado = it)
            }
        }
    }

    fun onLoginClick(onSuccess: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            uiState = uiState.copy(error = "Por favor, completa todos los campos")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            val usuario = repository.validarCredenciales(username, password)

            if (usuario != null) {
                uiState = uiState.copy(
                    isLoading = false,
                    usuarioLogueado = usuario,
                    loginExitoso = true,
                )
                onSuccess()
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Credenciales incorrectas o usuario no registrado",
                )
            }
        }
    }

    fun registrarUsuario(onSuccess: () -> Unit) {
        if (regNombre.isBlank() || regCorreo.isBlank() || regUsername.isBlank() || regClave.isBlank()) {
            regMensajeError = "Todos los campos son obligatorios"
            return
        }

        viewModelScope.launch {
            regMensajeError = null
            regMensajeExito = null
            val result = repository.registrarUsuario(
                nombre = regNombre,
                correo = regCorreo,
                clave = regClave,
                rol = regRol.name,
                username = regUsername,
            )

            result.onSuccess {
                regMensajeExito = "Registro exitoso. ¡Ahora puedes iniciar sesión!"
                // Limpiar campos
                regNombre = ""
                regCorreo = ""
                regUsername = ""
                regClave = ""
                onSuccess()
            }.onFailure { e ->
                regMensajeError = e.message ?: "Error al registrar el usuario"
            }
        }
    }
}
