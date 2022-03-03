package ru.harlion.pomodorolist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.harlion.pomodorolist.models.Project

@Dao
interface ProjectDao {

    @Query("SELECT * FROM project")
    fun getProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM project WHERE id = (:id)")
    fun getProjectById(id: Long): LiveData<Project?>

    @Update
    fun updateProject(project: Project)

    @Insert
    fun addProject(project: Project) : Long

    @Delete
    fun deleteProject(project: Project)
}