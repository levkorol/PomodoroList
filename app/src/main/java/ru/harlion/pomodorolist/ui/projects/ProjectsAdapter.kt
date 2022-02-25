package ru.harlion.pomodorolist.ui.projects

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.pomodorolist.base.BindingHolder
import ru.harlion.pomodorolist.databinding.ItemProjectBinding
import ru.harlion.pomodorolist.models.Project

private typealias ItemHolderProject = BindingHolder<ItemProjectBinding>

class ProjectsAdapter(private val click: (Long) -> Unit) :
    RecyclerView.Adapter<ItemHolderProject>() {

    var item: List<Project> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolderProject(ItemProjectBinding::inflate, parent).apply {
            binding.containerItemCv.setOnClickListener {
                if (adapterPosition > -1) {
                    click.invoke((item[adapterPosition]).id)
                }
            }
        }

    override fun onBindViewHolder(holder: ItemHolderProject, position: Int) {
        holder.binding.apply {

            containerItemCv.setOnClickListener {
                click.invoke(item[position].id)
            }

            name.text = item[position].name

            if (item[position].deadline > 0) {
                deadline.text = "Сделать до : 121212"
                deadline.visibility = View.VISIBLE
            } else {
                deadline.visibility = View.GONE
            }

            if (item[position].prize.isNotEmpty()) {
                prize.text = item[position].prize
                prize.visibility = View.VISIBLE
            } else {
                prize.visibility = View.GONE
            }

            val max = item[position].tasks.size
            val done = item[position].tasks.filter { it.isDone }.size
            countTasks.text =
                "$done / $max"
            progressDoneTasks.max = max
            progressDoneTasks.progress = done
        }
    }

    override fun getItemCount() = item.size
}