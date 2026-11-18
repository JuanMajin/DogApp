package cl.tuapp.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cl.tuapp.dogapp.R
import cl.tuapp.dogapp.databinding.FragmentNuevaCitaBinding
import cl.tuapp.dogapp.utils.Constants
import cl.tuapp.dogapp.viewmodel.NuevaCitaViewModel

class Fragment_nueva_cita : Fragment() {

    private var _binding: FragmentNuevaCitaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NuevaCitaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNuevaCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarSpinnerSintomas()
        configurarValidacionCampos()
        configurarObservadores()
        configurarEventos()
    }

    private fun configurarSpinnerSintomas() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Constants.LISTA_SINTOMAS
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSintomas.adapter = adapter
    }

    private fun configurarValidacionCampos() {
        val validar = {
            val mascotaOk = !binding.etNombreMascota.text.isNullOrBlank()
            val razaOk = !binding.actvRaza.text.isNullOrBlank()
            val propietarioOk = !binding.etNombrePropietario.text.isNullOrBlank()
            val telefonoOk = !binding.etTelefono.text.isNullOrBlank()

            val habilitar = mascotaOk && razaOk && propietarioOk && telefonoOk
            binding.btnGuardarCita.isEnabled = habilitar

            val colorTexto = if (habilitar) R.color.white else R.color.gray_hint
            binding.btnGuardarCita.setTextColor(
                ContextCompat.getColor(requireContext(), colorTexto)
            )
        }

        binding.etNombreMascota.addTextChangedListener { validar() }
        binding.actvRaza.addTextChangedListener { validar() }
        binding.etNombrePropietario.addTextChangedListener { validar() }
        binding.etTelefono.addTextChangedListener { validar() }
    }

    private fun configurarObservadores() {
        viewModel.razas.observe(viewLifecycleOwner) { listaRazas ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                listaRazas
            )
            binding.actvRaza.setAdapter(adapter)
        }

        viewModel.cargando.observe(viewLifecycleOwner) { estaCargando ->
            binding.btnGuardarCita.isEnabled = !estaCargando
        }

        viewModel.citaGuardada.observe(viewLifecycleOwner) { guardada ->
            if (guardada) {
                findNavController().navigateUp()
            }
        }
    }

    private fun configurarEventos() {
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGuardarCita.setOnClickListener {
            val sintomaSeleccionado = binding.spSintomas.selectedItem.toString()
            if (sintomaSeleccionado == "Síntomas") {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_selecciona_sintoma),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.guardarNuevaCita(
                nombreMascota = binding.etNombreMascota.text.toString(),
                raza = binding.actvRaza.text.toString(),
                nombrePropietario = binding.etNombrePropietario.text.toString(),
                telefono = binding.etTelefono.text.toString(),
                sintoma = sintomaSeleccionado
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}