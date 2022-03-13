package ru.harlion.pomodorolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.harlion.pomodorolist.ui.pomodoro.TimerFragment
import ru.harlion.pomodorolist.ui.profile.ProfileFragment
import ru.harlion.pomodorolist.ui.profile.on_boarding.OnBoardingFragment
import ru.harlion.pomodorolist.ui.profile.statistic.StatisticFragment
import ru.harlion.pomodorolist.ui.projects.ListProjectsFragment
import ru.harlion.pomodorolist.ui.tasks.ListTasksFragment
import ru.harlion.pomodorolist.utils.Prefs
import ru.harlion.pomodorolist.utils.replaceFragment

class AppActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        

        bottomNavView = findViewById(R.id.bottom_nav_view)

        val prefs = Prefs(this)
        if(!prefs.isShowOnBoarding) {
            prefs.isShowOnBoarding = true
            replaceFragment(OnBoardingFragment(), true)
        } else {
            replaceFragment(TimerFragment(), true)
        }




        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_main_page -> {
                    replaceFragment(TimerFragment(), true)
                    true
                }

                R.id.item_tasks_page -> {
                    replaceFragment(ListTasksFragment(), true)
                    true
                }

                R.id.item_projects_page -> {
                    replaceFragment(ListProjectsFragment(), true)
                    true
                }
                R.id.item_profile_page -> {
                    replaceFragment(ProfileFragment(), true)
                    true
                }

                else -> false
            }
        }
    }

    fun setBottomNavigationVisible(isVisible: Boolean) {
        if(isVisible) {
            bottomNavView.visibility  = View.VISIBLE
        } else {
            bottomNavView.visibility  = View.GONE
        }
    }
}