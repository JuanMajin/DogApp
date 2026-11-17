package cl.tuapp.dogapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey



/**
 * Tabla "cita" de Room. El id autogenerado es el TURNO que se muestra en el Home (HU 2.0, criterio 3).
 * Los demás campos los llena la HU 3.0 (Nueva Cita) y los muestra la HU 4.0 (Detalle).
 */
@Entity(tableName = "cita")
data class Cita (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreMascota: String,
    val raza: String,
    val nombrePropietario: String,
    val telefono: String,
    val sintoma: String,
    val urlImagen: String = ""
)