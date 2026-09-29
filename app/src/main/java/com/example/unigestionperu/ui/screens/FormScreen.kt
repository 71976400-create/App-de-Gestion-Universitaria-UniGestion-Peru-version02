package com.example.unigestionperu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    var selectedCiclo by remember { mutableStateOf(viewModel.cicloFiltro) }
    var selectedModalidad by remember { mutableStateOf(viewModel.modalidadFiltro) }

    val facultades = listOf("Todas", "Ingeniería", "Negocios", "Ciencias de la Salud", "Derecho", "Humanidades")
    val ciclos = listOf("Todos", "2026-I", "2026-II")
    val modalidades = listOf("Todas", "Presencial", "Virtual", "Híbrido")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
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

            // Facultad
            Text(
                text = "Filtrar por Facultad / Escuela:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(8.dp))
            facultades.forEach { facultad ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                ) {
                    RadioButton(
                        selected = (selectedFacultad == facultad),
                        onClick = { selectedFacultad = facultad },
                    )
                    Text(text = facultad, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ciclo Académico
            Text(
                text = "Filtrar por Ciclo Académico:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ciclos.forEach { ciclo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                ) {
                    RadioButton(
                        selected = (selectedCiclo == ciclo),
                        onClick = { selectedCiclo = ciclo },
                    )
                    Text(text = ciclo, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Modalidad
            Text(
                text = "Filtrar por Modalidad (Presencial / Virtual):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(8.dp))
            modalidades.forEach { modalidad ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                ) {
                    RadioButton(
                        selected = (selectedModalidad == modalidad),
                        onClick = { selectedModalidad = modalidad },
                    )
                    Text(text = modalidad, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.onBusquedaChange(queryText)
                    viewModel.onFacultadChange(selectedFacultad)
                    viewModel.onCicloChange(selectedCiclo)
                    viewModel.onModalidadChange(selectedModalidad)
                    onSearchComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text("Aplicar Filtros y Ver Cursos", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
