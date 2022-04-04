package ru.harlion.pomodorolist.ui.profile.settings.sounds

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.pomodorolist.base.BindingHolder
import ru.harlion.pomodorolist.databinding.ItemSoundBinding
import ru.harlion.pomodorolist.utils.Player
import ru.harlion.pomodorolist.utils.Prefs


private typealias ItemHolderSound = BindingHolder<ItemSoundBinding>

class SoundsAdapter(
    val prefs: Prefs,
    private val player: Player?,
    private val isSound : Boolean
) :
    RecyclerView.Adapter<ItemHolderSound>() {

    var items: List<SoundOrSignal> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolderSound(ItemSoundBinding::inflate, parent).apply {}

    override fun onBindViewHolder(holder: ItemHolderSound, position: Int) {
        holder.binding.apply {

            //todo

//               if (!items[position].isChecked) {
//                   items.forEach{
//                     it.isChecked = false
//                   }
//                   notifyItemChanged(position)
//               }
          //  val prefRawId = if (isSound) prefs.songRawId else prefs.signalRawId //todo
            textSound.text = items[position].name

            if(isSound) {
                if (prefs.songRawId == items[position].rawId) {
                    checkSound.isChecked = true
                }
                checkSound.setOnClickListener {
                    if (!checkSound.isChecked) {
                        checkSound.isChecked = true
                        prefs.songRawId = items[position].rawId
                    } else {
                        checkSound.isChecked = false
                        prefs.songRawId = items[position].rawId
                    }
                }
            } else {
                if (prefs.signalRawId == items[position].rawId) {
                    checkSound.isChecked = true
                }
                checkSound.setOnClickListener {
                    if (!checkSound.isChecked) {
                        checkSound.isChecked = true
                        prefs.signalRawId = items[position].rawId
                    } else {
                        checkSound.isChecked = false
                        prefs.signalRawId = items[position].rawId
                    }
                }
            }

            textSound.setOnClickListener {
                player?.stopSound()
                if (items[position].rawId > 0) {
                    player?.playSoundById(items[position].rawId)
                }
            }
        }
    }

    override fun getItemCount() = items.size
}

class SoundOrSignal(
    val name: String,
    val rawId: Int
)