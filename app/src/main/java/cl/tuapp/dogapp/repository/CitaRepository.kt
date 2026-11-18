package cl.tuapp.dogapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import cl.tuapp.dogapp.data.CitaDB
import cl.tuapp.dogapp.data.RetrofitClient
import cl.tuapp.dogapp.model.Cita
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/** Punto único de acceso a los datos. Las HU 3, 4 y 5 también lo usan. */
class CitaRepository(context: Context) {

    private val citaDao = CitaDB.obtenerBaseDatos(context).citaDao()

    fun obtenerCitas(): LiveData<List<Cita>> = citaDao.obtenerCitas()

    fun obtenerCita(id: Int): LiveData<Cita> = citaDao.obtenerCita(id)

    suspend fun insertar(cita: Cita) = citaDao.insertar(cita)

    suspend fun actualizar(cita: Cita) = citaDao.actualizar(cita)

    suspend fun eliminar(cita: Cita) = citaDao.eliminar(cita)

    // Métodos de ApiRest

    suspend fun obtenerRazas(): List<String> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService.getAllBreeds()
            if (response.isSuccessful) {
                val breedsMap = response.body()?.message ?: emptyMap()
                val listaRazas = mutableListOf<String>()
                for ((raza, subRazas) in breedsMap) {
                    if (subRazas.isEmpty()) {
                        listaRazas.add(raza)
                    } else {
                        listaRazas.add(raza)
                        subRazas.forEach { sub -> listaRazas.add("$sub $raza") }
                    }
                }
                listaRazas.sorted()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun obtenerImagenPorRaza(razaSeleccionada: String): String = withContext(Dispatchers.IO) {
        try {
            val partes = razaSeleccionada.trim().lowercase().split(" ")
            val endpointRaza = if (partes.size > 1) "${partes[1]}/${partes[0]}" else partes[0]

            val response = RetrofitClient.apiService.getRandomImageByBreed(endpointRaza)
            if (response.isSuccessful && !response.body()?.imageUrl.isNullOrEmpty()) {
                response.body()!!.imageUrl
            } else {
                RetrofitClient.apiService.getRandomDogImage().body()?.imageUrl.orEmpty()
            }
        } catch (e: Exception) {
            ""
        }
    }
}
