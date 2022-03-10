package ru.harlion.pomodorolist.ui.projects


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.databinding.FragmentListProjectsBinding
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.ui.projects.adding.AddProjectFragment
import ru.harlion.pomodorolist.ui.projects.detail_project.DetailProjectFragment
import ru.harlion.pomodorolist.utils.replaceFragment


class ListProjectsFragment :
    BindingFragment<FragmentListProjectsBinding>(FragmentListProjectsBinding::inflate) {

    private lateinit var adapterProject: ProjectsAdapter
    private val viewModel: ListProjectsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()

        viewModel.projects.observe(
            viewLifecycleOwner, { projects ->
                projectsRecyclerView(projects)
            })
    }

    private fun projectsRecyclerView(projects: List<Project>) {
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterProject =
            ProjectsAdapter(
                { replaceFragment(DetailProjectFragment.newInstance(it), true) },
                viewModel::getListTasks
            )

        binding.listProject.apply {
            layoutManager = llm
            adapter = adapterProject
        }

        if (projects.isNotEmpty()) {
            adapterProject.item = projects
            adapterProject.notifyDataSetChanged()
            binding.listProject.visibility = View.VISIBLE
            binding.emptyList.visibility = View.GONE
        } else {
            binding.listProject.visibility = View.GONE
            binding.emptyList.visibility = View.VISIBLE
        }
    }

    private fun initClicks() {
        binding.addBtn.setOnClickListener {
            replaceFragment(AddProjectFragment(), true)
        }
    }
}