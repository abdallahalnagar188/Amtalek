package eramo.amtalek.presentation.ui.drawer.addproperty.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddPropertyFirstBinding
import eramo.amtalek.domain.model.property.addpropertymodels.AddPropertyFirstModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.addproperty.second.AddPropertySecondFragmentArgs
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromBottomAnimation

class AddPropertyFirstFragment :BindingFragment<FragmentAddPropertyFirstBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertyFirstBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        clickListeners()
    }

    private fun clickListeners() {
        binding.btnNext.setOnClickListener(){
            if (formValidation()){
                binding.apply {
                    val addPropertyFirstModel = AddPropertyFirstModel(propertyNameInEnglish = etPropertyNameEnglish.text.toString(), propertyNameInArabic = etPropertyNameArabic.text.toString(), propertyAddressInEnglish = etPropertyAddressEnglish.text.toString(),
                        propertyAddressInArabic = etPropertyAddressArabic.text.toString(), propertyLocation = etPropertyLocation.text.toString(), propertyVideoUrl =etPropertyVideoLink.text.toString() )
                    findNavController().navigate(R.id.addPropertySecondFragment, AddPropertySecondFragmentArgs(addPropertyFirstModel).toBundle(), navOptionsAnimation())
                }
            }else{
                return@setOnClickListener
            }

        }
        binding.btnCancel.setOnClickListener(){
           findNavController().navigate(R.id.homeFragment,null, navOptionsFromBottomAnimation())
        }
    }

    private fun formValidation(): Boolean {
        var isValid = true
        binding.apply {
            if (etPropertyNameEnglish.text.isNullOrEmpty()){
                etPropertyNameEnglish.error = getString(R.string.please_enter_a_name)
                isValid = false
            }
            if (etPropertyNameArabic.text.isNullOrEmpty()){
                etPropertyNameArabic.error = getString(R.string.please_enter_a_name)
                isValid = false
            }
            if (etPropertyAddressEnglish.text.isNullOrEmpty()){
                etPropertyAddressEnglish.error = getString(R.string.please_enter_a_address)
                isValid = false
            }
            if (etPropertyAddressArabic.text.isNullOrEmpty()){
                etPropertyAddressArabic.error =  getString(R.string.please_enter_a_address)
            }
            if (etPropertyLocation.text.isNullOrEmpty()){
                //etPropertyLocation.error = getString(R.string.please_enter_a_property_location)
                isValid = true
            }

        }
        return isValid
    }

    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.add_property_1_5)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

}