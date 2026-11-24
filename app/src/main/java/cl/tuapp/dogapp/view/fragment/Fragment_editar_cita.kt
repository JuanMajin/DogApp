package cl.tuapp.dogapp.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cl.tuapp.dogapp.R
import cl.tuapp.dogapp.databinding.FragmentEditarCitaBinding
import cl.tuapp.dogapp.model.Cita
import cl.tuapp.dogapp.viewmodel.CitaViewModel

class Fragment_editar_cita : Fragment() {

    private var _binding: FragmentEditarCitaBinding? = null
    private val binding get() = _binding!!
    private val citaViewModel: CitaViewModel by viewModels()

    private var citaActual: Cita? = null
    private var datosCargados = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idCita = arguments?.getInt("idCita") ?: -1
        if (idCita == -1) {
            Toast.makeText(requireContext(), "No se recibió la cita", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        binding.etTelefono.keyListener = DigitsKeyListener.getInstance("0123456789")

        citaViewModel.obtenerCita(idCita).observe(viewLifecycleOwner) { cita ->
            if (cita != null && !datosCargados) {
                citaActual = cita
                binding.etNombreMascota.setText(cita.nombreMascota)
                binding.etRaza.setText(cita.raza)
                binding.etPropietario.setText(cita.nombrePropietario)
                binding.etTelefono.setText(cita.telefono)
                datosCargados = true
                validarFormulario()
            }
        }

        val observadorCampos = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validarFormulario()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etNombreMascota.addTextChangedListener(observadorCampos)
        binding.etRaza.addTextChangedListener(observadorCampos)
        binding.etPropietario.addTextChangedListener(observadorCampos)
        binding.etTelefono.addTextChangedListener(observadorCampos)

        binding.btnBackEditar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnEditarCita.setOnClickListener {
            val cita = citaActual ?: return@setOnClickListener

            val nombre = binding.etNombreMascota.text.toString().trim()
            val raza = binding.etRaza.text.toString().trim()
            val propietario = binding.etPropietario.text.toString().trim()
            val telefono = binding.etTelefono.text.toString().trim()

            if (nombre.isBlank() || raza.isBlank() ||
                propietario.isBlank() || telefono.isBlank()
            ) {
                return@setOnClickListener
            }

            citaViewModel.actualizarCita(
                cita.copy(
                    nombreMascota = nombre,
                    raza = raza,
                    nombrePropietario = propietario,
                    telefono = telefono
                )
            )

            Toast.makeText(requireContext(), "Cita actualizada", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.fragment_home)
        }
    }

    private fun validarFormulario() {
        val completo =
            !binding.etNombreMascota.text.isNullOrBlank() &&
                    !binding.etRaza.text.isNullOrBlank() &&
                    !binding.etPropietario.text.isNullOrBlank() &&
                    !binding.etTelefono.text.isNullOrBlank()

        binding.btnEditarCita.isEnabled = completo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}