package ru.harlion.pomodorolist.ui.tasks.edit

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentEditTaskBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.dialogs.AlertDialogBase
import ru.harlion.pomodorolist.ui.dialogs.DialogCalendar
import ru.harlion.pomodorolist.ui.dialogs.DialogPriorityTask
import ru.harlion.pomodorolist.ui.projects.lists_projects.ListProjectsFragment
import ru.harlion.pomodorolist.utils.dateToString
import ru.harlion.pomodorolist.utils.replaceFragment


class EditTaskFragment :
    BindingFragment<FragmentEditTaskBinding>(FragmentEditTaskBinding::inflate) {

    private val viewModel: EditTaskViewModel by viewModels()
    private var taskId = 0L
    private lateinit var task: Task
    private var date = 0L
    private var priorityTask = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskId = arguments?.getLong("task_id", 0) ?: 0L

        setFragmentResultListener("priority") { _, bundle ->
            priorityTask = bundle.getString("priority_task") ?: ""
            setLabelPriorityTask()
        }

        setFragmentResultListener("calendarDate") { _, bundle ->
            date = bundle.getLong("epochMillis")
            binding.taskDate.text = dateToString(date)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()

        viewModel.getTaskById(taskId)

        viewModel.task.observe(viewLifecycleOwner, {
            if (it != null) {
                task = it
                binding.nameTask.setText(it.name)
                binding.taskDate.text = dateToString(it.date)
                binding.taskPriority
            }
        })
    }

    private fun initClicks() {
        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.save.setOnClickListener {
            viewModel.updateTask(task)
            parentFragmentManager.popBackStack()
        }

        binding.deleteTask.setOnClickListener {
            AlertDialogBase(requireContext()).apply {
                setTitle(getString(R.string.delete_task))
                setPositiveButton(getString(R.string.yes)) {
                    viewModel.deleteTask()
                    parentFragmentManager.popBackStack()
                }
                setNegativeButton(getString(R.string.no)) {}
                show()
            }
        }

        binding.taskDate.setOnClickListener {
            DialogCalendar().show(parentFragmentManager, null)
        }

        binding.taskPriority.setOnClickListener {
            DialogPriorityTask().show(parentFragmentManager, null)
        }

    }

    companion object {
        fun newInstance(id: Long) = EditTaskFragment().apply {
            arguments = Bundle().apply {
                putLong("task_id", id)
            }
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

    private fun setLabelPriorityTask() {
        when (priorityTask) {
            "middle" -> TextViewCompat.setCompoundDrawableTintList(
                binding.nameTask,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.nameTask.context,
                        R.color.priority_green
                    )
                )
            )
            "high" ->TextViewCompat.setCompoundDrawableTintList(
                binding.nameTask,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.nameTask.context,
                        R.color.priority_red
                    )
                )
            )
            else -> TextViewCompat.setCompoundDrawableTintList(
                binding.nameTask,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.nameTask.context,
                        R.color.priority_gray
                    )
                )
            )
        }
    }
}

//"high" -> priority.setImageDrawable(
//ContextCompat.getDrawable(
//this.priority.context,
//R.drawable.ic_label_red
//)
//)
