package ru.harlion.pomodorolist.ui.profile.settings.sounds


import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentRingtonsBinding


class RingtonesFragment : BindingFragment<FragmentRingtonsBinding>(FragmentRingtonsBinding::inflate) {


   private fun listSound() = listOf(
       Sound(1, getString(R.string.no) , null),
       Sound(2, getString(R.string.tick_sound) , R.raw.alarm_clock),
       Sound(3, getString(R.string.sound_bird) , R.raw.sound_birds),
       Sound(4, getString(R.string.sound_fire) , R.raw.sound_fire),
       Sound(5, getString(R.string.sound_water) , R.raw.sound_water),
   )
}