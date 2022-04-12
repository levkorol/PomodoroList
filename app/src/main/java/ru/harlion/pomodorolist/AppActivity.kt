package ru.harlion.pomodorolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.harlion.pomodorolist.ui.pomodoro.TimerFragment
import ru.harlion.pomodorolist.ui.profile.ProfileFragment
import ru.harlion.pomodorolist.ui.profile.on_boarding.OnBoardingFragment
import ru.harlion.pomodorolist.ui.projects.ProjectsFragment
import ru.harlion.pomodorolist.ui.projects.lists_projects.ListProjectsFragment
import ru.harlion.pomodorolist.ui.tasks.ListTasksFragment
import ru.harlion.pomodorolist.utils.Prefs
import ru.harlion.pomodorolist.utils.replaceFragment

class AppActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = Prefs(this)

        when (prefs.theme) {
            "green" -> setTheme(R.style.GreenTheme)
            "sky" -> setTheme(R.style.SkyTheme)
            "peach" -> setTheme(R.style.PeachTheme)
            "violet" -> setTheme(R.style.VioletTheme)
            "pink" -> setTheme(R.style.PinkTheme)
            "sea" -> setTheme(R.style.SeaTheme)
            "red" -> setTheme(R.style.RedTheme)
             else -> setTheme(R.style.RedTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.bottom_nav_view)


        if (!prefs.isShowOnBoarding) {
            prefs.isShowOnBoarding = true
            replaceFragment(OnBoardingFragment(), false)
        } else {
            replaceFragment(TimerFragment(), false)
        }

        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_main_page -> {
                    replaceFragment(TimerFragment(), true)
                    true
                }

//                R.id.item_tasks_page -> {
//                    replaceFragment(ListTasksFragment(), true)
//                    true
//                }

                R.id.item_projects_page -> {
                    replaceFragment(ProjectsFragment(), true)
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
        if (isVisible) {
            bottomNavView.visibility = View.VISIBLE
        } else {
            bottomNavView.visibility = View.GONE
        }
    }

    fun updateNavigation(fragment: Fragment) {
        when (fragment) {
            is TimerFragment -> bottomNavView.menu.findItem(R.id.item_main_page).isChecked =
                true
//            is ListTasksFragment -> bottomNavView.menu.findItem(R.id.item_tasks_page).isChecked =
//                true
            is ListProjectsFragment -> bottomNavView.menu.findItem(R.id.item_projects_page).isChecked =
                true
            is ProfileFragment -> bottomNavView.menu.findItem(R.id.item_profile_page).isChecked =
                true
        }
    }
}