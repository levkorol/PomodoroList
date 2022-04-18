package ru.harlion.pomodorolist.ui.profile.statistic

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentStatisticBinding
import ru.harlion.pomodorolist.utils.formatTimeMins
import ru.harlion.pomodorolist.utils.formatTimeMinsSec
import java.time.LocalDate


class StatisticFragment :
    BindingFragment<FragmentStatisticBinding>(FragmentStatisticBinding::inflate) {

    private val viewModel: StatisticViewModel by viewModels()
    private var timeAll = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStatisticFocus(LocalDate.now().toEpochDay())

        initClicks()

        viewModel.allFocusTime.observe(viewLifecycleOwner, {
            binding.countInFocus.text = formatTimeMinsSec(it, resources)
            timeAll = it
        })

        viewModel.projectTime.observe(viewLifecycleOwner, {
            binding.chartFocus.setUsePercentValues(true)
            val millis = it.sumOf { pwt -> pwt.timeWork }
            binding.countInFocusTasks.text = formatTimeMinsSec(millis, resources)
            binding.countAll.text = formatTimeMinsSec(millis + timeAll, resources)

            val dataEntries = it.map { (name, timeWork) ->
                PieEntry(timeWork.toFloat(), name)
            }

            val colors = it.map { pwt ->
                    pwt.color
            }

            val dataSet = PieDataSet(dataEntries, "")
            val data = PieData(dataSet)

            data.setValueFormatter(object : ValueFormatter() {
                override fun getPieLabel(value: Float, pieEntry: PieEntry): String {
                    return formatTimeMinsSec(pieEntry.value.toLong(), resources)
                }
            })
            dataSet.sliceSpace = 3f
            dataSet.colors = colors
            binding.chartFocus.data = data
            data.setValueTextSize(10f)
            binding.chartFocus.apply {
                setExtraOffsets(5f, 10f, 5f, 5f)
                animateY(1400, Easing.EaseInOutQuad)
                holeRadius = 40f
                transparentCircleRadius = 69f
                isDrawHoleEnabled = true
                setHoleColor(Color.WHITE)
                setDrawCenterText(true);
                centerText = requireContext().getString(R.string.projects_menu)
                invalidate()
            }
        })

        initPieChart()
    }

    private fun initClicks() {
        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.today.setOnClickListener {
            viewModel.getStatisticFocus(LocalDate.now().toEpochDay())
        }

        binding.week.setOnClickListener {
            viewModel.getStatisticFocus(LocalDate.now().plusDays(-7).toEpochDay())
        }

        binding.month.setOnClickListener {
            viewModel.getStatisticFocus(LocalDate.now().plusDays(-30).toEpochDay())
        }

        binding.allTime.setOnClickListener {
            viewModel.getStatisticFocus(LocalDate.now().plusDays(-10000).toEpochDay()) //todo
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


    private fun initPieChart() {
        binding.chartFocus.apply {
            setUsePercentValues(true)
            description.text = ""
            isDrawHoleEnabled = false
            setTouchEnabled(false)
            setDrawEntryLabels(false)
            setExtraOffsets(20f, 0f, 20f, 20f)
            setUsePercentValues(true)
            isRotationEnabled = false
            setDrawEntryLabels(false)
            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.isWordWrapEnabled = true
        }

    }
}