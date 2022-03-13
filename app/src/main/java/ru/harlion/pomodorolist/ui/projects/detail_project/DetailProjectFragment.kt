package ru.harlion.pomodorolist.ui.projects.detail_project

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.databinding.FragmentDetailProjectBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.dialogs.DialogCalendar
import ru.harlion.pomodorolist.ui.dialogs.DialogPriorityTask
import ru.harlion.pomodorolist.ui.projects.ListProjectsFragment
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.utils.replaceFragment


class DetailProjectFragment :
    BindingFragment<FragmentDetailProjectBinding>(FragmentDetailProjectBinding::inflate) {

    private lateinit var adapterTask: AdapterTask
    private val viewModel: DetailProjectViewModel by viewModels()
    private var projectId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        projectId = arguments?.getLong("id_project")!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveTask.setOnClickListener {
            viewModel.addTask(binding.nameTask.text.toString())
            binding.nameTask.setText("")
        }

        binding.dateTask.setOnClickListener {
            DialogCalendar().show(parentFragmentManager, null)
        }

        binding.priority.setOnClickListener {
            DialogPriorityTask().show(parentFragmentManager, null)
        }

        binding.deleteProject.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Удалить проект?")
                setPositiveButton("Да") { _, _ ->
                    viewModel.deleteProject()
                    replaceFragment(ListProjectsFragment(), false)
                }
                setNegativeButton("Нет") { _, _ ->
                }
            }.show()
        }

        viewModel.tasks.observe(viewLifecycleOwner, {
            tasksRecyclerView(it.sortedBy { task ->
                task.isDone
            })
        })

        viewModel.project.observe(viewLifecycleOwner, {
            binding.nameProject.setText(it.name)
            binding.prizeToComplete.setText(it.prize)
        })

        viewModel.getProjectById(projectId)
    }

    private fun tasksRecyclerView(tasks: List<Task>) {
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterTask = AdapterTask(viewModel::updateTask)
        binding.tasksRecyclerView.apply {
            layoutManager = llm
            adapter = adapterTask
        }

        adapterTask.items = tasks
    }

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }

    companion object {
        fun newInstance(id: Long) = DetailProjectFragment().apply {
            arguments = Bundle().apply {
                putLong("id_project", id)
            }
        }
    }
}