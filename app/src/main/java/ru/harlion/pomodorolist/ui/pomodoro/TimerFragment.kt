package ru.harlion.pomodorolist.ui.pomodoro


import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentTimerBinding
import ru.harlion.pomodorolist.utils.Player
import ru.harlion.pomodorolist.utils.formatTimeMins
import kotlin.math.min


class TimerFragment : BindingFragment<FragmentTimerBinding>(FragmentTimerBinding::inflate) {

    private var shortBreakText = "05:00"
    private var longBreakText = "25:00"
    private var pomodoroTimerText = "25:00"
    private var timeOfBreak = ""
    private var timer: CountDownTimer? = null
    private lateinit var player: Player
    private var isOnPlayer: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        player = Player(requireContext())

        initClicks()
    }

    private fun initClicks() {
        binding.startBtn.setOnClickListener {
            timer = object : CountDownTimer(30000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    if (timer != null) {
                        binding.timerCount.text = formatTimeMins(millisUntilFinished, resources)

                        binding.progressBar.maximum = 30000.toFloat()
                        binding.progressBar.progress = min(millisUntilFinished, 30000).toFloat()

                        player.playSound()
                    }
                }

                override fun onFinish() {
                    binding.timerCount.text = "done!"
                    player.stopSound()
                }
            }.start()
        }

        binding.stopBtn.setOnClickListener {
            timer?.cancel()
            binding.timerCount.text = "stop"
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        timer?.cancel()
        super.onPause()
    }
}