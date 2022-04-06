package ru.harlion.pomodorolist.ui.projects.lists_projects


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentListProjectsBinding
import ru.harlion.pomodorolist.models.ProjectWithProgress
import ru.harlion.pomodorolist.ui.pomodoro.TimerFragment
import ru.harlion.pomodorolist.ui.projects.adapter.ProjectsAdapter
import ru.harlion.pomodorolist.ui.projects.adding.AddProjectFragment
import ru.harlion.pomodorolist.ui.projects.detail_project.DetailProjectFragment
import ru.harlion.pomodorolist.utils.Prefs
import ru.harlion.pomodorolist.utils.replaceFragment


class ListProjectsFragment :
    BindingFragment<FragmentListProjectsBinding>(FragmentListProjectsBinding::inflate) {

    private lateinit var adapterProject: ProjectsAdapter
    private val viewModel: ListProjectsViewModel by viewModels()
    private lateinit var prefs : Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = Prefs(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterProject =
            ProjectsAdapter(
                prefs, { requireParentFragment().replaceFragment(DetailProjectFragment.newInstance(it), true) },
                viewModel::getListTasks,
                viewModel::updateTask
            ) {
                requireParentFragment().replaceFragment(TimerFragment(), true)
            }
        binding.listProject.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterProject
        }

        initClicks()

        viewModel.projects.observe(viewLifecycleOwner, {
            projectsRecyclerView(it)
        })

        prefs.observeTaskId().observe(viewLifecycleOwner, {
             adapterProject.currentTaskId = it
        })
    }

    private fun projectsRecyclerView(projects: List<ProjectWithProgress>) {
        if (projects.isNotEmpty()) {
            adapterProject.setItems(projects)

            binding.listProject.visibility = View.VISIBLE
            binding.emptyList.visibility = View.GONE
        } else {
            binding.listProject.visibility = View.GONE
            binding.emptyList.visibility = View.VISIBLE
        }
    }

    private fun initClicks() {
//        binding.addBtn.setOnClickListener {
//            replaceFragment(AddProjectFragment(), true)
//        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppActivity).updateNavigation(this)
    }
}