package ru.harlion.pomodorolist.ui.profile.statistic

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentStatisticBinding
import ru.harlion.pomodorolist.utils.timeToString
import java.time.LocalDate


class StatisticFragment : BindingFragment<FragmentStatisticBinding>(FragmentStatisticBinding::inflate) {

    private val viewModel : StatisticViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.today.setOnClickListener {
            viewModel.initViewModel(LocalDate.now().toEpochDay())
        }

        binding.week.setOnClickListener {
            viewModel.initViewModel(LocalDate.now().plusDays(-7).toEpochDay())
        }

        viewModel.projectTime.observe(viewLifecycleOwner, {
            binding.chartFocus.setUsePercentValues(true)
            val dataEntries = it.map { (name, timeWork) ->
                 PieEntry(timeWork.toFloat(), name )
            }

            val colors: ArrayList<Int> = ArrayList()
            colors.add(Color.parseColor("#4DD0E1"))
            colors.add(Color.parseColor("#FFF176"))
            colors.add(Color.parseColor("#FF8A65"))
            colors.add(Color.parseColor("#4DD0E6"))
            colors.add(Color.parseColor("#FFF178"))
            colors.add(Color.parseColor("#FF8A63"))

            val dataSet = PieDataSet(dataEntries, "")
            val data = PieData(dataSet)

            data.setValueFormatter(object : ValueFormatter() {
                override fun getPieLabel(value: Float, pieEntry: PieEntry): String {
                    return timeToString(pieEntry.value.toLong())
                }
            })
            dataSet.sliceSpace = 3f
            dataSet.colors = colors
            binding.chartFocus.data = data
            data.setValueTextSize(15f)
            binding.chartFocus.apply {
                setExtraOffsets(5f, 10f, 5f, 5f)
                animateY(1400, Easing.EaseInOutQuad)
                holeRadius = 30f
                transparentCircleRadius = 61f
                isDrawHoleEnabled = true
                setHoleColor(Color.WHITE)
                setDrawCenterText(true);
                centerText = "Мой фокус"
                invalidate()
            }
        })

        initPieChart()
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