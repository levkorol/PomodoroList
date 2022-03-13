package ru.harlion.pomodorolist.ui.tasks


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentListTasksBinding
import ru.harlion.pomodorolist.ui.projects.adding.AddProjectFragment
import ru.harlion.pomodorolist.ui.tasks.fragments_pager.MonthFragment
import ru.harlion.pomodorolist.ui.tasks.fragments_pager.TodayFragment
import ru.harlion.pomodorolist.utils.replaceFragment

class ListTasksFragment : BindingFragment<FragmentListTasksBinding>(FragmentListTasksBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tasksViewPager.adapter = TaskViewPager(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabTasks, binding.tasksViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.today)
                }
                else -> {
                    tab.text = getString(R.string.month)
                }
            }
        }.attach()
    }

    class TaskViewPager(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TodayFragment()
                else -> MonthFragment()
            }
        }
    }
}