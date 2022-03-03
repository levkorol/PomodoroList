package ru.harlion.pomodorolist.ui.projects.adding


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentAddTaskBinding
import ru.harlion.pomodorolist.ui.projects.detail_project.DetailProjectFragment
import ru.harlion.pomodorolist.utils.replaceFragment

class AddProjectFragment : BindingFragment<FragmentAddTaskBinding>(FragmentAddTaskBinding::inflate) {

    private val viewModel: AddProjectViewModel by viewModels()
    private var projectId = -1L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.id.observe(viewLifecycleOwner, {
            projectId = it
        })

        binding.save.setOnClickListener {
            viewModel.addProject(
                name = binding.name.text.toString(),
                tasks = listOf(),
                prize = binding.prize.text.toString(),
                deadline = 1
            ) {
                replaceFragment(DetailProjectFragment.newInstance(projectId), true)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }
}