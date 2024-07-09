package eramo.amtalek.presentation.ui.search.searchform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSearchFormBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.search.searchform.locationspopup.AllLocationsPopUp

@AndroidEntryPoint
class SearchFormFragment : BindingFragment<FragmentSearchFormBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSearchFormBinding::inflate

    private val viewModel by viewModels<SearchFormViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        clickListeners()
    }

    private fun clickListeners() {
        binding.btnConfirm.setOnClickListener(){
            if (isValidForm()){
                // navigate to search results
            }
        }
        binding.locationSpinner.setOnClickListener(){
            val couponDialog = AllLocationsPopUp()
            couponDialog.show(
                activity?.supportFragmentManager!!,
                "extra"
            )
        }
    }
    private fun isValidForm():Boolean{
        var isValid = true
        if (binding.etMinArea.text.toString().toInt()>binding.etMaxArea.text.toString().toInt()){
            isValid = false
            Toast.makeText(requireContext(), getString(R.string.min_area_must_be_greater_than_max_area), Toast.LENGTH_SHORT).show()
        }
        if (binding.etMinPrice.text.toString().toInt()>binding.etMaxPrice.text.toString().toInt()){
            isValid = false
            Toast.makeText(requireContext(), getString(R.string.min_price_must_be_greater_than_max_price), Toast.LENGTH_SHORT).show()
        }




        return isValid
    }
    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.find_your_property)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

}