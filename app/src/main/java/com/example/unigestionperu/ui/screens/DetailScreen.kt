package com.example.unigestionperu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.unigestionperu.data.local.CursoEntity
import com.example.unigestionperu.viewmodel.CursoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    cursoId: Int,
    usuarioId: Int,
    viewModel: CursoViewModel,
    onBack: () -> Unit,
) {
    var cursoState by remember { mutableStateOf<CursoEntity?>(null) }
    val matriculas by viewModel.matriculasDelUsuario.collectAsState()

    LaunchedEffect(cursoId, matriculas) {
        cursoState = viewModel.getCursoById(cursoId)
    }

    val curso = cursoState
    val isMatriculado = matriculas.any { it.cursoId == cursoId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Curso") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { paddingValues ->
        if (curso == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            val vacantesDisponibles = curso.cupoMaximo - curso.matriculados
            val sinVacantes = vacantesDisponibles <= 0

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    // Tarjeta Principal del Curso
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = curso.facultad,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = curso.nombre,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text("Ciclo: ${curso.ciclo}") },
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(curso.modalidad) },
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Banners de Alerta de Vacantes y Estado (RF09 y RF10)
                    when {
                        isMatriculado -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            "¡Ya te encuentras matriculado!",
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            "Tienes tu vacante asegurada para este periodo lectivo.",
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }
                                }
                            }
                        }
                        sinVacantes -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            "SIN VACANTES (0 CUPOS)",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                        )
                                        Text(
                                            "RF10: La matrícula está bloqueada porque se ha alcanzado el límite de cupos (${curso.cupoMaximo}).",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                        )
                                    }
                                }
                            }
                        }
                        else -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            "Vacantes Disponibles: $vacantesDisponibles",
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            "RF09: Cupo Máximo ${curso.cupoMaximo} | Matriculados: ${curso.matriculados}",
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Información Académica Adicional
                    Text(
                        "Información de la Asignatura",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ListItem(
                        headlineContent = { Text(curso.docente) },
                        supportingContent = { Text("Docente de Asignatura") },
                        leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
                    )
                    HorizontalDivider()

                    ListItem(
                        headlineContent = { Text(curso.horario) },
                        supportingContent = { Text("Horario de Clases") },
                        leadingContent = { Icon(Icons.Default.Event, contentDescription = null) },
                    )
                    HorizontalDivider()

                    ListItem(
                        headlineContent = { Text(curso.aula) },
                        supportingContent = { Text("Aula / Ambiente") },
                        leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    )
                }

                // Botón de Acción de Matrícula (RF08, RF09 y RF10)
                Column(modifier = Modifier.fillMaxWidth()) {
                    when {
                        isMatriculado -> {
                            OutlinedButton(
                                onClick = {
                                    viewModel.cancelarMatricula(curso.id, usuarioId)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error,
                                ),
                            ) {
                                Text("Anular mi Matrícula")
                            }
                        }
                        sinVacantes -> {
                            Button(
                                onClick = { },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("Matrícula Bloqueada (Sin Vacantes)")
                            }
                        }
                        else -> {
                            Button(
                                onClick = {
                                    viewModel.matricularEnCurso(curso.id, usuarioId)
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("Matricularme en este Curso")
                            }
                        }
                    }
                }
            }
        }
    }
}
