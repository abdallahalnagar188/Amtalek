package eramo.amtalek.presentation.ui.main.home.seemore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.property.allproperty.DataX
import eramo.amtalek.databinding.FragmentSeeMorePropertiesBinding
import eramo.amtalek.domain.model.main.home.PropertyModelx
import eramo.amtalek.presentation.adapters.recyclerview.RvPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellAndRentFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.properties.PropertyDetailsSellFragmentArgs
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.navOptionsAnimation
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMorePropertiesFragment : BindingFragment<FragmentSeeMorePropertiesBinding>(),
    RvPropertiesAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesBinding::inflate

//    private val args by navArgs<SeeMorePropertiesFragmentArgs>()
//    private val propertiesList get() = args.propertiesList
//    private val title get() = args.title

    val viewModel: HomeMyViewModel by viewModels()
    @Inject
    lateinit var rvPropertiesAdapter: RvPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllProperty()
        lifecycleScope.launch {
            viewModel.allProperty.collect {
//                Log.e("elnagar", it?.data?.original?.data.toString())
//                rvPropertiesAdapter.submitList(it?.data?.original?.data)
//                binding.rvProperties.adapter = rvPropertiesAdapter
                it?.data?.original?.data?.let { it1 -> setupViews(it1) }
            }
        }
    }

    private fun setupViews(list:List<DataX> ) {
        setupToolbar()

        setupRv(list)
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
 //           tvTitle.text = "Featured Properties in Egypt"
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }


    private fun setupRv(data: List<DataX>) {
        rvPropertiesAdapter.setListener(this@SeeMorePropertiesFragment)
        binding.rvProperties.adapter = rvPropertiesAdapter
        rvPropertiesAdapter.submitList(data)
    }

    override fun onPropertyClick(model: DataX) {
        when (model.forWhat) {
            PropertyType.FOR_SELL.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listingNumber?.let { PropertyDetailsFragmentArgs(it).toBundle() }
                )
            }

            PropertyType.FOR_RENT.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listingNumber?.let { PropertyDetailsFragmentArgs(it).toBundle() }
                )
            }

            PropertyType.FOR_BOTH.key -> {
                findNavController().navigate(
                    R.id.propertyDetailsFragment,
                    model.listingNumber?.let { PropertyDetailsFragmentArgs(it).toBundle() }
                )
            }
        }
    }
}