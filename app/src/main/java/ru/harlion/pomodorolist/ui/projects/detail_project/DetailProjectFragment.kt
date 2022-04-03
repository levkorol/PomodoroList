package ru.harlion.pomodorolist.ui.projects.detail_project

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentDetailProjectBinding
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.dialogs.AlertDialogBase
import ru.harlion.pomodorolist.ui.dialogs.DialogCalendar
import ru.harlion.pomodorolist.ui.dialogs.DialogPriorityTask
import ru.harlion.pomodorolist.ui.pomodoro.TimerFragment
import ru.harlion.pomodorolist.ui.profile.archive.ArchiveProjectFragment
import ru.harlion.pomodorolist.ui.projects.lists_projects.ListProjectsFragment
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.ui.tasks.edit.EditTaskFragment
import ru.harlion.pomodorolist.utils.*


class DetailProjectFragment :
    BindingFragment<FragmentDetailProjectBinding>(FragmentDetailProjectBinding::inflate) {

    private lateinit var adapterTask: AdapterTask
    private val viewModel: DetailProjectViewModel by viewModels()
    private var projectId = 0L
    private var priorityTask: String = ""
    private var isArchive = false
    private var date = 0L
    private var dateDeadline = 0L
    private var project: Project? = null
    private var taskFilter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        projectId = arguments?.getLong("id_project")!!

        setFragmentResultListener("priority") { _, bundle ->
            priorityTask = bundle.getString("priority_task") ?: ""
            setLabelPriorityTask()
        }

        setFragmentResultListener("calendarDate") { _, bundle ->
            date = bundle.getLong("epochMillis")
            binding.dateTask.text = dateToString(date)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            view.hideKeyboardExt()
        }

        initClicks()

        viewModel.getProjectById(projectId)

        viewModel.projectWithTasks.observe(viewLifecycleOwner, { projectWithTasks ->
            val it = projectWithTasks?.project
            if (it != null) {
                project = it
                if (it.isArchive) {
                    isArchive = true
                }

                if (it.deadline > 0) {
                    binding.deadline.text =
                        getString(R.string.do_deadline) + dateToStringShort(it.deadline)
                    binding.deadline.visibility = View.VISIBLE
                }


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
            }

            val tasks = projectWithTasks?.tasks
            if (tasks != null) {

                binding.countInFocus.text = timeToString(tasks.sumOf{ it.timeWork })

                when (taskFilter) {
                    1 -> tasksRecyclerView(tasks.sortedBy { task ->
                        task.isDone
                    })
                    2 -> tasksRecyclerView(tasks.sortedBy { task ->
                        task.isDone
                    })
                    3 -> tasksRecyclerView(tasks.sortedBy { task ->
                        task.priority == "high"
                    })
                    else -> tasksRecyclerView(tasks.sortedBy { task ->
                        task.isDone
                    })
                }


                val done = tasks.filter { task -> task.isDone }.size
                val max = tasks.size
                binding.countTasks.text = "$done / $max"
                binding.progressDoneTasks.max = max
                binding.progressDoneTasks.progress = done
            }


        })

        viewModel.getProjectById(projectId)
    }

    private fun initClicks() {
        binding.saveTask.setOnClickListener {
            viewModel.addTask(
                binding.nameTask.text.toString(),
                priorityTask,
                date
            )
            binding.nameTask.setText("")
            it.hideKeyboardExt()
            binding.nameTask.isCursorVisible = false
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
                    replaceFragment(ListProjectsFragment(), false)
                }
                setNegativeButton(getString(R.string.no)) {}
                show()
            }
        }

        binding.nameProject.setOnClickListener {
            AlertDialogBase(requireContext()).apply {
                setTitle(getString(R.string.edit_name_pr))
                setEditText("", project?.name ?: "")
                setPositiveButton(getString(R.string.yes)) {
                    viewModel.updateProjectName(newText.toString())
                }
                setNegativeButton(getString(R.string.no)) {}
                show()
            }
        }

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.archiveProject.setOnClickListener {
            if (!isArchive) {
                setArchive(getString(R.string.add_archive), setArchive = true, isArchive = false)
            } else {
                setArchive(getString(R.string.delete_archive), setArchive = false, isArchive = true)
            }
        }

//        binding.deadline.setOnClickListener {
//            DialogCalendar().show(parentFragmentManager, null)
//        }

        binding.prizeToComplete.setOnClickListener {
            AlertDialogBase(requireContext()).apply {
                setTitle(getString(R.string.edit_name_pr))
                setEditText("", project?.name ?: "")
                setPositiveButton(getString(R.string.yes)) {
                    viewModel.updatePrize(newText.toString())
                }
                setNegativeButton(getString(R.string.no)) {}
                show()
            }
        }
    }

    private fun setArchive(title: String, setArchive: Boolean, isArchive: Boolean) {
        AlertDialogBase(requireContext()).apply {
            setTitle(title)
            setPositiveButton(getString(R.string.yes)) {
                viewModel.updateArchive(setArchive)
                if (!isArchive) {
                    parentFragmentManager.popBackStack()
                } else {
                    parentFragmentManager.popBackStack()
                }
            }
            setNegativeButton(getString(R.string.no)) {}
            show()
        }
    }

    private fun tasksRecyclerView(tasks: List<Task>) {
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterTask = AdapterTask(viewModel::updateTask, {
            replaceFragment(TimerFragment(), true)
        }, {
            replaceFragment(EditTaskFragment.newInstance(it), true)
        })
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