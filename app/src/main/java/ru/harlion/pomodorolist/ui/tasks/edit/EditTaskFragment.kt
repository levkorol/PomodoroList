package ru.harlion.pomodorolist.ui.tasks.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentEditTaskBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.utils.dateToString


class EditTaskFragment :
    BindingFragment<FragmentEditTaskBinding>(FragmentEditTaskBinding::inflate) {

    private val viewModel: EditTaskViewModel by viewModels()
    private var taskId = 0L
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskId = arguments?.getLong("task_id", 0) ?: 0L
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
}