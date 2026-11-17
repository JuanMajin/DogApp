package cl.tuapp.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cl.tuapp.dogapp.R
import cl.tuapp.dogapp.databinding.FragmentHomeBinding
import cl.tuapp.dogapp.view.adapter.CitaAdapter
import cl.tuapp.dogapp.viewmodel.CitaViewModel



class Fragment_home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val citaViewModel: CitaViewModel by viewModels()
    private lateinit var citaAdapter: CitaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controlarBotonAtras()
        configurarLista()
        observarCitas()
        controladores()
    }

    // Criterio 1: atrás no vuelve al login, cierra la app y deja al usuario en el escritorio
    private fun controlarBotonAtras() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireActivity().finish()
        }
    }
    // Criterio 3 y 5: lista con su adapter; al tocar una cita va al detalle (HU 4.0)
    private fun configurarLista(){
        citaAdapter= CitaAdapter{ cita ->
            findNavController().navigate( R.id.action_fragment_home_to_fragment_detalle_cita,
                bundleOf("idCita" to cita.id)
            )
        }
        binding.rvCitas.adapter = citaAdapter
    }
    // Criterio 6: la lista sale de Room y se refresca sola cuando cambia la base de datos
    private fun observarCitas() {
        citaViewModel.listaCitas.observe(viewLifecycleOwner) { citas ->
            citaAdapter.submitList(citas)
        }
    }
    // Criterio 4: botón flotante a la HU 3.0 Nueva Cita
    private fun controladores() {
        binding.fabNuevaCita.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_home_to_fragment_nueva_cita)
        }
    }
}