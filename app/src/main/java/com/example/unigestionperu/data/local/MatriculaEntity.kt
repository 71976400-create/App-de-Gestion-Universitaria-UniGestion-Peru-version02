package com.example.unigestionperu.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matriculas")
data class MatriculaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val cursoId: Int,
    val fecha: String = "",
    val nota: Double? = null,
)
