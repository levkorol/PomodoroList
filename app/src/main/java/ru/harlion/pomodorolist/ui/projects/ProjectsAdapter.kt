package ru.harlion.pomodorolist.ui.projects

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.pomodorolist.base.BindingHolder
import ru.harlion.pomodorolist.databinding.ItemProjectBinding
import ru.harlion.pomodorolist.databinding.ItemTaskBinding
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.tasks.ItemHolderTask
import ru.harlion.pomodorolist.ui.tasks.bindTask

private typealias ItemHolderProject = BindingHolder<ItemProjectBinding>

class ProjectsAdapter(
    private val click: (Long) -> Unit,
    private val taskList: (Long) -> List<Task>
) :
    RecyclerView.Adapter<BindingHolder<*>>() {

    var item: List<Any> = listOf()

    override fun getItemViewType(position: Int): Int {
        return when (item[position]) {
            is Project -> 1
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
                            click.invoke((item[adapterPosition] as Project).id)
                        }
                    }
                    binding.countTasks.setOnClickListener {
                        if (adapterPosition > -1) {
                            if (item.getOrNull(adapterPosition + 1) is Task) {
                                val taskCount =
                                    item.subList(adapterPosition + 1, item.size).indexOfFirst {
                                        it is Project
                                    }
                                val newItems = item.toMutableList()
                                newItems.subList(adapterPosition + 1, adapterPosition + 1 + taskCount)
                                    .clear()
                                item = newItems
                                notifyItemRangeRemoved(adapterPosition + 1, taskCount)
                            } else {
                                val newItems = item.toMutableList()
                                val elements =
                                    taskList.invoke((item[adapterPosition] as Project).id)
                                newItems.addAll(
                                    adapterPosition + 1,
                                    elements
                                )
                                item = newItems
                                notifyItemRangeInserted(adapterPosition + 1, elements.size)
                            }
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

                val project = item[position] as Project

                name.text = project.name

                if (project.deadline > 0) {
                    deadline.text = "Сделать до : 121212"
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

                val max = project.tasks.size
                val done = project.tasks.filter { it.isDone }.size
                countTasks.text =
                    "$done / $max"
                progressDoneTasks.max = max
                progressDoneTasks.progress = done
            }
        } else {
            bindTask(holder as ItemHolderTask, item[position] as Task)
        }

    }

    override fun getItemCount() = item.size
}