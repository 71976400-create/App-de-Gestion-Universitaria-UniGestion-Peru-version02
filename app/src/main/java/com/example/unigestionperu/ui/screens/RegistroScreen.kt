package com.example.unigestionperu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.unigestionperu.ui.components.AppLogo
import com.example.unigestionperu.ui.components.InputField
import com.example.unigestionperu.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel,
) {
    val facultades = listOf("Ingeniería", "Negocios", "Ciencias de la Salud", "Derecho", "Humanidades")
    val ciclos = listOf("2026-I", "2026-II")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Alumno") },
                navigationIcon = {
                    IconButton(onClick = onBackToLogin) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppLogo(modifier = Modifier.size(80.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear Cuenta de Alumno",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                value = viewModel.regNombre,
                onValueChange = { viewModel.regNombre = it },
                label = "Nombre Completo",
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputField(
                value = viewModel.regCorreo,
                onValueChange = { viewModel.regCorreo = it },
                label = "Correo Institucional",
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputField(
                value = viewModel.regUsername,
                onValueChange = { viewModel.regUsername = it },
                label = "Nombre de Usuario",
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputField(
                value = viewModel.regClave,
                onValueChange = { viewModel.regClave = it },
                label = "Contraseña",
                visualTransformation = PasswordVisualTransformation(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Facultad
            Text(
                text = "Facultad / Escuela:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start),
            )
            Spacer(modifier = Modifier.height(4.dp))
            facultades.forEach { facultad ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                ) {
                    RadioButton(
                        selected = (viewModel.regFacultad == facultad),
                        onClick = { viewModel.regFacultad = facultad },
                    )
                    Text(text = facultad, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de Ciclo Académico
            Text(
                text = "Ciclo Académico de Ingreso:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start),
            )
            Spacer(modifier = Modifier.height(4.dp))
            ciclos.forEach { ciclo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                ) {
                    RadioButton(
                        selected = (viewModel.regCiclo == ciclo),
                        onClick = { viewModel.regCiclo = ciclo },
                    )
                    Text(text = ciclo, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            viewModel.regMensajeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            viewModel.regMensajeExito?.let {
                Text(
                    text = it,
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.registrarUsuario(onSuccess = onBackToLogin)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Registrarme como Alumno")
            }

            TextButton(
                onClick = onBackToLogin,
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text("¿Ya tienes una cuenta? Inicia Sesión")
            }
        }
    }
}
