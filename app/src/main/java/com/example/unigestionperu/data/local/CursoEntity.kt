package com.example.unigestionperu.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cursos")
data class CursoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val facultad: String,
    val ciclo: String,
    val modalidad: String,
    val cupoMaximo: Int,
    val matriculados: Int = 0,
    val docente: String = "Docente Asignado",
    val horario: String = "Lun - Mie 08:00 - 10:00",
    val aula: String = "Aula 302",
)
