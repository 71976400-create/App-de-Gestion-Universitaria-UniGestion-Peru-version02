package com.example.unigestionperu.data.repository

import com.example.unigestionperu.data.local.CursoDao
import com.example.unigestionperu.data.local.CursoEntity
import com.example.unigestionperu.data.local.MatriculaDao
import com.example.unigestionperu.data.local.MatriculaEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CursoRepository(
    private val cursoDao: CursoDao,
    private val matriculaDao: MatriculaDao,
) {
    val todosLosCursos: Flow<List<CursoEntity>> = cursoDao.getAllCursos()

    suspend fun getCursoById(id: Int): CursoEntity? {
        return cursoDao.getCursoById(id)
    }

    fun getMatriculasDelUsuario(usuarioId: Int): Flow<List<MatriculaEntity>> {
        return matriculaDao.getMatriculasByUsuario(usuarioId)
    }

    suspend fun matricularEstudiante(usuarioId: Int, cursoId: Int): Result<String> {
        val curso = cursoDao.getCursoById(cursoId)
            ?: return Result.failure(Exception("Curso no encontrado"))

        // RF09 & RF10: Cálculo de vacantes disponibles
        val vacantesDisponibles = curso.cupoMaximo - curso.matriculados
        if (vacantesDisponibles <= 0) {
            return Result.failure(Exception("Matrícula denegada: El curso no cuenta con vacantes disponibles (0 vacantes)."))
        }

        val yaMatriculado = matriculaDao.getMatricula(usuarioId, cursoId)
        if (yaMatriculado != null) {
            return Result.failure(Exception("Ya se encuentra matriculado en este curso."))
        }

        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val nuevaMatricula = MatriculaEntity(
            usuarioId = usuarioId,
            cursoId = cursoId,
            fecha = fechaActual,
        )

        matriculaDao.insert(nuevaMatricula)

        // Incrementar la cantidad de matriculados en el curso
        val cursoActualizado = curso.copy(matriculados = curso.matriculados + 1)
        cursoDao.updateCurso(cursoActualizado)

        return Result.success("Matrícula realizada exitosamente en ${curso.nombre}")
    }

    suspend fun cancelarMatricula(usuarioId: Int, cursoId: Int): Result<String> {
        val matriculaExistente = matriculaDao.getMatricula(usuarioId, cursoId)
            ?: return Result.failure(Exception("No se encontró la matrícula"))

        matriculaDao.deleteMatricula(usuarioId, cursoId)

        val curso = cursoDao.getCursoById(cursoId)
        if ((curso != null) && (curso.matriculados > 0)) {
            val cursoActualizado = curso.copy(matriculados = curso.matriculados - 1)
            cursoDao.updateCurso(cursoActualizado)
        }

        return Result.success("Matrícula cancelada correctamente (${matriculaExistente.fecha})")
    }
}
