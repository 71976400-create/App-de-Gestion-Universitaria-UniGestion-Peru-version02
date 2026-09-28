package com.example.unigestionperu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val titles = listOf("Inicio", "Catálogo Cursos", "Mis Matrículas", "Mi Perfil")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.School, Icons.Filled.Book, Icons.Filled.Person)

    val usuarioId = usuarioLogueado?.id ?: 1
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(usuarioId) {
        cursoViewModel.setUsuarioId(usuarioId)
    }

    // Feedback de Snackbar para matrícula y vacantes (RF09 y RF10)
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
                            contentDescription = if (ThemeState.isDarkTheme) "Cambiar a Modo Claro" else "Cambiar a Modo Oscuro",
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
                0 -> WelcomeContent(usuarioLogueado = usuarioLogueado, onNavigateToCursos = { selectedItem = 1 })
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
                3 -> PerfilContent(usuarioLogueado = usuarioLogueado, onLogout = onLogout)
            }
        }
    }
}

@Composable
fun WelcomeContent(usuarioLogueado: Usuario?, onNavigateToCursos: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Bienvenido a UniGestión Perú",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Ciclo Académico: 2026-I", fontWeight = FontWeight.Bold)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Estudiante: ${usuarioLogueado?.nombreCompleto ?: "Estudiante Demo"}")
                Text("Rol: ${usuarioLogueado?.rol ?: "ESTUDIANTE"}")
                Text("Correo: ${usuarioLogueado?.correo ?: "estudiante@unigestion.edu.pe"}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToCursos,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Explorar Catálogo de Cursos (30+ Asignaturas)")
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

    val matriculadosIds = remember(matriculas) { matriculas.map { it.cursoId }.toSet() }
    val cursosMatriculados = remember(todosLosCursos, matriculadosIds) {
        todosLosCursos.filter { matriculadosIds.contains(it.id) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Mis Cursos Matriculados",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        if (cursosMatriculados.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Aún no te has matriculado en ningún curso.",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    items = cursosMatriculados,
                    key = { it.id },
                ) { curso ->
                    ItemCard(
                        curso = curso,
                        isMatriculado = true,
                        onClick = { onCourseClick(curso.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun PerfilContent(usuarioLogueado: Usuario?, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Perfil del Usuario",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nombre: ${usuarioLogueado?.nombreCompleto ?: "Juan Perez"}", fontWeight = FontWeight.Bold)
                Text("Usuario: ${usuarioLogueado?.username ?: "estudiante"}")
                Text("Correo: ${usuarioLogueado?.correo ?: "estudiante@unigestion.edu.pe"}")
                Text("Rol: ${usuarioLogueado?.rol ?: "ESTUDIANTE"}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            Text("Cerrar Sesión")
        }
    }
}
