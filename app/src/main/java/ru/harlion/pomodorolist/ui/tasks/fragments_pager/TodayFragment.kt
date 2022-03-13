package ru.harlion.pomodorolist.ui.tasks.fragments_pager


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.databinding.FragmentTodayBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.ui.tasks.TasksViewModel


class TodayFragment : BindingFragment<FragmentTodayBinding>(FragmentTodayBinding::inflate) {

    private lateinit var adapterTask: AdapterTask
    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTasksByDate()

        viewModel.tasks.observe(viewLifecycleOwner, {
            taskRecyclerView( it
                .filter { task ->
                    task.date == System.currentTimeMillis()
                }
            )
        })

    }

    private fun taskRecyclerView(tasks: List<Task>) {
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL

        adapterTask = AdapterTask(viewModel::updateTask)

        binding.listTaskRecycler.apply {
            layoutManager = llm
            adapter = adapterTask
        }

        if (tasks.isNotEmpty()) {
            adapterTask.items = tasks
            binding.listTaskRecycler.visibility = View.VISIBLE
            binding.taskEmpty.visibility = View.GONE
        } else {
            binding.listTaskRecycler.visibility = View.GONE
            binding.taskEmpty.visibility = View.VISIBLE
        }
    }
}