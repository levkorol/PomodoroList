package ru.harlion.pomodorolist.ui.projects.lists_projects.archive


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentArchiveProjectBinding
import ru.harlion.pomodorolist.models.ProjectWithProgress
import ru.harlion.pomodorolist.ui.pomodoro.TimerFragment
import ru.harlion.pomodorolist.ui.projects.adapter.ProjectsAdapter
import ru.harlion.pomodorolist.ui.projects.detail_project.DetailProjectFragment
import ru.harlion.pomodorolist.utils.replaceFragment


class ArchiveProjectFragment :
    BindingFragment<FragmentArchiveProjectBinding>(FragmentArchiveProjectBinding::inflate) {

    private lateinit var adapterProject: ProjectsAdapter
    private val viewModel: ArchiveProjectViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterProject =
            ProjectsAdapter(
                { requireParentFragment().replaceFragment(DetailProjectFragment.newInstance(it), true) },
                viewModel::getListTasks,
                viewModel::updateTask
            ) {
                requireParentFragment().replaceFragment(TimerFragment(), true)
            }
        binding.recyclerArchive.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterProject
        }

        viewModel.project.observe(
            viewLifecycleOwner, { projects ->
                projectsRecyclerView(projects)
            })
    }

    private fun projectsRecyclerView(projects: List<ProjectWithProgress>) {

        if (projects.isNotEmpty()) {
            adapterProject.setItems(projects)
            adapterProject.notifyDataSetChanged()
            binding.recyclerArchive.visibility = View.VISIBLE
            binding.emptyListProject.visibility = View.GONE
        } else {
            binding.recyclerArchive.visibility = View.GONE
            binding.emptyListProject.visibility = View.VISIBLE
        }
    }
}