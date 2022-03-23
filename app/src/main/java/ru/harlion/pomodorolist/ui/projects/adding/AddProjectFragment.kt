package ru.harlion.pomodorolist.ui.projects.adding


import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.base.onEvent
import ru.harlion.pomodorolist.databinding.FragmentAddTaskBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.dialogs.DialogCalendar
import ru.harlion.pomodorolist.ui.dialogs.DialogColor
import ru.harlion.pomodorolist.ui.projects.detail_project.DetailProjectFragment
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.utils.dateToString
import ru.harlion.pomodorolist.utils.replaceFragment

class AddProjectFragment :
    BindingFragment<FragmentAddTaskBinding>(FragmentAddTaskBinding::inflate) {

    private val viewModel: AddProjectViewModel by viewModels()
    private var colorId = 0
    private var date = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("calendarDate") { _, bundle ->
            date = bundle.getLong("epochMillis")
            binding.dateDeadline.text = dateToString(date)
        }

        setFragmentResultListener("color") { _, bundle ->
            colorId = bundle.getInt("colorId")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val color = ContextCompat.getColor(requireContext(), colorId)
                val colorList = ColorStateList.valueOf(color)
                TextViewCompat.setCompoundDrawableTintList(binding.color, colorList)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.createdProjectId.onEvent(viewLifecycleOwner, {
            parentFragmentManager.popBackStack()
            replaceFragment(DetailProjectFragment.newInstance(it), true)
        })

        binding.color.setOnClickListener {
            DialogColor().show(parentFragmentManager, null)
        }

        binding.dateDeadline.setOnClickListener {
            DialogCalendar().show(parentFragmentManager, null)
        }

        binding.save.setOnClickListener {
            viewModel.addProject(
                name = binding.name.text.toString(),
                tasks = listOf(),
                prize = binding.prize.text.toString(),
                deadline = date,
                color = colorId
            )
        }

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
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