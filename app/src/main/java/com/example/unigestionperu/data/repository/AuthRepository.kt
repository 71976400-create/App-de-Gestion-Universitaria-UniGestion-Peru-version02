package com.example.unigestionperu.data.repository

import com.example.unigestionperu.data.local.UsuarioDao
import com.example.unigestionperu.data.local.UsuarioEntity
import com.example.unigestionperu.model.RolUsuario
import com.example.unigestionperu.model.Usuario

class AuthRepository(private val usuarioDao: UsuarioDao) {

    suspend fun validarCredenciales(userOrEmail: String, clave: String): Usuario? {
        val entity = usuarioDao.login(userOrEmail, clave) ?: return null
        val rolEnum = try {
            RolUsuario.valueOf(entity.rol.uppercase())
        } catch (_: Exception) {
            RolUsuario.ESTUDIANTE
        }
        return Usuario(
            id = entity.id,
            username = entity.username.ifEmpty { entity.correo },
            nombreCompleto = entity.nombre,
            rol = rolEnum,
            correo = entity.correo,
        )
    }

    suspend fun getUsuarioById(id: Int): Usuario? {
        val entity = usuarioDao.getUsuarioById(id) ?: return null
        val rolEnum = try {
            RolUsuario.valueOf(entity.rol.uppercase())
        } catch (_: Exception) {
            RolUsuario.ESTUDIANTE
        }
        return Usuario(
            id = entity.id,
            username = entity.username.ifEmpty { entity.correo },
            nombreCompleto = entity.nombre,
            rol = rolEnum,
            correo = entity.correo,
        )
    }

    suspend fun registrarUsuario(
        nombre: String,
        correo: String,
        clave: String,
        rol: String,
        username: String,
    ): Result<UsuarioEntity> {
        val existente = usuarioDao.findByUsernameOrCorreo(username, correo)
        if (existente != null) {
            return Result.failure(Exception("El usuario o correo ya está registrado"))
        }
        val nuevo = UsuarioEntity(
            nombre = nombre,
            correo = correo,
            clave = clave,
            rol = rol,
            username = username,
        )
        val id = usuarioDao.insert(nuevo)
        return Result.success(nuevo.copy(id = id.toInt()))
    }
}
