package ru.harlion.pomodorolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.harlion.pomodorolist.ui.pomodoro.TimerFragment
import ru.harlion.pomodorolist.ui.profile.ProfileFragment
import ru.harlion.pomodorolist.ui.profile.statistic.StatisticFragment
import ru.harlion.pomodorolist.utils.replaceFragment

class AppActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.bottom_nav_view)

        replaceFragment(TimerFragment(), true)

        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_main_page -> {
                    replaceFragment(TimerFragment(), true)
                    true
                }

                R.id.item_projects_page -> {
                    replaceFragment(StatisticFragment(), true)
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
}