package com.example.unigestionperu.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: String, // "ESTUDIANTE"
    val username: String = "",
    val facultad: String = "Ingeniería",
    val ciclo: String = "2026-I",
)
