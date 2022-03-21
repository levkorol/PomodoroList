package ru.harlion.pomodorolist.ui.projects.detail_project

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentDetailProjectBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.dialogs.AlertDialogBase
import ru.harlion.pomodorolist.ui.dialogs.DialogCalendar
import ru.harlion.pomodorolist.ui.dialogs.DialogPriorityTask
import ru.harlion.pomodorolist.ui.projects.lists_projects.ListProjectsFragment
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.utils.replaceFragment


class DetailProjectFragment :
    BindingFragment<FragmentDetailProjectBinding>(FragmentDetailProjectBinding::inflate) {

    private lateinit var adapterTask: AdapterTask
    private val viewModel: DetailProjectViewModel by viewModels()
    private var projectId = 0L
    private var priorityTask: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        projectId = arguments?.getLong("id_project")!!

        setFragmentResultListener("priority") { _, bundle ->
            priorityTask = bundle.getString("priority_task") ?: ""
            setLabelPriorityTask()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()

        viewModel.tasks.observe(viewLifecycleOwner, {
            tasksRecyclerView(it.sortedBy { task ->
                task.isDone
            })
        })

        viewModel.project.observe(viewLifecycleOwner, {
            binding.nameProject.text = it.name
            if (it.prize.isNotBlank()) {
                binding.prizeToComplete.text = it.prize
                binding.prizeToComplete.visibility = View.VISIBLE
            } else {
                binding.prizeToComplete.visibility = View.GONE
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (it.color > 0) {
                    val color = ContextCompat.getColor(requireContext(), it.color)
                    val colorList = ColorStateList.valueOf(color)
                    TextViewCompat.setCompoundDrawableTintList(binding.nameProject, colorList)
                }
            }
        })

        viewModel.getProjectById(projectId)
    }

    private fun initClicks() {
        binding.saveTask.setOnClickListener {
            viewModel.addTask(
                binding.nameTask.text.toString(),
                priorityTask
            )
            binding.nameTask.setText("")
        }

        binding.dateTask.setOnClickListener {
            DialogCalendar().show(parentFragmentManager, null)
        }

        binding.priority.setOnClickListener {
            DialogPriorityTask().show(parentFragmentManager, null)
        }

        binding.deleteProject.setOnClickListener {
            AlertDialogBase(requireContext()).apply {
                setTitle(getString(R.string.delete_project))
                setPositiveButton(getString(R.string.yes)) {
                    viewModel.deleteProject()
                    replaceFragment(ListProjectsFragment(), false) }
                setNegativeButton(getString(R.string.no)) {}
                show()
            }
        }

        binding.nameProject.setOnClickListener {
            AlertDialogBase(requireContext()).apply {
                val text = setEditText("2", "")
                setPositiveButton(getString(R.string.yes)) {
                    viewModel.updateProject(text)
                    replaceFragment(ListProjectsFragment(), false) }
                setNegativeButton(getString(R.string.no)) {}
                show()
            }
        }

        binding.archiveProject.setOnClickListener {
//            viewModel.updateProject()
        }
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

    private fun setLabelPriorityTask() {
        when (priorityTask) {
            "normal" -> binding.priority.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_label
                )
            )
            "middle" -> binding.priority.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_label_green
                )
            )
            "high" -> binding.priority.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_label_red
                )
            )
            else -> binding.priority.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_label
                )
            )
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

    companion object {
        fun newInstance(id: Long) = DetailProjectFragment().apply {
            arguments = Bundle().apply {
                putLong("id_project", id)
            }
        }
    }
}