package eramo.amtalek.presentation.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentLatestProjectsBinding
import eramo.amtalek.domain.model.drawer.latestprojects.LatestProjectsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvLatestProjectsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class LatestProjectsFragment : BindingFragment<FragmentLatestProjectsBinding>(), RvLatestProjectsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLatestProjectsBinding::inflate

    @Inject
    lateinit var rvLatestProjectsAdapter: RvLatestProjectsAdapter
//    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()

        initRv(Dummy.dummyLatestProjectsList())
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.latest_projects)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initRv(data: List<LatestProjectsModel>) {
        rvLatestProjectsAdapter.setListener(this@LatestProjectsFragment)
        binding.rvLatestProjects.adapter = rvLatestProjectsAdapter
        rvLatestProjectsAdapter.submitList(data)
    }

    override fun onProjectClick(model: LatestProjectsModel) {

    }
}