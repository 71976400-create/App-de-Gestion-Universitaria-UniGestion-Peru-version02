package com.example.unigestionperu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class EventoAcademico(
    val fecha: String,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
)

@Composable
fun CalendarioScreen() {
    val eventos = listOf(
        EventoAcademico("01 Mar - 15 Mar 2026", "Matrícula Regular 2026-I", "Proceso de inscripción de asignaturas en el sistema Room.", "Matrícula"),
        EventoAcademico("16 Mar 2026", "Inicio de Clases", "Comienzo oficial de actividades lectivas del ciclo 2026-I.", "Clases"),
        EventoAcademico("04 May - 09 May 2026", "Exámenes Parciales", "Evaluación de conocimientos de mitad de periodo lectivo.", "Examen"),
        EventoAcademico("06 Jul - 11 Jul 2026", "Exámenes Finales", "Evaluaciones finales de todas las asignaturas matriculadas.", "Examen"),
        EventoAcademico("18 Jul 2026", "Cierre de Actas y Notas", "Publicación definitiva de actas y calificaciones en `matriculas.nota`.", "Cierre"),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Calendario Académico 2026-I",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = "Fechas clave del periodo lectivo oficial",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(eventos) { evento ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = when (evento.tipo) {
                                "Matrícula" -> MaterialTheme.colorScheme.primaryContainer
                                "Examen" -> MaterialTheme.colorScheme.errorContainer
                                else -> MaterialTheme.colorScheme.secondaryContainer
                            },
                        ) {
                            Icon(
                                Icons.Default.Event,
                                contentDescription = null,
                                tint = when (evento.tipo) {
                                    "Matrícula" -> MaterialTheme.colorScheme.primary
                                    "Examen" -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.secondary
                                },
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(28.dp),
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = evento.fecha,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                )
                                AssistChip(
                                    onClick = { },
                                    label = { Text(evento.tipo, style = MaterialTheme.typography.labelSmall) },
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = evento.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = evento.descripcion,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            )
                        }
                    }
                }
            }
        }
    }
}
