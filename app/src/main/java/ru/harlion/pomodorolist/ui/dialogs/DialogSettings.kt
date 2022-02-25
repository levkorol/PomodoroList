package ru.harlion.pomodorolist.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.databinding.FragmentDialogSettingsBinding

class DialogSettings : BottomSheetDialogFragment() {

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
    }
}