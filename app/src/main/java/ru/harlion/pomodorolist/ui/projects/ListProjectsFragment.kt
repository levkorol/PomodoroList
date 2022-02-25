package ru.harlion.pomodorolist.ui.projects


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.databinding.FragmentListProjectsBinding
import ru.harlion.pomodorolist.ui.projects.adding.AddProjectFragment
import ru.harlion.pomodorolist.ui.projects.detail_project.DetailProjectFragment
import ru.harlion.pomodorolist.utils.replaceFragment


class ListProjectsFragment : BindingFragment<FragmentListProjectsBinding>(FragmentListProjectsBinding::inflate) {

    private lateinit var adapterProject: ProjectsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()

        projectsRecyclerView()
    }

    private fun projectsRecyclerView() {
         val llm = LinearLayoutManager(requireContext())
         llm.orientation = LinearLayoutManager.VERTICAL
        adapterProject = ProjectsAdapter {
            replaceFragment(DetailProjectFragment.newInstance(it), true)
        }
        binding.listProject.apply {
            layoutManager = llm
            adapter = adapterProject
        }

        val projects = Repository.getListProjects()
        if(projects.isNotEmpty()) {
            adapterProject.item = projects
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