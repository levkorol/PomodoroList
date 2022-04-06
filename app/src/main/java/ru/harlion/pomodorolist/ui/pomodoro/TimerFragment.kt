package ru.harlion.pomodorolist.ui.pomodoro


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.viewModels
import ru.harlion.pomodorolist.AppActivity
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

        if (prefs.taskId > 0) {
            viewModel.getTaskById(prefs.taskId)
            binding.taskCv.visibility = View.VISIBLE
            binding.infoTasks.visibility = View.VISIBLE
        }

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
        binding.closeTask.setOnClickListener {
            prefs.taskId = 0L
            binding.taskCv.visibility = View.GONE
            binding.infoTasks.visibility = View.GONE
        }

        binding.doneTask.setOnClickListener {
            viewModel.updateTaskDone()
            prefs.taskId = 0L
            binding.taskCv.visibility = View.GONE
            binding.infoTasks.visibility = View.GONE
        }
    }

    private fun initTimerAndClick() {

        binding.startFocusBtn.setOnClickListener {
            timerService?.startTimer( true)
        }

        binding.pauseBtn.setOnClickListener {
            timerService?.pause()
        }

        binding.resumeFocusBtn.setOnClickListener {
            timerService?.resume(prefs)
        }

        binding.skipBreakBtn.setOnClickListener {
             timerService?.apply {
                 stopTimer()
                 startTimer( true)
             }
        }

        binding.startBreakBtn.setOnClickListener {
             timerService?.startTimer( false)
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
            onStateChange = null
            onFinish = null
        }
        timerService = null
        requireContext().unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        timerService = (service as TimerService.TimerBinder).service.apply {

            visibleButton(this.timerState)

            bind()

            onTick = { millisUntilFinished, time ->
                bind(millisUntilFinished, time)
            }

            onFinish = {
                bind()
            }

            onStateChange = {
                visibleButton(it)
            }
        }
    }

    private fun TimerService.bind() {
        val timeFocus = prefTimeFocus * 60000
        val timeBreak = prefTimeBreak * 60000

        when (timerState) {
            TimerState.WAIT_FOCUS -> bind(timeFocus, timeFocus)
            TimerState.WAIT_BREAK -> bind(timeBreak, timeBreak)
            TimerState.FOCUS, TimerState.PAUSE_FOCUS -> bind(millisLeft, timeFocus)
            TimerState.BREAK -> bind(millisLeft, timeBreak)
        }
    }

    private fun TimerService.bind(
        millisUntilFinished: Long,
        timeFocus: Long
    ) {
        binding.timerCount.text = formatTimeMins(millisUntilFinished, resources)
        binding.progressBar.maximum = timeFocus.toFloat()

        binding.progressBar.progress =
            timeFocus - min(millisUntilFinished, timeFocus).toFloat()
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