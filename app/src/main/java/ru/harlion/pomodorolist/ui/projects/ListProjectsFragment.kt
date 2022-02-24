package ru.harlion.pomodorolist.ui.projects


import android.os.Bundle
import android.view.View
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentListProjectsBinding
import ru.harlion.pomodorolist.ui.adding.AddProjectFragment
import ru.harlion.pomodorolist.utils.replaceFragment


class ListProjectsFragment : BindingFragment<FragmentListProjectsBinding>(FragmentListProjectsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()
    }

    private fun initClicks() {
        binding.addBtn.setOnClickListener {
            replaceFragment(AddProjectFragment(), true)
        }
    }
}