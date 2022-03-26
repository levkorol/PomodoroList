package ru.harlion.pomodorolist.ui.tasks


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.opengl.Visibility
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingHolder
import ru.harlion.pomodorolist.databinding.ItemTaskBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.utils.TimerService

typealias ItemHolderTask = BindingHolder<ItemTaskBinding>

class AdapterTask(
    private val updateTask: (Task) -> Unit
) : RecyclerView.Adapter<ItemHolderTask>() {

    var items: List<Task> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolderTask(ItemTaskBinding::inflate, parent).apply {

        }

    override fun onBindViewHolder(holder: ItemHolderTask, position: Int) {
        bindTask(holder, items[position], updateTask)
    }

    override fun getItemCount() = items.size
}

fun bindTask(
    holder: ItemHolderTask,
    task: Task,
    updateTask: (Task) -> Unit
) {
    holder.binding.apply {
        descTask.text = task.name
        time.text = "47:00"
        //  setCheckMarkDrawable
        //             colorProj.setBackgroundColor(Color.CYAN)
        descTask.isChecked = task.isDone

        descTask.setOnClickListener {
            // descTask.isChecked = !descTask.isChecked
            if (!descTask.isChecked) {
                descTask.isChecked = true
                task.isDone = true
                updateTask.invoke(task)
            } else {
                descTask.isChecked = false
                task.isDone = false
                updateTask.invoke(task)
            }
        }

        pauseOrPlay.setOnClickListener {
            it.context.bindService(
                Intent(it.context, TimerService::class.java),
                object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                       (service as TimerService.TimerBinder).service.startTimer(task.id)
                        it.context.unbindService(this)
                    }
                    override fun onServiceDisconnected(name: ComponentName?) {}
                },
        Context.BIND_AUTO_CREATE
        )

    }

    when (task.priority) {
        "middle" -> priority.setBackgroundColor(
            ContextCompat.getColor(this.priority.context, R.color.green)
        )

        "high" -> priority.setBackgroundColor(
            ContextCompat.getColor(this.priority.context, R.color.priority_red)
        )
        else -> priority.visibility = View.GONE
    }
}
}

//"high" -> priority.setImageDrawable(
//ContextCompat.getDrawable(
//this.priority.context,
//R.drawable.ic_label_red
//)
//)