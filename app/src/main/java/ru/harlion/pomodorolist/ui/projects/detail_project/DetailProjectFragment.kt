package ru.harlion.pomodorolist.ui.projects.detail_project

import android.os.Bundle
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentDetailProjectBinding


class DetailProjectFragment : BindingFragment<FragmentDetailProjectBinding>(FragmentDetailProjectBinding::inflate) {


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