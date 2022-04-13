package ru.harlion.pomodorolist.ui.profile.statistic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.ProjectWithTime

class StatisticViewModel : ViewModel() {

    private  val repo = Repository.get()
    val projectTime = MutableLiveData<List<ProjectWithTime>>(emptyList())

    fun initViewModel (start : Long) {
      projectTime.value = repo.getFocusStatisticByProject(start)
    }
}