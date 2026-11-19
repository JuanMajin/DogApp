package cl.tuapp.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cl.tuapp.dogapp.R
import cl.tuapp.dogapp.databinding.FragmentDetalleCitaBinding
import cl.tuapp.dogapp.model.Cita
import cl.tuapp.dogapp.viewmodel.CitaViewModel
import com.bumptech.glide.Glide

/**
 * HU 4.0 - Detalle de Cita (SCRUM-8)
 * Cumple con la visualización superpuesta y las acciones de edición (pendiente HU 5.0) y eliminación.
 */
class Fragment_detalle_cita : Fragment() {

    // ViewBinding para acceder fácilmente a los componentes del layout XML
    private var _binding: FragmentDetalleCitaBinding? = null
    private val binding get() = _binding!!

    // Inyección de nuestro ViewModel compartido con la base de datos
    private val citaViewModel: CitaViewModel by viewModels()

    // Variable para tener la cita activa en memoria y poder eliminarla/editarla
    private var citaActual: Cita? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Recibir el ID de la cita que se envía desde la lista del Home (HU 2.0)
        val idCita = arguments?.getInt("idCita") ?: return

        // 2. Observar la base de datos de Room en tiempo real
        citaViewModel.obtenerCita(idCita).observe(viewLifecycleOwner) { cita ->
            // IMPORTANTE (Validación): Al borrar en Room, se puede emitir un evento 'null'
            // antes de que ocurra la navegación hacia atrás. Evitamos un NullPointerException.
            if (cita != null) {
                citaActual = cita
                mostrarDatos(cita)
            }
        }

        // 3. Configurar los eventos de clic en los botones
        configurarBotones()
    }

    private fun mostrarDatos(cita: Cita) {
        // Toolbar
        binding.tvToolbarTitle.text = cita.nombreMascota

        // Tarjeta
        binding.tvTurno.text = "#${cita.id}"
        binding.tvRaza.text = cita.raza
        binding.tvSintoma.text = cita.sintoma
        binding.tvPropietario.text = "Propietario: ${cita.nombrePropietario}"
        binding.tvTelefono.text = "Teléfono: ${cita.telefono}"

        // Imagen redonda cargada mediante Glide
        // circleCrop() la vuelve completamente circular para encajar en el CardView superpuesto
        Glide.with(this)
            .load(cita.urlImagen.ifBlank { null }) // Si no hay url (ej. error en HU 3), pasamos null
            .placeholder(R.drawable.img_perro)     // Imagen por defecto del proyecto
            .circleCrop()
            .into(binding.ivMascota)
    }

    private fun configurarBotones() {
        // Flecha rosada atrás (Regresa al Home)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Botón FAB Eliminar (Rosado)
        binding.fabEliminar.setOnClickListener {
            citaActual?.let { cita ->
                // Eliminamos en Room usando MVVM
                citaViewModel.eliminarCita(cita)
                // Retornamos automáticamente al Home
                findNavController().popBackStack()
            }
        }

        // Botón FAB Editar (Azul/Gris oscuro)
        binding.fabEditar.setOnClickListener {
            // Manejo temporal para evitar el quiebre de la app (Criterio 5)
            Toast.makeText(
                requireContext(),
                "Falta integrar la HU 5.0 (Editar Cita)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberar el binding al destruir la vista (Buena práctica en Fragments)
        _binding = null
    }
}