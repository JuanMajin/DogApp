package cl.tuapp.dogapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.tuapp.dogapp.model.Cita

/** Base de datos local SQLite con Room (HU 2.0, criterio 6). */
@Database(entities = [Cita::class], version = 1, exportSchema = false)
abstract class CitaDB : RoomDatabase() {

    abstract fun citaDao(): CitaDao

    companion object {
        @Volatile
        private var INSTANCIA: CitaDB? = null

        // Una sola instancia para toda la app
        fun obtenerBaseDatos(context: Context): CitaDB {
            return INSTANCIA ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CitaDB::class.java,
                    "dogapp_db"
                ).build().also { INSTANCIA = it }
            }
        }
    }
}
