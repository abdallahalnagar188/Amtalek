package eramo.amtalek.presentation.ui.main.searchProperty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchPropertyResultBinding
import eramo.amtalek.presentation.adapters.dummy.RvMyFavouritesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class SearchPropertyResultFragment : BindingFragment<FragmentSearchPropertyResultBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchPropertyResultBinding::inflate

    @Inject
    lateinit var rvMyFavouritesAdapter: RvMyFavouritesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        binding.apply {
//            rvMyFavouritesAdapter.submitList(Dummy.list())
            rvProperties.adapter = rvMyFavouritesAdapter
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

}