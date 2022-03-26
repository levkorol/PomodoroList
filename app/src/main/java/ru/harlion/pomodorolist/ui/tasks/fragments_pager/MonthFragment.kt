package ru.harlion.pomodorolist.ui.tasks.fragments_pager


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentMonthBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.ui.tasks.TasksViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId


class MonthFragment : BindingFragment<FragmentMonthBinding>(FragmentMonthBinding::inflate) {

    private val viewModel: TasksViewModel by viewModels()
    private lateinit var adapterTask: AdapterTask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recyclerViewTaskMonth.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AdapterTask(viewModel::updateTask).also {
                adapterTask = it
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner, {
            setTasks(it)
        })

        var lDate = LocalDate.now()
        binding.calendarView.apply {
            val millis = lDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
            date = millis
            maxDate = millis + 31536000000

            viewModel.getTasksByDate(
                Instant.ofEpochMilli(
                    lDate.atStartOfDay(ZoneId.systemDefault())
                        .toEpochSecond() * 1000
                ).atZone(ZoneId.systemDefault()).toLocalDate()
            )

            setOnDateChangeListener { _, year, month, dayOfMonth ->
                lDate = LocalDate.of(year, Month.values()[month], dayOfMonth)

                viewModel.getTasksByDate(
                    Instant.ofEpochMilli(
                        lDate.atStartOfDay(ZoneId.systemDefault())
                            .toEpochSecond() * 1000
                    ).atZone(ZoneId.systemDefault()).toLocalDate()
                )
            }
        }
    }

    private fun setTasks(tasks: List<Task>) {
        if (tasks.isNotEmpty()) {
            adapterTask.items = tasks
//            binding.listTaskRecycler.visibility = View.VISIBLE
//            binding.taskEmpty.visibility = View.GONE
        } else {
//            binding.listTaskRecycler.visibility = View.GONE
//            binding.taskEmpty.visibility = View.VISIBLE
        }
    }

}