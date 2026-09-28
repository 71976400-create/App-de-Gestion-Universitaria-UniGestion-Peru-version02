package com.example.unigestionperu.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.unigestionperu.ui.components.ItemCard
import com.example.unigestionperu.viewmodel.CursoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    usuarioId: Int,
    viewModel: CursoViewModel,
    onCourseClick: (Int) -> Unit,
    onNavigateToForm: () -> Unit,
) {
    val cursos by viewModel.cursosFiltrados.collectAsState()
    val matriculas by viewModel.matriculasDelUsuario.collectAsState()
    var mostrarFiltros by remember { mutableStateOf(false) }

    val facultades = listOf("Todas", "Ingeniería", "Negocios", "Ciencias de la Salud", "Derecho", "Humanidades")
    val ciclos = listOf("Todos", "2026-I", "2026-II")
    val modalidades = listOf("Todas", "Presencial", "Virtual", "Híbrido")

    val matriculadosIds = remember(matriculas) { matriculas.map { it.cursoId }.toSet() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Barra de Búsqueda y Botones de Filtros y Búsqueda Avanzada
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = viewModel.busquedaQuery,
                onValueChange = { viewModel.onBusquedaChange(it) },
                placeholder = { Text("Buscar asignatura...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (viewModel.busquedaQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onBusquedaChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Botón de Búsqueda Avanzada (FormScreen)
            FilledTonalIconButton(
                onClick = onNavigateToForm,
            ) {
                Icon(Icons.Default.Tune, contentDescription = "Búsqueda Avanzada")
            }

            Spacer(modifier = Modifier.width(4.dp))

            FilledTonalIconButton(
                onClick = { mostrarFiltros = !mostrarFiltros },
            ) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = "Filtros",
                    tint = if ((viewModel.facultadFiltro != "Todas") || (viewModel.cicloFiltro != "Todos") || (viewModel.modalidadFiltro != "Todas")) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        LocalContentColor.current
                    },
                )
            }
        }

        // Sección de Filtros Desplegables (RF07)
        AnimatedVisibility(visible = mostrarFiltros) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Filtros de Búsqueda (RF07)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Filtro de Facultad
                    FiltroDropdown(
                        label = "Facultad",
                        opciones = facultades,
                        seleccionado = viewModel.facultadFiltro,
                        onSelect = { viewModel.onFacultadChange(it) },
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Filtro de Ciclo
                        Box(modifier = Modifier.weight(1f)) {
                            FiltroDropdown(
                                label = "Ciclo",
                                opciones = ciclos,
                                seleccionado = viewModel.cicloFiltro,
                                onSelect = { viewModel.onCicloChange(it) },
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        // Filtro de Modalidad
                        Box(modifier = Modifier.weight(1f)) {
                            FiltroDropdown(
                                label = "Modalidad",
                                opciones = modalidades,
                                seleccionado = viewModel.modalidadFiltro,
                                onSelect = { viewModel.onModalidadChange(it) },
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = { viewModel.resetFiltros() },
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text("Restablecer Filtros")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Conteo de Cursos encontrados utilizando usuarioId
        Text(
            text = "Usuario ID: $usuarioId | Mostrando ${cursos.size} asignaturas",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 8.dp),
        )

        // Lista de Cursos
        if (cursos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "No se encontraron cursos con los filtros aplicados",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.resetFiltros() }) {
                        Text("Ver todos los cursos")
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f),
            ) {
                items(
                    items = cursos,
                    key = { it.id },
                ) { curso ->
                    ItemCard(
                        curso = curso,
                        isMatriculado = matriculadosIds.contains(curso.id),
                        onClick = { onCourseClick(curso.id) },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltroDropdown(
    label: String,
    opciones: List<String>,
    seleccionado: String,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = seleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSelect(opcion)
                        expanded = false
                    },
                )
            }
        }
    }
}
