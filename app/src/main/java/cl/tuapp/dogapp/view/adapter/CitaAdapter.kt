package cl.tuapp.dogapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cl.tuapp.dogapp.R
import cl.tuapp.dogapp.databinding.ItemCitaBinding
import cl.tuapp.dogapp.model.Cita



/**
 * HU 2.0, criterio 3: pinta cada cita (foto circular, nombre, síntoma y turno).
 * Criterio 5: al tocar un ítem avisa al fragmento con onClick.
 */
class CitaAdapter(
    private val onClick: (Cita) -> Unit
) : ListAdapter<Cita, CitaAdapter.CitaViewHolder>(CitaDiff){

    class CitaViewHolder(private val binding: ItemCitaBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(cita: Cita, onClick: (Cita) -> Unit) {
            binding.tvNombreMascota.text = cita.nombreMascota
            binding.tvSintoma.text = cita.sintoma
            binding.tvTurno.text = "#${cita.id}"

            //  La URL la guarda la HU 3.0 con la API de dog.ceo; mientras no haya, se ve el perro de la app
            Glide.with(binding.ivMascota)
                .load(cita.urlImagen.ifBlank { null })
                .placeholder(R.drawable.img_perro)
                .centerCrop()
                .into(binding.ivMascota)

            binding.cardCita.setOnClickListener { onClick(cita) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val binding = ItemCitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    // Compara listas para refrescar solo lo que cambió
    object CitaDiff : DiffUtil.ItemCallback<Cita>() {
        override fun areItemsTheSame(oldItem: Cita, newItem: Cita) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Cita, newItem: Cita) = oldItem == newItem
    }

}

