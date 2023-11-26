package eramo.amtalek.presentation.ui.main.extension.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAllPropertiesBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvMyFavouritesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.ShopViewModel
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class AllPropertiesFragment : BindingFragment<FragmentAllPropertiesBinding>(){

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAllPropertiesBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: ShopViewModel by viewModels()

    @Inject
    lateinit var rvMyFavouritesAdapter: RvMyFavouritesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

//        rvMyFavouritesAdapter.setListener(this)
        binding.apply {
//            rvMyFavouritesAdapter.submitList(Dummy.list())
            rvProperties.adapter = rvMyFavouritesAdapter

            ivFilter.setOnClickListener {
                findNavController().navigate(R.id.searchDialog)
            }
        }
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            toolbarIvMenu.setOnClickListener { viewModelShared.openDrawer.value = true }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

//    override fun onPropertyClick(model: String) {
//        findNavController().navigate(R.id.propertyDetailsFragment)
//    }

}