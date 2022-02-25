package ru.harlion.pomodorolist.ui.tasks


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.pomodorolist.base.BindingHolder
import ru.harlion.pomodorolist.databinding.ItemTaskBinding
import ru.harlion.pomodorolist.models.Task

private typealias ItemHolderTask = BindingHolder<ItemTaskBinding>

class AdapterTask() : RecyclerView.Adapter<ItemHolderTask>() {

    var items: List<Task> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolderTask(ItemTaskBinding::inflate, parent).apply {

        }

    override fun onBindViewHolder(holder: ItemHolderTask, position: Int) {
         holder.binding.apply {
             descTask.text = items[position].name
             time.text = "0"
           //  setCheckMarkDrawable
//             colorProj.setBackgroundColor(Color.CYAN)
             descTask.isChecked = items[position].isDone
         }
    }

    override fun getItemCount() = items.size
}