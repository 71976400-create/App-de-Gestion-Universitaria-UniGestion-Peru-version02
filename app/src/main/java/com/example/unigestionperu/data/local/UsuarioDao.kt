package com.example.unigestionperu.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(usuarios: List<UsuarioEntity>): List<Long>

    @Query("SELECT * FROM usuarios WHERE (username = :userOrEmail OR correo = :userOrEmail) AND clave = :clave LIMIT 1")
    suspend fun login(userOrEmail: String, clave: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUsuarioById(id: Int): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE username = :username OR correo = :correo LIMIT 1")
    suspend fun findByUsernameOrCorreo(username: String, correo: String): UsuarioEntity?
}
