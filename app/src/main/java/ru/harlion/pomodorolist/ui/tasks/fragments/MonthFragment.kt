package ru.harlion.pomodorolist.ui.tasks.fragments


import android.graphics.Color.parseColor
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentMonthBinding
import ru.harlion.pomodorolist.ui.tasks.TasksViewModel
import ru.harlion.pomodorolist.utils.replaceFragment
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class MonthFragment : BindingFragment<FragmentMonthBinding>(FragmentMonthBinding::inflate) {

    private val viewModel: TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        var lDate = LocalDate.now()
//        binding.calendarView.apply {
//            val millis = lDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
//            date = millis
//            maxDate = millis + 31536000000
//
//            setOnDateChangeListener { _, year, month, dayOfMonth ->
//                lDate = LocalDate.of(year, Month.values()[month], dayOfMonth)
//
//                requireParentFragment().replaceFragment(DayTasksFragment.newInstance(lDate), true)
//
//            }
//        }

        binding.calendarView.setOnDayClickListener (object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val lDate = LocalDate.now()
                val millis = lDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
                eventDay.calendar.timeInMillis = millis

               // lDate = LocalDate.of(year, Month.values()[month], dayOfMonth) //todo

                requireParentFragment().replaceFragment(DayTasksFragment.newInstance(lDate), true)
            }

        })

    }



    override fun onStart() {
        super.onStart()
        observeTasks()
    }

    private fun observeTasks() {
        viewModel.tasks.observe(this) { tasks ->
            val events = arrayListOf<EventDay>()
            for (i in tasks.indices) {
                val s = tasks!![i]
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = s.date
                //calendar.set(2020, 6, 7)
                //calendar.setCurrentMonthDayColors()
                events.add(EventDay(calendar, R.drawable.ic_label, parseColor("#228B22")))
                //events.add(EventDay(calendar, R.drawable.ic_no))
                //  events.add(EventDay(calendar, R.drawable.ic_repit, parseColor("#228B22")))
            }
            val calendarViewM = binding.calendarView
            calendarViewM.setEvents(events)
        }
    }
}