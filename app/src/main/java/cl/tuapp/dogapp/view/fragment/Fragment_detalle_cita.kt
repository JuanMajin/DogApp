package cl.tuapp.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
 * Muestra el detalle de la cita y permite editarla o eliminarla.
 */
class Fragment_detalle_cita : Fragment() {

    private var _binding: FragmentDetalleCitaBinding? = null
    private val binding get() = _binding!!

    private val citaViewModel: CitaViewModel by viewModels()
    private var citaActual: Cita? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idCita = arguments?.getInt("idCita") ?: return

        citaViewModel.obtenerCita(idCita).observe(viewLifecycleOwner) { cita ->
            if (cita != null) {
                citaActual = cita
                mostrarDatos(cita)
            }
        }

        configurarBotones()
    }

    private fun mostrarDatos(cita: Cita) {
        binding.tvToolbarTitle.text = cita.nombreMascota
        binding.tvTurno.text = "#${cita.id}"
        binding.tvRaza.text = cita.raza
        binding.tvSintoma.text = cita.sintoma
        binding.tvPropietario.text = "Propietario: ${cita.nombrePropietario}"
        binding.tvTelefono.text = "Teléfono: ${cita.telefono}"

        Glide.with(this)
            .load(cita.urlImagen.ifBlank { null })
            .placeholder(R.drawable.img_perro)
            .circleCrop()
            .into(binding.ivMascota)
    }

    private fun configurarBotones() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fabEliminar.setOnClickListener {
            citaActual?.let { cita ->
                citaViewModel.eliminarCita(cita)
                findNavController().popBackStack()
            }
        }

        binding.fabEditar.setOnClickListener {
            citaActual?.let { cita ->
                val argumentos = Bundle().apply {
                    putInt("idCita", cita.id)
                }

                findNavController().navigate(
                    R.id.action_fragment_detalle_cita_to_fragment_editar_cita,
                    argumentos
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}