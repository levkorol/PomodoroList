package ru.harlion.pomodorolist.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.databinding.FragmentDialogSettingsBinding
import java.util.*

class DialogCalendar : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDialogSettingsBinding.bind(view)

//        binding.close.setOnClickListener {
//            dismiss()
//        }

        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC")

        binding.calendarView.apply {
            date = calendar.timeInMillis.let { it + TimeZone.getDefault().getOffset(it) }
            maxDate = System.currentTimeMillis() + 31536000000

            setOnDateChangeListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
            }
        }
        binding.save.setOnClickListener {
            setFragmentResult("calendarDate", Bundle().apply {
                putLong("epochMillis", calendar.timeInMillis)
            })
            dismiss()
        }
    }

}