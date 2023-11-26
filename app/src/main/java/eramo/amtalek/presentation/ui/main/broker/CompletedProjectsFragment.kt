package eramo.amtalek.presentation.ui.main.broker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentCompletedProjectsBinding
import eramo.amtalek.domain.model.drawer.latestprojects.LatestProjectsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvCompletedProjectsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvLatestProjectsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class CompletedProjectsFragment : BindingFragment<FragmentCompletedProjectsBinding>(),RvCompletedProjectsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentCompletedProjectsBinding::inflate

    @Inject
    lateinit var rvCompletedProjectsAdapter: RvCompletedProjectsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()
        initRv(Dummy.dummyLatestProjectsList())
    }

    private fun setupToolbar() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        binding.apply {
            inToolbar.tvTitle.text = getString(R.string.s_completed_projects, "122")
            inToolbar.ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initRv(data: List<LatestProjectsModel>) {
        rvCompletedProjectsAdapter.setListener(this@CompletedProjectsFragment)
        binding.rv.adapter = rvCompletedProjectsAdapter
        rvCompletedProjectsAdapter.submitList(data)
    }

    override fun onProjectClick(model: LatestProjectsModel) {

    }
}