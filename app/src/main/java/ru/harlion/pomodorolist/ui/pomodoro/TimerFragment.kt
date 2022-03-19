package ru.harlion.pomodorolist.ui.pomodoro


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentTimerBinding
import ru.harlion.pomodorolist.ui.profile.settings.SettingsTimerFragment
import ru.harlion.pomodorolist.ui.profile.statistic.StatisticFragment
import ru.harlion.pomodorolist.utils.*
import kotlin.math.min


class TimerFragment : BindingFragment<FragmentTimerBinding>(FragmentTimerBinding::inflate),
    ServiceConnection {

    private lateinit var prefs: Prefs
    private var prefTimeFocus = 0L
    private var prefTimeBreak = 0L
    private var timerService: TimerService? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = Prefs(requireContext())
        prefTimeFocus = prefs.focusTimerActiveSettings
        prefTimeBreak = prefs.breakTimerActiveSettings

        initTimerAndClick()

        initClicks()

    }

    private fun initClicks() {
        binding.settingsTimer.setOnClickListener {
            replaceFragment(SettingsTimerFragment(), true)
        }

        binding.btnStatistic.setOnClickListener {
            replaceFragment(StatisticFragment(), true)
        }
    }

    private fun initTimerAndClick() {

        val timeFocus = prefTimeFocus * 60000

        binding.startBtn.setOnClickListener {
           timerService?.startTimer(timeFocus)
        }

        binding.stopBtn.setOnClickListener {
            timerService?.stopTimer()
            binding.timerCount.text = "stop"
        }
    }


    override fun onStart() {
        super.onStart()
        requireContext().bindService(
            Intent(requireContext(), TimerService::class.java),
            this,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        timerService?.apply {
            onTick = null
            onFinish = null
        }
        timerService = null
        requireContext().unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        timerService = (service as TimerService.TimerBinder).service.apply {
            if (!isFocusTime) {
                binding.timerCount.text = formatTimeMins(prefTimeFocus * 60000, resources)
                //todo сделать отображение минут больше 60ти
            }
            onTick = { millisUntilFinished, timeFocus ->
                binding.timerCount.text = formatTimeMins(millisUntilFinished, resources)
                binding.progressBar.maximum = timeFocus.toFloat()

                binding.progressBar.progress =
                    timeFocus - min(millisUntilFinished, timeFocus).toFloat()
            }
            onFinish = {
                binding.timerCount.text = "done!"
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {}

    override fun onResume() {
        super.onResume()
        (activity as AppActivity).updateNavigation(this)
    }
}