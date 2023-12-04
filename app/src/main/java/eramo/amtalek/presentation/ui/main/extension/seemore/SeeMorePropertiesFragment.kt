package eramo.amtalek.presentation.ui.main.extension.seemore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSeeMorePropertiesBinding
import eramo.amtalek.domain.model.main.home.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.RvPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.extension.PropertyDetailsRentFragmentArgs
import eramo.amtalek.presentation.ui.main.extension.PropertyDetailsSellAndRentFragmentArgs
import eramo.amtalek.presentation.ui.main.extension.PropertyDetailsSellFragmentArgs
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class SeeMorePropertiesFragment : BindingFragment<FragmentSeeMorePropertiesBinding>(),
    RvPropertiesAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesBinding::inflate

    private val args by navArgs<SeeMorePropertiesFragmentArgs>()
    private val propertiesList get() = args.propertiesList
    private val title get() = args.title

    @Inject
    lateinit var rvPropertiesAdapter: RvPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()

        setupRv(propertiesList.toList())
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = title
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }


    private fun setupRv(data: List<PropertyModel>) {
        rvPropertiesAdapter.setListener(this@SeeMorePropertiesFragment)
        binding.rvProperties.adapter = rvPropertiesAdapter
        rvPropertiesAdapter.submitList(data)
    }

    override fun onPropertyClick(model: PropertyModel) {
        when (model.type) {
            PropertyType.FOR_SELL.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellFragment,
                    PropertyDetailsSellFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_RENT.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsRentFragment,
                    PropertyDetailsRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }

            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsSellAndRentFragment,
                    PropertyDetailsSellAndRentFragmentArgs(model.id.toString()).toBundle(),
                    navOptionsAnimation()
                )
            }
        }
    }
}