package com.example.unigestionperu.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MatriculaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(matricula: MatriculaEntity): Long

    @Query("SELECT * FROM matriculas WHERE usuarioId = :usuarioId")
    fun getMatriculasByUsuario(usuarioId: Int): Flow<List<MatriculaEntity>>

    @Query("SELECT * FROM matriculas WHERE usuarioId = :usuarioId AND cursoId = :cursoId LIMIT 1")
    suspend fun getMatricula(usuarioId: Int, cursoId: Int): MatriculaEntity?

    @Query("DELETE FROM matriculas WHERE usuarioId = :usuarioId AND cursoId = :cursoId")
    suspend fun deleteMatricula(usuarioId: Int, cursoId: Int): Int
}
