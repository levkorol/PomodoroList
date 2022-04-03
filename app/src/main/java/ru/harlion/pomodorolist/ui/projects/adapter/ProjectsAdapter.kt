package ru.harlion.pomodorolist.ui.projects.adapter

import android.content.res.ColorStateList
import android.os.Build
import android.provider.Settings.Global.getString
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingHolder
import ru.harlion.pomodorolist.databinding.ItemProjectBinding
import ru.harlion.pomodorolist.databinding.ItemTaskBinding
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.ProjectWithProgress
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.tasks.ItemHolderTask
import ru.harlion.pomodorolist.ui.tasks.bindTask
import ru.harlion.pomodorolist.utils.dateToStringShort

private typealias ItemHolderProject = BindingHolder<ItemProjectBinding>

class ProjectsAdapter(
    private val click: (Long) -> Unit,
    private val taskList: (Long) -> List<Task>,
    private val updateTask: (Task) -> Unit,
    private val clickTask: (Long) -> Unit
) :
    RecyclerView.Adapter<BindingHolder<*>>() {

    private var item: List<Any> = listOf()
    var currentTaskId = 0L
    set (value){
        field = value
        notifyDataSetChanged()
    }

    fun setItems(value : List<ProjectWithProgress>) {
        val newList = value.toMutableList<Any>()
        val listIterator = newList.listIterator()
        while(listIterator.hasNext()) {
            listIterator.next()
            val previousIndex = listIterator.previousIndex()
            if (item.getOrNull(previousIndex + 1) is Task) {
                val index = item.subList(previousIndex + 1, item.size).indexOfFirst {
                    it is ProjectWithProgress
                }
                item.subList(
                    previousIndex + 1,
                    if (index < 0) {
                        item.size
                    } else {
                        previousIndex + 1 + index
                    }
                ).forEach {
                    listIterator.add(it)
                }
            }
        }
        item = newList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (item[position]) {
            is ProjectWithProgress -> 1
            is Task -> 2
            else -> throw Exception()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            1 -> {
                ItemHolderProject(ItemProjectBinding::inflate, parent).apply {
                    binding.containerItemCv.setOnClickListener {
                        if (adapterPosition > -1) {
                            click.invoke((item[adapterPosition] as ProjectWithProgress).project.id)
                        }
                    }
                    binding.countTasks.setOnClickListener {
                        if (adapterPosition > -1) {
                            if (item.getOrNull(adapterPosition + 1) is Task) {
                                var taskCount =
                                    item.subList(adapterPosition + 1, item.size).indexOfFirst {
                                        it is ProjectWithProgress
                                    }
                                val newItems = item.toMutableList()
                                val subList = newItems.subList(
                                    adapterPosition + 1,
                                    if (taskCount < 0) {
                                        newItems.size
                                    } else {
                                        adapterPosition + 1 + taskCount
                                    }
                                )
                                taskCount = subList.size
                                subList.clear()
                                item = newItems
                                notifyItemRangeRemoved(adapterPosition + 1, taskCount)
                            } else {
                                val newItems = item.toMutableList()
                                val elements =
                                    taskList.invoke((item[adapterPosition] as ProjectWithProgress).project.id).sortedBy {       //or filter
                                       it.isDone
                                    }
                                newItems.addAll(
                                    adapterPosition + 1,
                                    elements
                                )
                                item = newItems
                                notifyItemRangeInserted(adapterPosition + 1, elements.size)
                            }
                            notifyItemChanged(adapterPosition, Unit)
                        }
                    }
                }
            }
            2 -> ItemHolderTask(ItemTaskBinding::inflate, parent).apply { }
            else -> throw Exception()
        }


    override fun onBindViewHolder(holder: BindingHolder<*>, position: Int) {

        if (holder.binding is ItemProjectBinding) {
            holder.binding.apply {

                countTasks.compoundDrawablesRelative[2].level =
                    if (item.getOrNull(position + 1) is Task) 0 else 5000

                val projectWithP = item[position] as ProjectWithProgress
                val project = projectWithP.project

                name.text = project.name

                if (project.deadline > 0) {
                    deadline.text = "Сделать до" + dateToStringShort(project.deadline)
                    deadline.visibility = View.VISIBLE
                } else {
                    deadline.visibility = View.GONE
                }

                if (project.prize.isNotEmpty()) {
                    prize.text = project.prize
                    prize.visibility = View.VISIBLE
                } else {
                    prize.visibility = View.GONE
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (project.color > 0) {
                        val color = ContextCompat.getColor(name.context, project.color)
                        val colorList = ColorStateList.valueOf(color)
                        TextViewCompat.setCompoundDrawableTintList(name, colorList)
                    }
                }

                countTasks.text = "${projectWithP.doneTasks} / ${projectWithP.tasks}"
                progressDoneTasks.max = projectWithP.tasks
                progressDoneTasks.progress = projectWithP.doneTasks
            }
        } else {
            bindTask(currentTaskId, holder as ItemHolderTask, item[position] as Task, updateTask, clickTask){} //todo edit task
        }
    }

    override fun getItemCount() = item.size
}