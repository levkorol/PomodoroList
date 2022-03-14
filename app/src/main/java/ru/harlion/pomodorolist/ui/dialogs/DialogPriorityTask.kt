package ru.harlion.pomodorolist.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.databinding.FragmentDialogPriorityTaskBinding

class DialogPriorityTask : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_priority_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDialogPriorityTaskBinding.bind(view)

        binding.high.setOnClickListener {
            setFragmentResult("priority", Bundle().apply {
                putString("priority_task", "high")
            })
            dismiss()
        }

        binding.middle.setOnClickListener {
            setFragmentResult("priority", Bundle().apply {
                putString("priority_task", "middle")
            })
            dismiss()
        }
        binding.normal.setOnClickListener {
            setFragmentResult("priority", Bundle().apply {
                putString("priority_task", "normal")
            })
            dismiss()
        }
    }
}