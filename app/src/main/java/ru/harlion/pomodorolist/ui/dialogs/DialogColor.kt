package ru.harlion.pomodorolist.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.databinding.FragmentDialogColorBinding

class DialogColor : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDialogColorBinding.bind(view)

        binding.project1.setOnClickListener { setResultColor(R.color.project_1) }
        binding.project2.setOnClickListener { setResultColor(R.color.project_2) }
        binding.project3.setOnClickListener { setResultColor(R.color.project_3) }
        binding.project4.setOnClickListener { setResultColor(R.color.project_4) }
        binding.project5.setOnClickListener { setResultColor(R.color.project_5) }
        binding.project6.setOnClickListener { setResultColor(R.color.project_6) }
        binding.project7.setOnClickListener { setResultColor(R.color.project_7) }
        binding.project8.setOnClickListener { setResultColor(R.color.project_8) }
    }

    private fun setResultColor(value : Int) {
        setFragmentResult("color", Bundle().apply {
            putInt("colorId", value)
        })
        dismiss()
    }
}