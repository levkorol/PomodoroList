package ru.harlion.pomodorolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.reflect.TypeToken
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.AppApplication
import ru.harlion.pomodorolist.data.dao.ProjectDao
import ru.harlion.pomodorolist.data.dao.TaskDao
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task

@Database(entities = [Project::class, Task::class], version = 1, exportSchema = false)
@TypeConverters(ConverterApp::class)
abstract class DataBaseApp : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
}

object ConverterApp {
    @TypeConverter
    fun taskToString(emotion : List<Task>): String = AppApplication.gson.toJson(emotion)

    @TypeConverter
    fun stringToTask(string: String): List<Task> =
        AppApplication.gson.fromJson(string, TypeToken.getParameterized(List::class.java, Task::class.java).type)
}
