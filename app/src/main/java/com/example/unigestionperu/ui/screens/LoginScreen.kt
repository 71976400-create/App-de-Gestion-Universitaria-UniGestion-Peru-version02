package com.example.unigestionperu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.unigestionperu.model.RolUsuario
import com.example.unigestionperu.ui.components.AppLogo
import com.example.unigestionperu.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel
) {
    val state = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppLogo(modifier = Modifier.size(100.dp))
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "UNIGESTIÓN PERÚ",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Selecciona tu rol:",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        val roles = listOf(
            RolUsuario.ESTUDIANTE to "Estudiante",
            RolUsuario.DOCENTE to "Docente",
            RolUsuario.ADMINISTRATIVO to "Administrativo"
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            roles.forEachIndexed { index, (rol, label) ->
                SegmentedButton(
                    selected = viewModel.selectedRol == rol,
                    onClick = { viewModel.selectRol(rol) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = roles.size
                    )
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.username = it },
            label = { Text("Usuario o Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        state.error?.let {
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.onLoginClick(onLoginSuccess) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Iniciar Sesión")
            }
        }

        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("¿No tienes una cuenta? Regístrate aquí")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val credencialesHint = when (viewModel.selectedRol) {
            RolUsuario.ESTUDIANTE -> "Credenciales por defecto: estudiante / estudiante"
            RolUsuario.DOCENTE -> "Credenciales por defecto: docente / docente"
            RolUsuario.ADMINISTRATIVO -> "Credenciales por defecto: admin / admin"
        }

        Text(
            text = credencialesHint,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
