package cl.tuapp.dogapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import cl.tuapp.dogapp.R
import cl.tuapp.dogapp.databinding.FragmentLoginBinding

/**
 * HU 1.0 - Ventana Login (SCRUM-5)
 * Criterio 5: al tocar la huella se abre la ventana "Autenticación con Biometría".
 * Criterio 6: huella correcta -> Home (HU 2.0); incorrecta -> error del dispositivo, no entra.
 */
class Fragment_login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores()
        configurarBiometria()
    }

    // Eventos de la vista
    private fun controladores(){
        binding.lottieHuella.setOnClickListener {
            if (puedeUsarHuella()){
                biometricPrompt.authenticate(promptInfo)
            }
        }
    }

    // Criterio 5: ventana emergente con título, subtítulo y botón "Cancelar"
    private fun configurarBiometria() {
        val executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = BiometricPrompt(this, executor, object: BiometricPrompt.AuthenticationCallback(){

            //Criterio 6 huella correcta
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                irAlHome()
            }
            //huella incorrecta

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
            //demasiados intentos

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                val usuarioCancelo = errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                        errorCode == BiometricPrompt.ERROR_USER_CANCELED
                if (!usuarioCancelo){
                    Toast.makeText(requireContext(),errString, Toast.LENGTH_SHORT).show()
                }
            }

        })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometria_titulo))
            .setTitle(getString(R.string.biometria_subtitulo))
            .setNegativeButtonText(getString(R.string.biometria_cancelar))
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
    }
    // Revisa que el celular tenga sensor y al menos una huella registrada
    private fun puedeUsarHuella(): Boolean {
        val mensaje = when (BiometricManager.from(requireContext()).canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> return true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> R.string.biometria_sin_sensor
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> R.string.biometria_no_disponible
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> R.string.biometria_sin_huellas
            else -> R.string.biometria_no_disponible
        }
        Toast.makeText(requireContext(), getString(mensaje), Toast.LENGTH_LONG).show()
        return false

        }

    // Navega al Home quitando el login de la pila (ver nav_graph: popUpTo inclusive),
    // así el botón atrás del Home cierra la app en vez de volver al login (HU 2.0, criterio 1).
    private fun irAlHome() {
        findNavController().navigate(R.id.action_fragment_login_to_fragment_home)
    }

}


