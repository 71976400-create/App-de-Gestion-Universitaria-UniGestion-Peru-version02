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
    val rol: String, // "ESTUDIANTE", "DOCENTE", "ADMINISTRATIVO"
    val username: String = ""
)
