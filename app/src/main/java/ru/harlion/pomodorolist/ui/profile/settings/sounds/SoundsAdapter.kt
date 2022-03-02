package ru.harlion.pomodorolist.ui.profile.settings.sounds

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.pomodorolist.base.BindingHolder
import ru.harlion.pomodorolist.databinding.ItemSoundBinding


private typealias ItemHolderSound = BindingHolder<ItemSoundBinding>

class SoundsAdapter(private val clickDone: (Long) -> Unit) :
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