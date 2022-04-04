package ru.harlion.pomodorolist.ui.profile.settings


import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentSettingsTimerBinding
import ru.harlion.pomodorolist.ui.profile.settings.sounds.RingtonesFragment
import ru.harlion.pomodorolist.utils.Prefs
import ru.harlion.pomodorolist.utils.replaceFragment


class SettingsTimerFragment : BindingFragment<FragmentSettingsTimerBinding>(FragmentSettingsTimerBinding::inflate) {

    private var onSeekBarChange: ((Int) -> Unit)? = null
    private var timeFocus: Long? = 0
    private var timeBreak: Long? = 0
    private lateinit var prefs: Prefs

    private var textSeekBarFocusTime: CharSequence
        get() = binding.timerCountWork.text
        private set(value) {
            binding.timerCountWork.text = value
        }

    private var progressSeekBarFocusTime: Int
        get() = binding.minFocus.progress
        set(value) {
            binding.minFocus.progress = value
        }

    private var textSeekBarBreakTime: CharSequence
        get() = binding.timerCountBreak.text
        private set(value) {
            binding.timerCountBreak.text = value
        }

    private var progressSeekBarBreakTime: Int
        get() = binding.minBreak.progress
        set(value) {
            binding.minBreak.progress = value
        }

    private fun setProgressBarTimeFocus(progress: Int) {
        val seekBar = binding.minFocus
        seekBar.progress = progress
        seekBar.onProgressChange { progressChange ->
            textSeekBarFocusTime = progressChange.toString()
            onSeekBarChange?.invoke(progressChange)

            prefs.focusTimerActiveSettings = progressChange.toLong()
        }
        textSeekBarFocusTime = "$progress"
    }

    private fun setProgressBarBreakFocus(progress: Int) {
        val seekBar = binding.minBreak
        seekBar.progress = progress
        seekBar.onProgressChange { progressChange ->
            textSeekBarBreakTime = progressChange.toString()
            onSeekBarChange?.invoke(progressChange)

            prefs.breakTimerActiveSettings = progressChange.toLong()
        }
        textSeekBarBreakTime = "$progress"
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs(requireContext())

        binding.sound.setOnClickListener {
            replaceFragment(RingtonesFragment.newInstance(true), true)
        }

        binding.signal.setOnClickListener {
            replaceFragment(RingtonesFragment.newInstance(false), true)
        }

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.swichBreakAuto.isChecked = prefs.isAutoBreakTimer
        binding.swichBreakAuto.setOnClickListener {
            if ( !prefs.isAutoBreakTimer) {
                binding.swichBreakAuto.isChecked = true
                prefs.isAutoBreakTimer = true
            } else {
                binding.swichBreakAuto.isChecked = false
                prefs.isAutoBreakTimer = false
            }
        }

        binding.swichFocusAuto.isChecked = prefs.isAutoFocusTimer
        binding.swichFocusAuto.setOnClickListener {
            if ( !prefs.isAutoFocusTimer) {
                binding.swichFocusAuto.isChecked = true
                prefs.isAutoFocusTimer = true
            } else {
                binding.swichFocusAuto.isChecked = false
                prefs.isAutoFocusTimer = false
            }
        }

        progressSeekBarFocusTime =
            if(prefs.focusTimerActiveSettings < 0) timeFocus!!.toInt() else prefs.focusTimerActiveSettings.toInt()
        setProgressBarTimeFocus(progressSeekBarFocusTime)

        progressSeekBarBreakTime =
            if (prefs.breakTimerActiveSettings < 0) timeBreak!!.toInt() else prefs.breakTimerActiveSettings.toInt()
        setProgressBarBreakFocus(progressSeekBarBreakTime)
    }

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }
}


private inline fun SeekBar.onProgressChange(crossinline onChange: (Int) -> Unit) {
    setOnSeekBarChangeListener(object : SimpleSeekBarListener() {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onChange(progress)
        }
    })
}

private open class SimpleSeekBarListener : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}