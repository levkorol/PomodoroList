package ru.harlion.pomodorolist.ui.tasks.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentTodayBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.pomodoro.TimerFragment
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.ui.tasks.TasksViewModel
import ru.harlion.pomodorolist.ui.tasks.edit.EditTaskFragment
import ru.harlion.pomodorolist.utils.Prefs
import ru.harlion.pomodorolist.utils.replaceFragment
import java.time.LocalDate


class TodayFragment : BindingFragment<FragmentTodayBinding>(FragmentTodayBinding::inflate) {

    private lateinit var adapterTask: AdapterTask
    private val viewModel: TasksViewModel by viewModels()
    private lateinit var prefs : Prefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = Prefs(requireContext())

        binding.listTaskRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AdapterTask(prefs, viewModel::updateTask,{
                requireParentFragment().replaceFragment(TimerFragment(), true)
            } , {
                requireParentFragment().replaceFragment(EditTaskFragment.newInstance(it), true)
            }) .also {
                adapterTask = it
            }
        }

        viewModel.getTasksByDate(LocalDate.now())

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskRecyclerView(it)
        }
    }

    private fun taskRecyclerView(tasks: List<Task>) {
        if (tasks.isNotEmpty()) {
         //   adapterTask.items = tasks
            binding.listTaskRecycler.visibility = View.VISIBLE
            binding.taskEmpty.visibility = View.GONE
        } else {
            binding.listTaskRecycler.visibility = View.GONE
            binding.taskEmpty.visibility = View.VISIBLE
        }
    }
}