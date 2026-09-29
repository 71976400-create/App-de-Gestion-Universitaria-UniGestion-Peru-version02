package com.example.unigestionperu.data.fake

import com.example.unigestionperu.model.RolUsuario
import com.example.unigestionperu.model.Usuario

object FakeData {
    val usuariosSimulados = listOf(
        Usuario(
            id = 1,
            username = "estudiante",
            nombreCompleto = "Juan Perez",
            rol = RolUsuario.ESTUDIANTE,
            correo = "estudiante@unigestion.edu.pe",
        ),
    )
}
