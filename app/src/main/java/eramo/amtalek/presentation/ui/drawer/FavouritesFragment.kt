package eramo.amtalek.presentation.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentFavouritesBinding
import eramo.amtalek.presentation.adapters.dummy.DummyPropertyPreviewAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class FavouritesFragment : BindingFragment<FragmentFavouritesBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentFavouritesBinding::inflate

    @Inject
    lateinit var dummyPropertyPreviewAdapter: DummyPropertyPreviewAdapter
    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        binding.apply {
            dummyPropertyPreviewAdapter.submitList(Dummy.list())
            rvProperties.adapter = dummyPropertyPreviewAdapter
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener {
                viewModelShared.openDrawer.value = true
            }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

}