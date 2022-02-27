package ru.harlion.pomodorolist.ui.pomodoro


import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentTimerBinding
import ru.harlion.pomodorolist.ui.profile.settings.SettingsTimerFragment
import ru.harlion.pomodorolist.ui.tasks.ListTasksFragment
import ru.harlion.pomodorolist.utils.*
import kotlin.math.min


class TimerFragment : BindingFragment<FragmentTimerBinding>(FragmentTimerBinding::inflate) {

    private lateinit var prefs: Prefs
    private  var prefTimeFocus = 0L
    private  var prefTimeBreak = 0L
    private var timer: CountDownTimer? = null
    private lateinit var player: Player
    private var isOnFocus: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = Prefs(requireContext())
        prefTimeFocus = prefs.focusTimerActiveSettings
        prefTimeBreak = prefs.breakTimerActiveSettings

        player = Player(requireContext())

        initTimerAndClick()

        initClicks()

        if(!isOnFocus) {
            binding.timerCount.text = formatTimeMins(prefTimeFocus * 60000, resources)
        //todo сделать отображение минут больше 60ти
        }
    }

    private fun initClicks() {
        binding.settingsTimer.setOnClickListener {
            replaceFragment(SettingsTimerFragment(), true)
        }

        binding.btnCalendar.setOnClickListener {
            replaceFragment(ListTasksFragment(), true)
        }
    }

    private fun initTimerAndClick() {

        val timeFocus = prefTimeFocus * 60000

        binding.startBtn.setOnClickListener {
            timer = object : CountDownTimer(timeFocus, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    if (timer != null) {
                        binding.timerCount.text = formatTimeMins(millisUntilFinished, resources)
                        isOnFocus = true
                        binding.progressBar.maximum = timeFocus.toFloat()

                        binding.progressBar.progress = min(millisUntilFinished, timeFocus).toFloat()

                        player.playSound()
                    }
                }

                override fun onFinish() {
                    binding.timerCount.text = "done!"
                    isOnFocus = false
                    player.stopSound()
                }
            }.start()
        }

        binding.stopBtn.setOnClickListener {
            timer?.cancel()
            binding.timerCount.text = "stop"
            player.stopSound()
        }
    }


    override fun onPause() {
        timer?.cancel()
        super.onPause()
    }
}