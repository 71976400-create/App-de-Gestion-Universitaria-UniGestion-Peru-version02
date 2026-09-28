package com.example.unigestionperu.model

data class Usuario(
    val id: Int = 0,
    val username: String,
    val nombreCompleto: String,
    val rol: RolUsuario,
    val correo: String = ""
)
