package cl.tuapp.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cl.tuapp.dogapp.model.Cita
import cl.tuapp.dogapp.repository.CitaRepository
import kotlinx.coroutines.launch

class NuevaCitaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CitaRepository(application)

    private val _razas = MutableLiveData<List<String>>()
    val razas: LiveData<List<String>> = _razas

    private val _citaGuardada = MutableLiveData<Boolean>()
    val citaGuardada: LiveData<Boolean> = _citaGuardada

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    init {
        cargarRazas()
    }

    private fun cargarRazas() {
        viewModelScope.launch {
            _razas.value = repository.obtenerRazas()
        }
    }

    fun guardarNuevaCita(
        nombreMascota: String,
        raza: String,
        nombrePropietario: String,
        telefono: String,
        sintoma: String
    ) {
        viewModelScope.launch {
            _cargando.value = true
            val imagenUrl = repository.obtenerImagenPorRaza(raza)
            val nuevaCita = Cita(
                nombreMascota = nombreMascota.trim(),
                raza = raza.trim(),
                nombrePropietario = nombrePropietario.trim(),
                telefono = telefono.trim(),
                sintoma = sintoma,
                urlImagen = imagenUrl
            )
            repository.insertar(nuevaCita)
            _cargando.value = false
            _citaGuardada.value = true
        }
    }
}