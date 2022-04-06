package ru.harlion.pomodorolist.ui.tasks.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentMonthBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.pomodoro.TimerFragment
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.ui.tasks.TasksViewModel
import ru.harlion.pomodorolist.ui.tasks.edit.EditTaskFragment
import ru.harlion.pomodorolist.utils.replaceFragment
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId


class MonthFragment : BindingFragment<FragmentMonthBinding>(FragmentMonthBinding::inflate) {

    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView

        var lDate = LocalDate.now()
        binding.calendarView.apply {
            val millis = lDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
            date = millis
            maxDate = millis + 31536000000

//            viewModel.getTasksByDate(
//                Instant.ofEpochMilli(
//                    lDate.atStartOfDay(ZoneId.systemDefault())
//                        .toEpochSecond() * 1000
//                ).atZone(ZoneId.systemDefault()).toLocalDate()
//            )

            setOnDateChangeListener { _, year, month, dayOfMonth ->
                lDate = LocalDate.of(year, Month.values()[month], dayOfMonth)

                requireParentFragment().replaceFragment(DayTasksFragment.newInstance(lDate), true)

//                viewModel.getTasksByDate(
//                    Instant.ofEpochMilli(
//                        lDate.atStartOfDay(ZoneId.systemDefault())
//                            .toEpochSecond() * 1000
//                    ).atZone(ZoneId.systemDefault()).toLocalDate()
//                )
            }
        }
    }
}