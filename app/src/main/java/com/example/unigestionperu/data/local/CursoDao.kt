package com.example.unigestionperu.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CursoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cursos: List<CursoEntity>): List<Long>

    @Query("SELECT * FROM cursos")
    fun getAllCursos(): Flow<List<CursoEntity>>

    @Query("SELECT * FROM cursos WHERE id = :id")
    suspend fun getCursoById(id: Int): CursoEntity?

    @Update
    suspend fun updateCurso(curso: CursoEntity): Int

    @Query("SELECT COUNT(*) FROM cursos")
    suspend fun getCount(): Int
}
