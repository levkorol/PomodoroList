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
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.dialogs.AlertDialogBase
import ru.harlion.pomodorolist.ui.dialogs.DialogCalendar
import ru.harlion.pomodorolist.ui.dialogs.DialogPriorityTask
import ru.harlion.pomodorolist.ui.profile.archive.ArchiveProjectFragment
import ru.harlion.pomodorolist.ui.projects.lists_projects.ListProjectsFragment
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.utils.dateToString
import ru.harlion.pomodorolist.utils.hideKeyboardExt
import ru.harlion.pomodorolist.utils.replaceFragment


class DetailProjectFragment :
    BindingFragment<FragmentDetailProjectBinding>(FragmentDetailProjectBinding::inflate) {

    private lateinit var adapterTask: AdapterTask
    private val viewModel: DetailProjectViewModel by viewModels()
    private var projectId = 0L
    private var priorityTask: String = ""
    private var isArchive = false
    private var date = 0L

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

        viewModel.tasks.observe(viewLifecycleOwner, {
            tasksRecyclerView(it.sortedBy { task ->
                task.isDone
            })
        })

        viewModel.project.observe(viewLifecycleOwner, {
            if(it.isArchive) {
                isArchive = true
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
                val text = setEditText("2", "")
                setPositiveButton(getString(R.string.yes)) {
                    viewModel.updateProjectName(text)
                    replaceFragment(ListProjectsFragment(), false)
                }
                setNegativeButton(getString(R.string.no)) {}
                show()
            }
        }

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.archiveProject.setOnClickListener {
            if(!isArchive) {
                setArchive(getString(R.string.add_archive), setArchive = true, isArchive = false)
            } else {
                setArchive(getString(R.string.delete_archive), setArchive = false, isArchive = true)
            }
        }
    }

    private fun setArchive(title : String, setArchive: Boolean, isArchive : Boolean) {
        AlertDialogBase(requireContext()).apply {
            setTitle(title)
            setPositiveButton(getString(R.string.yes)) {
                viewModel.updateArchive(setArchive)
                if(!isArchive) {
                    parentFragmentManager.popBackStack() //todo stack
                    replaceFragment(ListProjectsFragment(), true)
                } else {
                    parentFragmentManager.popBackStack()
                    replaceFragment(ArchiveProjectFragment(), true)
                }
            }
            setNegativeButton(getString(R.string.no)) {}
            show()
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