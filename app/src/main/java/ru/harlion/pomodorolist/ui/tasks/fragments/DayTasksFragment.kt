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
import ru.harlion.pomodorolist.utils.dateToString
import ru.harlion.pomodorolist.utils.replaceFragment
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DayTasksFragment : BindingFragment<FragmentDayTasksBinding>(FragmentDayTasksBinding::inflate) {

    private val viewModel : TasksViewModel by viewModels()
    private lateinit var adapterTask: AdapterTask
    private var dateTasks = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dateTasks = arguments?.getLong("DATE_TASKS", 0L) ?: 0L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = dateToString(dateTasks)

        binding.tasksRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AdapterTask(viewModel::updateTask,{
                replaceFragment(TimerFragment(), true)
            } , {
                replaceFragment(EditTaskFragment.newInstance(it), true)
            }) .also {
                adapterTask = it
            }
        }

        val lDate = LocalDate.now()
        lDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000

        viewModel.getTasksByDate(  Instant.ofEpochMilli(
            lDate.atStartOfDay(ZoneId.systemDefault())
                .toEpochSecond() * 1000
        ).atZone(ZoneId.systemDefault()).toLocalDate())


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
            adapterTask.items = tasks
            binding.tasksRecycler.visibility = View.VISIBLE
            binding.taskEmpty.visibility = View.GONE
        } else {
            binding.tasksRecycler.visibility = View.GONE
            binding.taskEmpty.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(date : Long) = DayTasksFragment().apply {
            arguments?.putLong("DATE_TASK", date)
        }
    }
}