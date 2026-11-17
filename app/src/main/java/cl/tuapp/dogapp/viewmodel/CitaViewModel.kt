package cl.tuapp.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cl.tuapp.dogapp.model.Cita
import cl.tuapp.dogapp.repository.CitaRepository
import kotlinx.coroutines.launch

class CitaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CitaRepository(application)

    // HU 2.0: el Home observa esta lista
    val listaCitas: LiveData<List<Cita>> = repository.obtenerCitas()

    fun obtenerCita(id: Int): LiveData<Cita> = repository.obtenerCita(id)

    fun insertarCita(cita: Cita) {
        viewModelScope.launch { repository.insertar(cita) }
    }

    fun actualizarCita(cita: Cita) {
        viewModelScope.launch { repository.actualizar(cita) }
    }

    fun eliminarCita(cita: Cita) {
        viewModelScope.launch { repository.eliminar(cita) }
    }
}
