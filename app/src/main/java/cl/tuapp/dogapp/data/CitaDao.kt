package cl.tuapp.dogapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.tuapp.dogapp.model.Cita

@Dao
interface CitaDao {

    // HU 2.0: lista del Home. Al ser LiveData, la lista se actualiza sola al guardar, editar o borrar.
    @Query("SELECT * FROM cita ORDER BY id ASC")
    fun obtenerCitas(): LiveData<List<Cita>>

    // HU 4.0 / 5.0: buscar una cita por su turno
    @Query("SELECT * FROM cita WHERE id = :id")
    fun obtenerCita(id: Int): LiveData<Cita>

    // HU 3.0
    @Insert
    suspend fun insertar(cita: Cita)

    // HU 5.0
    @Update
    suspend fun actualizar(cita: Cita)

    // HU 4.0
    @Delete
    suspend fun eliminar(cita: Cita)
}