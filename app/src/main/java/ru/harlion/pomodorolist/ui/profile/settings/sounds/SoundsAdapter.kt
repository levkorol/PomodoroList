package ru.harlion.pomodorolist.ui.profile.settings.sounds

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.pomodorolist.base.BindingHolder
import ru.harlion.pomodorolist.databinding.ItemSoundBinding
import ru.harlion.pomodorolist.utils.Prefs


private typealias ItemHolderSound = BindingHolder<ItemSoundBinding>

class SoundsAdapter(val prefs: Prefs, private val clickDone: (Long) -> Unit ) :
RecyclerView.Adapter<ItemHolderSound>(){

    var items: List <Sound> = listOf()
    set (value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolderSound(ItemSoundBinding::inflate, parent).apply {

    }

    override fun onBindViewHolder(holder: ItemHolderSound, position: Int) {
       holder.binding.apply {
           textSound.text = items[position].name
           textSound.isChecked = items[position].isChecked
           textSound.setOnClickListener {

               if (!textSound.isChecked) {
                   textSound.isChecked = true
                   prefs.song = items[position].rawId ?: 0
               } else {
                   textSound.isChecked = false
                   prefs.song = items[position].rawId ?: 0
               }
           }
       }
    }

    override fun getItemCount() = items.size
}

class Sound(
    val id : Long,
    val name: String,
    val rawId: Int?,
    val isChecked : Boolean = false
)