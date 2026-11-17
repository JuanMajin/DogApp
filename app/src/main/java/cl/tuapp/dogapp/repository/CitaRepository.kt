package cl.tuapp.dogapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import cl.tuapp.dogapp.data.CitaDB
import cl.tuapp.dogapp.model.Cita


/** Punto único de acceso a los datos. Las HU 3, 4 y 5 también lo usan. */
class CitaRepository(context: Context) {

    private val citaDao = CitaDB.obtenerBaseDatos(context).citaDao()

    fun obtenerCitas(): LiveData<List<Cita>> = citaDao.obtenerCitas()

    fun obtenerCita(id: Int): LiveData<Cita> = citaDao.obtenerCita(id)

    suspend fun insertar(cita: Cita) = citaDao.insertar(cita)

    suspend fun actualizar(cita: Cita) = citaDao.actualizar(cita)

    suspend fun eliminar(cita: Cita) = citaDao.eliminar(cita)
}
