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
    private val isSound: Boolean
) :
    RecyclerView.Adapter<ItemHolderSound>() {

    var items: List<SoundOrSignal> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var checkedPosition: Int
        set(value) {
            val valueOld = checkedPosition
            if(value != valueOld) {
                if (isSound) {
                    prefs.songRawId = value
                } else {
                    prefs.signalRawId = value
                }
                notifyItemChanged(value)
                notifyItemChanged(valueOld)
            }
        }
        get() {
            return if (isSound) prefs.songRawId else prefs.signalRawId
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolderSound(ItemSoundBinding::inflate, parent).apply {}

    override fun onBindViewHolder(holder: ItemHolderSound, position: Int) {
        holder.binding.apply {

            textSound.setText(items[position].name)

            checkSound.isChecked = position == checkedPosition

            checkSound.setOnClickListener {
               checkedPosition = position
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
    val name: Int,
    val rawId: Int
)