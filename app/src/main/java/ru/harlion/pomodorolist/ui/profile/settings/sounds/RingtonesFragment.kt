package ru.harlion.pomodorolist.ui.profile.settings.sounds


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentRingtonsBinding


class RingtonesFragment : BindingFragment<FragmentRingtonsBinding>(FragmentRingtonsBinding::inflate) {

    private lateinit var adapterSound: SoundsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerSound()
    }

    private fun initRecyclerSound() {
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterSound = SoundsAdapter {  }
        binding.ringtonesRecyclerView.apply {
            layoutManager = llm
            adapter = adapterSound
        }

        adapterSound.items = listSound()
    }

    private fun listSound() = listOf(
       Sound(1, getString(R.string.no) , null),
       Sound(2, getString(R.string.tick_sound) , R.raw.alarm_clock),
       Sound(3, getString(R.string.sound_bird) , R.raw.sound_birds),
       Sound(4, getString(R.string.sound_fire) , R.raw.sound_fire),
       Sound(5, getString(R.string.sound_water) , R.raw.sound_water),
   )

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }
}