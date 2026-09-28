package com.example.unigestionperu.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu.data.local.AppDatabase
import com.example.unigestionperu.data.local.CursoEntity
import com.example.unigestionperu.data.local.MatriculaEntity
import com.example.unigestionperu.data.repository.CursoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CursoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CursoRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = CursoRepository(db.cursoDao(), db.matriculaDao())
    }

    // Filtros
    var facultadFiltro by mutableStateOf("Todas")
    var cicloFiltro by mutableStateOf("Todos")
    var modalidadFiltro by mutableStateOf("Todas")
    var busquedaQuery by mutableStateOf("")

    // Mensajes para Feedback de UI (Snackbar / Toast)
    var mensajeSnackbar by mutableStateOf<String?>(null)

    // Flow de cursos combinados con los criterios de filtrado (RF07)
    private val _busqueda = MutableStateFlow("")
    private val _facultad = MutableStateFlow("Todas")
    private val _ciclo = MutableStateFlow("Todos")
    private val _modalidad = MutableStateFlow("Todas")

    // Flow con los IDs de cursos en los que el alumno actual está matriculado
    private val _usuarioId = MutableStateFlow(1) // Por defecto ID 1 (Juan Perez / estudiante)
    
    val matriculasDelUsuario: StateFlow<List<MatriculaEntity>> = _usuarioId
        .combine(repository.todosLosCursos) { usuarioId, _ -> usuarioId }
        .combine(repository.getMatriculasDelUsuario(1)) { _, matriculas -> matriculas }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val cursosFiltrados: StateFlow<List<CursoEntity>> = combine(
        repository.todosLosCursos,
        _busqueda,
        _facultad,
        _ciclo,
        _modalidad,
    ) { listaCursos, query, facultad, ciclo, modalidad ->
        listaCursos.filter { curso ->
            val coincideBusqueda = query.isBlank() || curso.nombre.contains(query, ignoreCase = true) || curso.docente.contains(query, ignoreCase = true)
            val coincideFacultad = (facultad == "Todas") || curso.facultad.equals(facultad, ignoreCase = true)
            val coincideCiclo = (ciclo == "Todos") || curso.ciclo.equals(ciclo, ignoreCase = true)
            val coincideModalidad = (modalidad == "Todas") || curso.modalidad.equals(modalidad, ignoreCase = true)
            
            coincideBusqueda && coincideFacultad && coincideCiclo && coincideModalidad
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    fun setUsuarioId(usuarioId: Int) {
        _usuarioId.value = usuarioId.takeIf { it > 0 } ?: 1
    }

    fun onBusquedaChange(query: String) {
        busquedaQuery = query
        _busqueda.value = query
    }

    fun onFacultadChange(facultad: String) {
        facultadFiltro = facultad
        _facultad.value = facultad
    }

    fun onCicloChange(ciclo: String) {
        cicloFiltro = ciclo
        _ciclo.value = ciclo
    }

    fun onModalidadChange(modalidad: String) {
        modalidadFiltro = modalidad
        _modalidad.value = modalidad
    }

    fun resetFiltros() {
        busquedaQuery = ""
        facultadFiltro = "Todas"
        cicloFiltro = "Todos"
        modalidadFiltro = "Todas"

        _busqueda.value = ""
        _facultad.value = "Todas"
        _ciclo.value = "Todos"
        _modalidad.value = "Todas"
    }

    // RF08, RF09 & RF10: Proceso de Matrícula
    fun matricularEnCurso(cursoId: Int, usuarioId: Int) {
        viewModelScope.launch {
            val res = repository.matricularEstudiante(usuarioId, cursoId)
            res.onSuccess { msg ->
                mensajeSnackbar = msg
            }.onFailure { err ->
                mensajeSnackbar = err.message ?: "Error al procesar la matrícula"
            }
        }
    }

    fun cancelarMatricula(cursoId: Int, usuarioId: Int) {
        viewModelScope.launch {
            val res = repository.cancelarMatricula(usuarioId, cursoId)
            res.onSuccess { msg ->
                mensajeSnackbar = msg
            }.onFailure { err ->
                mensajeSnackbar = err.message ?: "Error al cancelar la matrícula"
            }
        }
    }

    suspend fun getCursoById(id: Int): CursoEntity? {
        return repository.getCursoById(id)
    }

    fun clearSnackbar() {
        mensajeSnackbar = null
    }
}
