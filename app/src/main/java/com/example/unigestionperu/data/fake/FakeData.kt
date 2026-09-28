package com.example.unigestionperu.data.fake

import com.example.unigestionperu.model.RolUsuario
import com.example.unigestionperu.model.Usuario

object FakeData {
    val usuariosSimulados = listOf(
        Usuario(id = 1, username = "estudiante", nombreCompleto = "Juan Perez", rol = RolUsuario.ESTUDIANTE, correo = "estudiante@unigestion.edu.pe"),
        Usuario(id = 2, username = "docente", nombreCompleto = "Prof. Maria Garcia", rol = RolUsuario.DOCENTE, correo = "docente@unigestion.edu.pe"),
        Usuario(id = 3, username = "admin", nombreCompleto = "Admin System", rol = RolUsuario.ADMINISTRATIVO, correo = "admin@unigestion.edu.pe")
    )
}
