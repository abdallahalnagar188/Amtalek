package eramo.amtalek.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentFilterDialogBinding
import eramo.amtalek.presentation.ui.search.searchform.SearchFormViewModel
import eramo.amtalek.presentation.ui.search.searchresult.SearchResultFragmentArgs
import eramo.amtalek.util.UserUtil

@AndroidEntryPoint
class FilterDialogFragment : DialogFragment() {

    private var binding: FragmentFilterDialogBinding? = null
    private val viewModel by viewModels<SearchFormViewModel>()
    // Remove the navArgs reference
    // val args: SearchResultFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {
        binding?.apply {
            // Setup any initial UI
        }
    }

    private fun listeners() {
        binding?.apply {
            ivClose.setOnClickListener {
                dialog?.dismiss()
            }

            btnSearch.setOnClickListener {
                // Extract values from the dialog inputs
                val mainKey = etMainKey.text.toString()
                val minArea = etMinimumSpace.text.toString()
                val maxArea = etMaximumSpace.text.toString()
                val lowerPrice = etLowestPrice.text.toString()
                val maxPrice = etHighestPrice.text.toString()

                // Pass these values to viewModel.search() (replace 'args' with your values)
                viewModel.search(
                    city = 0.toString(),  // Replace with an actual value
                    propertyType = "",
                    minPrice = lowerPrice,
                    maxPrice = maxPrice,
                    minArea = minArea,
                    maxArea = maxArea,
                    minBeds = null,
                    minBathes = null,
                    country = UserUtil.getUserCountryFiltrationTitleId(),
                    currency = null,
                    finishing = "",
                    keyword = mainKey,
                    priceArrangeKeys = null,
                    purpose = "",
                    region = null,
                    subRegion = null,
                    amenitiesListIds = null,
                    priority_keys = null
                )

                // Dismiss the dialog
                dialog?.dismiss()
            }
        }
    }
}
