package com.example.unigestionperu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.unigestionperu.model.RolUsuario
import com.example.unigestionperu.ui.components.AppLogo
import com.example.unigestionperu.ui.components.InputField
import com.example.unigestionperu.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onBackToLogin) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo(modifier = Modifier.size(80.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear nueva cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                value = viewModel.regNombre,
                onValueChange = { viewModel.regNombre = it },
                label = "Nombre Completo"
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputField(
                value = viewModel.regCorreo,
                onValueChange = { viewModel.regCorreo = it },
                label = "Correo Institucional"
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputField(
                value = viewModel.regUsername,
                onValueChange = { viewModel.regUsername = it },
                label = "Nombre de Usuario"
            )

            Spacer(modifier = Modifier.height(8.dp))

            InputField(
                value = viewModel.regClave,
                onValueChange = { viewModel.regClave = it },
                label = "Contraseña",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Selecciona el Rol:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )

            val roles = listOf(
                RolUsuario.ESTUDIANTE to "Estudiante",
                RolUsuario.DOCENTE to "Docente",
                RolUsuario.ADMINISTRATIVO to "Administrativo"
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                roles.forEachIndexed { index, (rol, label) ->
                    SegmentedButton(
                        selected = viewModel.regRol == rol,
                        onClick = { viewModel.regRol = rol },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = roles.size
                        )
                    ) {
                        Text(label, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            viewModel.regMensajeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            viewModel.regMensajeExito?.let {
                Text(
                    text = it,
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.registrarUsuario(onSuccess = onBackToLogin)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarme")
            }

            TextButton(
                onClick = onBackToLogin,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("¿Ya tienes una cuenta? Inicia Sesión")
            }
        }
    }
}
