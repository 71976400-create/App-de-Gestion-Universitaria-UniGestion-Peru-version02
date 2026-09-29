package com.example.unigestionperu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unigestionperu.model.Usuario
import com.example.unigestionperu.ui.components.ItemCard
import com.example.unigestionperu.viewmodel.CursoViewModel
import com.example.unigestionperu.viewmodel.ThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    usuarioLogueado: Usuario?,
    onCourseClick: (Int) -> Unit,
    onNavigateToForm: () -> Unit,
    onLogout: () -> Unit,
    cursoViewModel: CursoViewModel = viewModel(),
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val titles = listOf("Inicio", "Catálogo", "Matrículas", "Calendario", "Perfil")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.School,
        Icons.Filled.Book,
        Icons.Filled.CalendarMonth,
        Icons.Filled.Person,
    )

    val usuarioId = usuarioLogueado?.id ?: 1
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(usuarioId) {
        cursoViewModel.setUsuarioId(usuarioId)
    }

    val mensajeSnackbar = cursoViewModel.mensajeSnackbar
    LaunchedEffect(mensajeSnackbar) {
        mensajeSnackbar?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            cursoViewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(titles[selectedItem], fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    IconButton(
                        onClick = { ThemeState.isDarkTheme = !ThemeState.isDarkTheme },
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Icon(
                            imageVector = if (ThemeState.isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.DarkMode,
                            contentDescription = "Cambiar Tema",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                titles.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                    )
                }
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (selectedItem) {
                0 -> WelcomeDashboardContent(
                    usuarioLogueado = usuarioLogueado,
                    cursoViewModel = cursoViewModel,
                    onNavigateToTab = { selectedItem = it },
                )
                1 -> ListScreen(
                    usuarioId = usuarioId,
                    viewModel = cursoViewModel,
                    onCourseClick = onCourseClick,
                    onNavigateToForm = onNavigateToForm,
                )
                2 -> MisMatriculasContent(
                    usuarioId = usuarioId,
                    viewModel = cursoViewModel,
                    onCourseClick = onCourseClick,
                )
                3 -> CalendarioScreen()
                4 -> PerfilContent(usuarioLogueado = usuarioLogueado, onLogout = onLogout)
            }
        }
    }
}

@Composable
fun WelcomeDashboardContent(
    usuarioLogueado: Usuario?,
    cursoViewModel: CursoViewModel,
    onNavigateToTab: (Int) -> Unit,
) {
    val matriculas by cursoViewModel.matriculasDelUsuario.collectAsState()
    val cursos by cursoViewModel.cursosFiltrados.collectAsState()

    val nombre = usuarioLogueado?.nombreCompleto ?: "Estudiante Demo"
    val initials = nombre.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Banner de Bienvenida con Avatar
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = initials.ifEmpty { "U" },
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "¡Hola, $nombre!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Periodo Académico: 2026-I • Alumno",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Notificación / Aviso Institucional
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Matrícula Regular 2026-I Activa",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Valida tus vacantes y matricúlate sin perder tu avance en Room.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjetas de Métricas Rápidas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToTab(2) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Cursos Inscritos", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${matriculas.size}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToTab(1) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Catálogo General", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${cursos.size}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Accesos Rápidos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onNavigateToTab(1) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Icon(Icons.Default.School, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Explorar Catálogo de Cursos (30+ Asignaturas)", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onNavigateToTab(3) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver Calendario Académico 2026", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MisMatriculasContent(
    usuarioId: Int,
    viewModel: CursoViewModel,
    onCourseClick: (Int) -> Unit,
) {
    val matriculas by viewModel.matriculasDelUsuario.collectAsState()
    val todosLosCursos by viewModel.cursosFiltrados.collectAsState()

    val matriculasMap = remember(matriculas) { matriculas.associateBy { it.cursoId } }
    val cursosMatriculados = remember(todosLosCursos, matriculasMap) {
        todosLosCursos.filter { matriculasMap.containsKey(it.id) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Resumen de Matrícula
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Mis Asignaturas Matriculadas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = "Periodo 2026-I • Total: ${cursosMatriculados.size} cursos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        text = "${cursosMatriculados.size}",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cursosMatriculados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Book,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aún no te has matriculado en ningún curso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Explora el catálogo general para inscribirte en tus asignaturas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                items(
                    items = cursosMatriculados,
                    key = { it.id },
                ) { curso ->
                    ItemCard(
                        curso = curso,
                        isMatriculado = true,
                        nota = matriculasMap[curso.id]?.nota,
                        onClick = { onCourseClick(curso.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun PerfilContent(usuarioLogueado: Usuario?, onLogout: () -> Unit) {
    val nombre = usuarioLogueado?.nombreCompleto ?: "Juan Perez"
    val initials = nombre.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Avatar grande
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initials.ifEmpty { "JP" },
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = nombre,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Estudiante de Pregrado • UniGestión Perú",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de Identificación Universitaria
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("CARNET DE ESTUDIANTE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("2026-I", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                PerfilItemInfo(label = "Código de Alumno", value = "#U20260${usuarioLogueado?.id ?: 1}")
                PerfilItemInfo(label = "Usuario Institucional", value = usuarioLogueado?.username ?: "estudiante")
                PerfilItemInfo(label = "Correo Electrónico", value = usuarioLogueado?.correo ?: "estudiante@unigestion.edu.pe")
                PerfilItemInfo(label = "Facultad / Escuela", value = usuarioLogueado?.facultad ?: "Ingeniería")
                PerfilItemInfo(label = "Ciclo Académico", value = usuarioLogueado?.ciclo ?: "2026-I")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            Icon(Icons.Default.Person, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión Segura", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PerfilItemInfo(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Spacer(modifier = Modifier.height(4.dp))
}
