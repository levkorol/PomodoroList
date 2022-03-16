package ru.harlion.pomodorolist.ui.projects.adding


import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.base.onEvent
import ru.harlion.pomodorolist.databinding.FragmentAddTaskBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.dialogs.DialogCalendar
import ru.harlion.pomodorolist.ui.dialogs.DialogColor
import ru.harlion.pomodorolist.ui.projects.detail_project.DetailProjectFragment
import ru.harlion.pomodorolist.ui.tasks.AdapterTask
import ru.harlion.pomodorolist.utils.replaceFragment

class AddProjectFragment :
    BindingFragment<FragmentAddTaskBinding>(FragmentAddTaskBinding::inflate) {

    private lateinit var adapterTask: AdapterTask
    private val viewModel: AddProjectViewModel by viewModels()
    private var colorId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getLong("id_project")?.takeIf { it > 0 }?.let {
            viewModel.setProjectId(it, savedInstanceState == null) }

        setFragmentResultListener("color") { _, bundle ->
            colorId = bundle.getInt("colorId")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              //  binding.color.compoundDrawables[0].setTint(colorId)
               // TintList = ColorStateList.valueOf(colorId)

                val color = ContextCompat.getColor(requireContext(), colorId)
                val colorList = ColorStateList.valueOf(color)
                TextViewCompat.setCompoundDrawableTintList(binding.color, colorList)

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.createdProjectId.onEvent(viewLifecycleOwner, {
            parentFragmentManager.popBackStack()
            replaceFragment(DetailProjectFragment.newInstance(it), true)
        })

        viewModel.project.observe(viewLifecycleOwner, {
           binding.name.setText(it.name)
        })

        binding.color.setOnClickListener {
            DialogColor().show(parentFragmentManager, null)
        }

        binding.dateDeadline.setOnClickListener {
            DialogCalendar().show(parentFragmentManager, null)
        }

        binding.save.setOnClickListener {
            viewModel.addProject(
                name = binding.name.text.toString(),
                tasks = listOf(),
                prize = binding.prize.text.toString(),
                deadline = 1,
                color = colorId
            )
        }

//        binding.addTask.setOnClickListener {
//            viewModel.addTask(binding.nameTask.text.toString())
//        }

        viewModel.tasks.observe(viewLifecycleOwner, {
            tasksRecyclerView(it)
        })
    }

    private fun tasksRecyclerView(tasks: List<Task>) {
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterTask = AdapterTask( viewModel::updateTask)
        binding.tasksRecyclerView.apply {
            layoutManager = llm
            adapter = adapterTask
        }

        adapterTask.items = tasks
    }

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }

    companion object {
        fun newInstance(id: Long) = AddProjectFragment().apply {
            arguments = Bundle().apply {
                putLong("id_project", id)
            }
        }
    }
}