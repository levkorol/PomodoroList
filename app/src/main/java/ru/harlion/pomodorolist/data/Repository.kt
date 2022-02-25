package ru.harlion.pomodorolist.data

import android.content.Context
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task

class Repository private constructor( context: Context) {


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