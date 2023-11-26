package eramo.amtalek.presentation.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMyOffersBinding
import eramo.amtalek.presentation.adapters.recyclerview.DummyOfferAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class MyOffersFragment : BindingFragment<FragmentMyOffersBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyOffersBinding::inflate

    @Inject
    lateinit var dummyOfferAdapter: DummyOfferAdapter
    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.whiteWithBackground(requireActivity(), R.color.amtalek_blue_dark)
        setupToolbar()

        binding.apply {
            dummyOfferAdapter.submitList(Dummy.list())
            rvOffers.adapter = dummyOfferAdapter
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