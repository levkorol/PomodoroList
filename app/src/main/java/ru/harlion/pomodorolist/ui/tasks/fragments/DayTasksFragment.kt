package ru.harlion.pomodorolist.ui.tasks.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentDayTasksBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.pomodoro.TimerFragment
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.ui.tasks.TasksViewModel
import ru.harlion.pomodorolist.ui.tasks.edit.EditTaskFragment
import ru.harlion.pomodorolist.utils.Prefs
import ru.harlion.pomodorolist.utils.dateToString
import ru.harlion.pomodorolist.utils.replaceFragment
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DayTasksFragment :
    BindingFragment<FragmentDayTasksBinding>(FragmentDayTasksBinding::inflate) {

    private val viewModel: TasksViewModel by viewModels()
    private lateinit var adapterTask: AdapterTask
    private lateinit var dateTasks: LocalDate
    private lateinit var prefs : Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dateTasks = arguments?.getSerializable("DATE_TASK") as LocalDate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = Prefs(requireContext())

        binding.title.text =
            dateToString(dateTasks.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000)

        binding.tasksRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AdapterTask(prefs, viewModel::updateTask, {
                replaceFragment(TimerFragment(), true)
            }, {
                replaceFragment(EditTaskFragment.newInstance(it), true)
            }).also {
                adapterTask = it
            }
        }

        binding.back.setOnClickListener { parentFragmentManager.popBackStack() }

        viewModel.getTasksByDate(
            Instant.ofEpochMilli(
                dateTasks.atStartOfDay(ZoneId.systemDefault())
                    .toEpochSecond() * 1000
            ).atZone(ZoneId.systemDefault()).toLocalDate()
        )

        viewModel.tasks.observe(viewLifecycleOwner, {
            taskRecyclerView(it)
        })
    }

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }

    private fun taskRecyclerView(tasks: List<Task>) {
        if (tasks.isNotEmpty()) {
         //   adapterTask.items = tasks
            binding.tasksRecycler.visibility = View.VISIBLE
            binding.taskEmpty.visibility = View.GONE
        } else {
            binding.tasksRecycler.visibility = View.GONE
            binding.taskEmpty.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(date: LocalDate) = DayTasksFragment().apply {
            arguments = Bundle().apply {
                putSerializable("DATE_TASK", date)
            }
        }
    }
}