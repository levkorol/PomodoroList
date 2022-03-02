package ru.harlion.pomodorolist.data

import android.content.Context
import androidx.room.Room
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task
import java.util.concurrent.Executors


private const val DATABASE_NAME = "pomodoro-database"
class Repository private constructor( context: Context) {


    private val database: DataBaseApp = Room.databaseBuilder(
        context.applicationContext,
        DataBaseApp::class.java,
        DATABASE_NAME
    ).build()

    private val projectDao = database.projectDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun addProject(project: Project) {
        executor.execute {
            projectDao.addProject(project)
        }
    }

   companion object {
       private var INSTANCE: Repository? = null

       fun initialize(context: Context) {
           if (INSTANCE == null) {
               INSTANCE = Repository(context)
           }
       }

       fun get(): Repository {
           return INSTANCE ?: throw IllegalStateException("Repository must be init")
       }

       fun getListProjects() =
           listOf(
               Project(
                   1, "Project work 2", "prize cucumber", listOf(), false, 1,0
               ),
               Project(
                   1, "Project work", "prize cucumber", listOf(), false, 1,0
               ),
               Project(
                   1, "Project work", "", listOf(), false, 0,0
               ),
               Project(
                   1, "Project work", "prize cucumber", listOf(), false, 0,0
               ),
               Project(
                   1, "Project work", "prize cucumber", listOf(), false, 1,0
               ),

           )


       fun getListTask() = listOf (
           Task(1, 1, 1, "taskito", "Высокий", true, 0, 3, true, 1111, 11111)
               )
   }
}