package com.example.unigestionperu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.unigestionperu.ui.components.InputField
import com.example.unigestionperu.viewmodel.CursoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: CursoViewModel,
    onSearchComplete: () -> Unit,
) {
    var queryText by remember { mutableStateOf(viewModel.busquedaQuery) }
    var selectedFacultad by remember { mutableStateOf(viewModel.facultadFiltro) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Búsqueda Avanzada y Filtros",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            value = queryText,
            onValueChange = { queryText = it },
            label = "Nombre de Asignatura o Docente",
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Seleccionar Facultad:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start),
        )

        val facultades = listOf("Todas", "Ingeniería", "Negocios", "Ciencias de la Salud", "Derecho", "Humanidades")

        facultades.forEach { facultad ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            ) {
                RadioButton(
                    selected = (selectedFacultad == facultad),
                    onClick = { selectedFacultad = facultad },
                )
                Text(
                    text = facultad,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.onBusquedaChange(queryText)
                viewModel.onFacultadChange(selectedFacultad)
                onSearchComplete()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Aplicar Filtros y Ver Cursos")
        }
    }
}
