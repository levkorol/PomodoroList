package ru.harlion.pomodorolist.ui.profile.settings.sounds


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentRingtonsBinding
import ru.harlion.pomodorolist.utils.Player
import ru.harlion.pomodorolist.utils.Prefs


class RingtonesFragment :
    BindingFragment<FragmentRingtonsBinding>(FragmentRingtonsBinding::inflate) {

    private lateinit var adapterSound: SoundsAdapter
    private lateinit var prefs: Prefs
    private var player: Player? = null
    private var isSound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isSound = arguments?.getBoolean("IS_SOUND", false) ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = Prefs(requireContext())
        player = Player(requireContext())


        binding.title.text = if (isSound) {
            getString(R.string.sound_focus)
        } else {
            getString(R.string.notify)
        }

        initRecyclerSound(isSound)

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun initRecyclerSound(isSound: Boolean) {
        adapterSound = SoundsAdapter(prefs, player, isSound)
        binding.ringtonesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterSound
        }
        adapterSound.items = if (isSound) SOUNDS else SIGNALS
    }


    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
        player?.stopSound()
        player = null
    }

    companion object {
        fun newInstance(isSound: Boolean) = RingtonesFragment().apply {
            arguments = Bundle().apply {
                putBoolean("IS_SOUND", isSound)
            }
        }

        val SIGNALS = listOf(
            SoundOrSignal(R.string.no, 0),
            SoundOrSignal(R.string.signal_1, R.raw.signal_1),
            SoundOrSignal(R.string.signal_2, R.raw.signal_2),
            SoundOrSignal(R.string.signal_3, R.raw.signal_3),
            SoundOrSignal(R.string.signal_4, R.raw.signal_4),
            SoundOrSignal(R.string.signal_5, R.raw.signal_5),
            SoundOrSignal(R.string.signal_6, R.raw.signal_6),
        )

        val SOUNDS = listOf(
            SoundOrSignal(R.string.no, 0),
            SoundOrSignal(R.string.tick_sound, R.raw.alarm_clock),
            SoundOrSignal(R.string.sound_bird, R.raw.sound_birds),
            SoundOrSignal(R.string.sound_fire, R.raw.sound_fire),
            SoundOrSignal(R.string.sound_water, R.raw.sound_water),
        )
    }
}