package com.example.unigestionperu.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.unigestionperu.data.fake.FakeData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UsuarioEntity::class, CursoEntity::class, MatriculaEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun cursoDao(): CursoDao
    abstract fun matriculaDao(): MatriculaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "unigestion_peru.db",
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateData(database)
                }
            }
        }

        suspend fun prepopulateData(database: AppDatabase) {
            // Prepopulación de Usuarios por defecto usando FakeData
            val usuarioDao = database.usuarioDao()
            val usuariosIniciales = FakeData.usuariosSimulados.map { user ->
                UsuarioEntity(
                    nombre = user.nombreCompleto,
                    correo = user.correo,
                    clave = user.username,
                    rol = user.rol.name,
                    username = user.username,
                )
            }
            usuarioDao.insertAll(usuariosIniciales)

            // Prepopulación de 31 Cursos para cumplir RF06 (mínimo 30 cursos)
            val cursoDao = database.cursoDao()
            if (cursoDao.getCount() == 0) {
                val cursosIniciales = listOf(
                    CursoEntity(
                        nombre = "Desarrollo de Apps Móviles",
                        facultad = "Ingeniería",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 30,
                        matriculados = 28,
                        docente = "Ing. Carlos Mendoza",
                        horario = "Lun - Mie 08:00 - 10:00",
                        aula = "Lab-301",
                    ),
                    CursoEntity(
                        nombre = "Base de Datos Avanzada",
                        facultad = "Ingeniería",
                        ciclo = "2026-I",
                        modalidad = "Virtual",
                        cupoMaximo = 35,
                        matriculados = 35, // 0 vacantes
                        docente = "Ing. Ana Torres",
                        horario = "Mar - Jue 10:00 - 12:00",
                        aula = "Virtual-01",
                    ),
                    CursoEntity(
                        nombre = "Arquitectura de Software",
                        facultad = "Ingeniería",
                        ciclo = "2026-I",
                        modalidad = "Híbrido",
                        cupoMaximo = 25,
                        matriculados = 20,
                        docente = "Ing. Roberto Gomez",
                        horario = "Vie 14:00 - 18:00",
                        aula = "Lab-102",
                    ),
                    CursoEntity(
                        nombre = "Inteligencia Artificial",
                        facultad = "Ingeniería",
                        ciclo = "2026-II",
                        modalidad = "Virtual",
                        cupoMaximo = 40,
                        matriculados = 38,
                        docente = "Dr. Luis Paredes",
                        horario = "Lun - Mie 18:00 - 20:00",
                        aula = "Virtual-02",
                    ),
                    CursoEntity(
                        nombre = "Redes y Comunicaciones",
                        facultad = "Ingeniería",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 30,
                        matriculados = 15,
                        docente = "Ing. Pedro Castillo",
                        horario = "Mar - Jue 14:00 - 16:00",
                        aula = "Lab-204",
                    ),
                    CursoEntity(
                        nombre = "Ingeniería de Requerimientos",
                        facultad = "Ingeniería",
                        ciclo = "2026-II",
                        modalidad = "Presencial",
                        cupoMaximo = 28,
                        matriculados = 22,
                        docente = "Dra. Carmen Silva",
                        horario = "Mie - Sab 08:00 - 10:00",
                        aula = "Aula-201",
                    ),
                    CursoEntity(
                        nombre = "Ciberseguridad",
                        facultad = "Ingeniería",
                        ciclo = "2026-II",
                        modalidad = "Virtual",
                        cupoMaximo = 30,
                        matriculados = 30, // 0 vacantes
                        docente = "Ing. Fernando Ramos",
                        horario = "Lun - Jue 20:00 - 22:00",
                        aula = "Virtual-03",
                    ),
                    CursoEntity(
                        nombre = "Algoritmos y Estructura de Datos",
                        facultad = "Ingeniería",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 40,
                        matriculados = 32,
                        docente = "Ing. Maria Lopez",
                        horario = "Lun - Mie 10:00 - 12:00",
                        aula = "Lab-101",
                    ),
                    CursoEntity(
                        nombre = "Computación en la Nube",
                        facultad = "Ingeniería",
                        ciclo = "2026-II",
                        modalidad = "Híbrido",
                        cupoMaximo = 30,
                        matriculados = 18,
                        docente = "Ing. Diego Vega",
                        horario = "Sab 09:00 - 13:00",
                        aula = "Lab-302",
                    ),
                    CursoEntity(
                        nombre = "Sistemas Operativos",
                        facultad = "Ingeniería",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 35,
                        matriculados = 25,
                        docente = "Ing. Jorge Benitez",
                        horario = "Mar - Jue 08:00 - 10:00",
                        aula = "Lab-202",
                    ),
                    CursoEntity(
                        nombre = "Contabilidad Financiera",
                        facultad = "Negocios",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 40,
                        matriculados = 30,
                        docente = "Lic. Sofia Morales",
                        horario = "Lun - Mie 08:00 - 10:00",
                        aula = "Aula-105",
                    ),
                    CursoEntity(
                        nombre = "Marketing Digital",
                        facultad = "Negocios",
                        ciclo = "2026-I",
                        modalidad = "Virtual",
                        cupoMaximo = 50,
                        matriculados = 45,
                        docente = "Mg. Daniel Ruiz",
                        horario = "Mar - Jue 19:00 - 21:00",
                        aula = "Virtual-04",
                    ),
                    CursoEntity(
                        nombre = "Gestión de Proyectos",
                        facultad = "Negocios",
                        ciclo = "2026-II",
                        modalidad = "Híbrido",
                        cupoMaximo = 35,
                        matriculados = 28,
                        docente = "Ing. Patricia Flores",
                        horario = "Mie - Vie 16:00 - 18:00",
                        aula = "Aula-208",
                    ),
                    CursoEntity(
                        nombre = "Finanzas Corporativas",
                        facultad = "Negocios",
                        ciclo = "2026-II",
                        modalidad = "Presencial",
                        cupoMaximo = 30,
                        matriculados = 20,
                        docente = "Dr. Alberto Castro",
                        horario = "Lun - Mie 14:00 - 16:00",
                        aula = "Aula-110",
                    ),
                    CursoEntity(
                        nombre = "Administración de Empresas",
                        facultad = "Negocios",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 45,
                        matriculados = 40,
                        docente = "Lic. Elena Gutierrez",
                        horario = "Mar - Jue 10:00 - 12:00",
                        aula = "Aula-102",
                    ),
                    CursoEntity(
                        nombre = "Comportamiento Organizacional",
                        facultad = "Negocios",
                        ciclo = "2026-II",
                        modalidad = "Virtual",
                        cupoMaximo = 40,
                        matriculados = 25,
                        docente = "Mg. Javier Rios",
                        horario = "Vie 18:00 - 22:00",
                        aula = "Virtual-05",
                    ),
                    CursoEntity(
                        nombre = "Comercio Internacional",
                        facultad = "Negocios",
                        ciclo = "2026-I",
                        modalidad = "Híbrido",
                        cupoMaximo = 30,
                        matriculados = 29,
                        docente = "Lic. Beatriz Medina",
                        horario = "Lun - Mie 11:00 - 13:00",
                        aula = "Aula-204",
                    ),
                    CursoEntity(
                        nombre = "Anatomía Humana",
                        facultad = "Ciencias de la Salud",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 25,
                        matriculados = 25, // 0 vacantes
                        docente = "Dr. Gonzalo Vargas",
                        horario = "Lun - Mie - Vie 07:00 - 09:00",
                        aula = "Lab-Med01",
                    ),
                    CursoEntity(
                        nombre = "Fisiología General",
                        facultad = "Ciencias de la Salud",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 25,
                        matriculados = 18,
                        docente = "Dra. Rosa Chavez",
                        horario = "Mar - Jue 08:00 - 11:00",
                        aula = "Lab-Med02",
                    ),
                    CursoEntity(
                        nombre = "Farmacología Clínica",
                        facultad = "Ciencias de la Salud",
                        ciclo = "2026-II",
                        modalidad = "Híbrido",
                        cupoMaximo = 30,
                        matriculados = 22,
                        docente = "Dr. Esteban Guzman",
                        horario = "Mie - Vie 10:00 - 12:00",
                        aula = "Aula-Med05",
                    ),
                    CursoEntity(
                        nombre = "Bioquímica Médica",
                        facultad = "Ciencias de la Salud",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 20,
                        matriculados = 15,
                        docente = "Dra. Claudia Aguilar",
                        horario = "Lun - Jue 14:00 - 16:00",
                        aula = "Lab-Bioq",
                    ),
                    CursoEntity(
                        nombre = "Salud Pública",
                        facultad = "Ciencias de la Salud",
                        ciclo = "2026-II",
                        modalidad = "Virtual",
                        cupoMaximo = 40,
                        matriculados = 30,
                        docente = "Dr. Mario Herrera",
                        horario = "Sab 08:00 - 12:00",
                        aula = "Virtual-06",
                    ),
                    CursoEntity(
                        nombre = "Derecho Constitucional",
                        facultad = "Derecho",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 50,
                        matriculados = 42,
                        docente = "Dr. Francisco Salazar",
                        horario = "Lun - Mie 08:00 - 10:00",
                        aula = "Auditorio A",
                    ),
                    CursoEntity(
                        nombre = "Derecho Penal",
                        facultad = "Derecho",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 45,
                        matriculados = 40,
                        docente = "Dr. Ricardo Fernandez",
                        horario = "Mar - Jue 10:00 - 12:00",
                        aula = "Aula-Der01",
                    ),
                    CursoEntity(
                        nombre = "Derecho Civil",
                        facultad = "Derecho",
                        ciclo = "2026-II",
                        modalidad = "Presencial",
                        cupoMaximo = 40,
                        matriculados = 35,
                        docente = "Dra. Teresa Ocampo",
                        horario = "Mie - Vie 14:00 - 16:00",
                        aula = "Aula-Der02",
                    ),
                    CursoEntity(
                        nombre = "Derecho Mercantil",
                        facultad = "Derecho",
                        ciclo = "2026-II",
                        modalidad = "Virtual",
                        cupoMaximo = 35,
                        matriculados = 20,
                        docente = "Dr. Gabriel Nunez",
                        horario = "Lun - Mie 19:00 - 21:00",
                        aula = "Virtual-07",
                    ),
                    CursoEntity(
                        nombre = "Psicología General",
                        facultad = "Humanidades",
                        ciclo = "2026-I",
                        modalidad = "Presencial",
                        cupoMaximo = 40,
                        matriculados = 30,
                        docente = "Dra. Lucia Campos",
                        horario = "Lun - Mie 10:00 - 12:00",
                        aula = "Aula-Hum01",
                    ),
                    CursoEntity(
                        nombre = "Ética Profesional",
                        facultad = "Humanidades",
                        ciclo = "2026-I",
                        modalidad = "Virtual",
                        cupoMaximo = 60,
                        matriculados = 55,
                        docente = "Mg. Hernan Paredes",
                        horario = "Jue 18:00 - 21:00",
                        aula = "Virtual-08",
                    ),
                    CursoEntity(
                        nombre = "Comunicación Efectiva",
                        facultad = "Humanidades",
                        ciclo = "2026-I",
                        modalidad = "Híbrido",
                        cupoMaximo = 50,
                        matriculados = 40,
                        docente = "Lic. Vanessa Leon",
                        horario = "Mar - Jue 08:00 - 10:00",
                        aula = "Aula-Hum02",
                    ),
                    CursoEntity(
                        nombre = "Metodología de la Investigación",
                        facultad = "Humanidades",
                        ciclo = "2026-II",
                        modalidad = "Virtual",
                        cupoMaximo = 45,
                        matriculados = 38,
                        docente = "Dr. Cesar Valdivia",
                        horario = "Vie 17:00 - 20:00",
                        aula = "Virtual-09",
                    ),
                    CursoEntity(
                        nombre = "Liderazgo y Trabajo en Equipo",
                        facultad = "Humanidades",
                        ciclo = "2026-II",
                        modalidad = "Presencial",
                        cupoMaximo = 35,
                        matriculados = 12,
                        docente = "Mg. Monica Solis",
                        horario = "Sab 14:00 - 18:00",
                        aula = "Aula-Hum03",
                    ),
                )
                cursoDao.insertAll(cursosIniciales)
            }
        }
    }
}
