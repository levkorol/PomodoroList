package ru.harlion.pomodorolist.ui.tasks


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingHolder
import ru.harlion.pomodorolist.databinding.ItemTaskBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.models.TaskWithTime
import ru.harlion.pomodorolist.utils.Prefs
import ru.harlion.pomodorolist.utils.TimerService
import ru.harlion.pomodorolist.utils.timeToString

typealias ItemHolderTask = BindingHolder<ItemTaskBinding>

class AdapterTask(
    private val prefs: Prefs,
    private val updateTask: (Task) -> Unit,
    private val click: (Long) -> Unit,
    private val clickEditTask: (Long) -> Unit,
) : RecyclerView.Adapter<ItemHolderTask>() {

    var currentTaskId = 0L
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var items: List<TaskWithTime> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolderTask(ItemTaskBinding::inflate, parent).apply {

        }

    override fun onBindViewHolder(holder: ItemHolderTask, position: Int) {
        bindTask(prefs, currentTaskId, holder, items[position], updateTask, click, clickEditTask)
    }

    override fun getItemCount() = items.size
}

fun bindTask(
    prefs: Prefs,
    currentTaskId: Long,
    holder: ItemHolderTask,
    taskWithTime: TaskWithTime,
    updateTask: (Task) -> Unit,
    click: (Long) -> Unit,
    clickEditTask: (Long) -> Unit
) {
    holder.binding.apply {
        val (task, timeWork) = taskWithTime
        taskName.text = task.name

        if (timeWork > 0) {
            time.text = timeToString(timeWork)
        } else {
            time.text = ""
        }

        pauseOrPlay.setImageLevel(if (task.id == currentTaskId) 1 else 0)

        descTask.isChecked = task.isDone

        descTask.setOnClickListener {

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

        containerTaskItem.setOnClickListener {
            clickEditTask.invoke(task.id)
        }

        pauseOrPlay.setOnClickListener {

            it.context.bindService(
                Intent(it.context, TimerService::class.java),
                object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        prefs.taskId = task.id
                        (service as TimerService.TimerBinder).service.startTimer( true)
                        it.context.unbindService(this)
                        click.invoke(task.id)

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
            "normal" -> priority.visibility = View.GONE
            else -> priority.visibility = View.GONE
        }
    }
}
