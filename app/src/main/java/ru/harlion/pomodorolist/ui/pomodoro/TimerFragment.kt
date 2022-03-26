package ru.harlion.pomodorolist.ui.pomodoro


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentTimerBinding
import ru.harlion.pomodorolist.ui.profile.settings.SettingsTimerFragment
import ru.harlion.pomodorolist.ui.profile.statistic.StatisticFragment
import ru.harlion.pomodorolist.utils.*
import kotlin.math.min


class TimerFragment : BindingFragment<FragmentTimerBinding>(FragmentTimerBinding::inflate),
    ServiceConnection {

    private val viewModel: TimerViewModel by viewModels()
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

        binding.startFocusBtn.setOnClickListener {
            timerService?.startTimer(-1)
        }

        binding.pauseBtn.setOnClickListener {
            timerService?.pause()
        }

        binding.resumeFocusBtn.setOnClickListener {
            timerService?.resume(prefs)
        }

        binding.stopFocusBtn.setOnClickListener {
            timerService?.stopTimer()
            binding.timerCount.text = formatTimeMins(prefTimeFocus * 60000, resources)
            binding.progressBar.progress = 0F
        }

        viewModel.task.observe(viewLifecycleOwner, {
            binding.textTask.text = it?.name
        })
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
            onStateChange = null
        }
        timerService = null
        requireContext().unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        timerService = (service as TimerService.TimerBinder).service.apply {

            if (taskId > 0) {
                viewModel.getTaskById(taskId)
                binding.taskCv.visibility = View.VISIBLE
                binding.infoTasks.visibility = View.VISIBLE
            }

            visibleButton(this.timerState)
            if (this.timerState == TimerState.WAIT_FOCUS) {
                binding.timerCount.text = formatTimeMins(prefTimeFocus * 60000, resources)
                //todo сделать отображение минут больше 60ти
            }
            if (this.timerState == TimerState.WAIT_BREAK) {
                binding.timerCount.text = formatTimeMins(prefTimeBreak * 60000, resources)
            }
            onTick = { millisUntilFinished, timeFocus ->
                binding.timerCount.text = formatTimeMins(millisUntilFinished, resources)
                binding.progressBar.maximum = timeFocus.toFloat()

                binding.progressBar.progress =
                    timeFocus - min(millisUntilFinished, timeFocus).toFloat()
            }
            onFinish = {
                binding.timerCount.text = formatTimeMins(prefTimeFocus * 60000, resources)
                //todo btn all without break prefs
            }
            onStateChange = {
                visibleButton(it)
            }
        }
    }

    private fun visibleButton(timerState: TimerState) {
        when (timerState) {
            TimerState.WAIT_FOCUS -> {
                binding.startFocusBtn.visibility = View.VISIBLE
                binding.pauseBtn.visibility = View.GONE
                binding.pauseLinear.visibility = View.GONE
                binding.startBreakBtn.visibility = View.GONE
                binding.skipBreakBtn.visibility = View.GONE
            }
            TimerState.FOCUS -> {
                binding.startFocusBtn.visibility = View.GONE
                binding.pauseBtn.visibility = View.VISIBLE
                binding.pauseLinear.visibility = View.GONE
                binding.startBreakBtn.visibility = View.GONE
                binding.skipBreakBtn.visibility = View.GONE
            }
            TimerState.PAUSE_FOCUS -> {
                binding.startFocusBtn.visibility = View.GONE
                binding.pauseBtn.visibility = View.GONE
                binding.pauseLinear.visibility = View.VISIBLE
                binding.startBreakBtn.visibility = View.GONE
                binding.skipBreakBtn.visibility = View.GONE
            }
            TimerState.WAIT_BREAK -> {
                binding.startFocusBtn.visibility = View.GONE
                binding.pauseBtn.visibility = View.GONE
                binding.pauseLinear.visibility = View.GONE
                binding.startBreakBtn.visibility = View.VISIBLE
                binding.skipBreakBtn.visibility = View.GONE
            }
            TimerState.BREAK -> {
                binding.startFocusBtn.visibility = View.GONE
                binding.pauseBtn.visibility = View.GONE
                binding.pauseLinear.visibility = View.GONE
                binding.startBreakBtn.visibility = View.GONE
                binding.skipBreakBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {}

    override fun onResume() {
        super.onResume()
        (activity as AppActivity).updateNavigation(this)
    }
}