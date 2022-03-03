package ru.harlion.pomodorolist.ui.projects.detail_project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.databinding.FragmentDetailProjectBinding
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.ui.tasks.AdapterTask


class DetailProjectFragment : BindingFragment<FragmentDetailProjectBinding>(FragmentDetailProjectBinding::inflate) {

   private lateinit var adapterTask: AdapterTask
   private val viewModel: DetailProjectViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addTask.setOnClickListener {
            viewModel.addTask(binding.nameTask.text.toString())
        }

        viewModel.tasks.observe(viewLifecycleOwner, {
            tasksRecyclerView(it)
        })
    }

    private fun tasksRecyclerView(tasks: List<Task>) {
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterTask = AdapterTask()
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
        fun newInstance(id : Long) = DetailProjectFragment().apply {
            arguments = Bundle().apply {
                putLong("id_project" , id)
            }
        }
    }
}