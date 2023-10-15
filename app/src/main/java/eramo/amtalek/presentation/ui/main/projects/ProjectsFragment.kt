package eramo.amtalek.presentation.ui.main.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentProjectsBinding
import eramo.amtalek.presentation.adapters.dummy.DummyProjectAdapter
import eramo.amtalek.presentation.adapters.dummy.DummyRealEstateAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.ShopViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class ProjectsFragment : BindingFragment<FragmentProjectsBinding>(),
    DummyProjectAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProjectsBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: ShopViewModel by viewModels()

    @Inject
    lateinit var dummyProjectAdapter: DummyProjectAdapter

    @Inject
    lateinit var dummyRealEstateAdapter: DummyRealEstateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        dummyProjectAdapter.setListener(this)
        binding.apply {
            dummyProjectAdapter.submitList(Dummy.list())
            rvProjects.adapter = dummyProjectAdapter

            dummyRealEstateAdapter.submitList(Dummy.list())
            rvRealEstates.adapter = dummyRealEstateAdapter
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
//            ivSearch.setOnClickListener { findNavController().navigate(R.id.searchPropertyFragment) }
            inNotification.root.setOnClickListener {
                findNavController().navigate(R.id.notificationFragment)
            }
        }
    }

    override fun onProductClick(model: String) {
        findNavController().navigate(R.id.projectDetailsFragment, null, navOptionsAnimation())
    }
}