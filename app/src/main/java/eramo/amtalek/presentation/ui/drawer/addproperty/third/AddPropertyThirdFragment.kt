package eramo.amtalek.presentation.ui.drawer.addproperty.third

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddPropertyThirdBinding
import eramo.amtalek.presentation.ui.BindingFragment

@AndroidEntryPoint
class AddPropertyThirdFragment : BindingFragment<FragmentAddPropertyThirdBinding>(){
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertyThirdBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        clickListeners()
    }

    private fun clickListeners() {
        binding.btnNext.setOnClickListener {
            if (formValidation()){
                
            }else{
                return@setOnClickListener
            }
        }
    }

    private fun setUpViews() {
        binding.propertyCityTv.visibility = View.GONE
        binding.citySpinner.visibility = View.GONE
        binding.propertyRegionTv.visibility = View.GONE
        binding.regionSpinner.visibility = View.GONE
        binding.propertySubRegionTv.visibility = View.GONE
        binding.subRegionSpinner.visibility = View.GONE

        binding.autoCompleteSubRegion.inputType = InputType.TYPE_NULL
        binding.autoCompleteRegion.inputType = InputType.TYPE_NULL
        binding.autoCompleteCity.inputType = InputType.TYPE_NULL
        binding.autoCompleteCountry.inputType = InputType.TYPE_NULL
        binding.autoCompletePurpose.inputType = InputType.TYPE_NULL
        binding.autoCompletePriorityType.inputType = InputType.TYPE_NULL
        binding.autoCompleteFinishing.inputType = InputType.TYPE_NULL
        binding.autoCompleteFloorFinishingType.inputType = InputType.TYPE_NULL
        binding.autoCompleteType.inputType = InputType.TYPE_NULL

        setupToolBar()
    }
    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.add_property_3_5)
            ivBack.setOnClickListener { findNavController().popBackStack()}
        }
    }

    private fun formValidation():Boolean{
        var isValid = true
        binding.apply {
            if (autoCompletePurpose.text.toString().isEmpty()){
                autoCompletePurpose.error = getString(R.string.please_select_purpose)
               isValid = false
            }
            if (autoCompleteCategoryType.text.toString().isEmpty()){
                autoCompleteCategoryType.error = getString(R.string.please_select_category_type)
                isValid = false
            }
            if (autoCompletePriorityType.text.toString().isEmpty()){
                autoCompletePriorityType.error = getString(R.string.please_select_priority_type)
                isValid = false
            }
            if (autoCompleteFinishing.text.toString().isEmpty()){
                autoCompleteFinishing.error = getString(R.string.please_select_finishing)
                isValid = false
            }
            if (autoCompleteFloorFinishingType.text.toString().isEmpty()){
                autoCompleteFloorFinishingType.error = getString(R.string.please_select_floor_finishing_type)
                isValid = false
            }
            if (autoCompleteType.text.toString().isEmpty()){
                autoCompleteType.error = getString(R.string.please_select_type)
                isValid = false
            }
            if (autoCompleteCountry.text.toString().isEmpty()){
                autoCompleteCountry.error = getString(R.string.please_select_country)
                isValid = false
            }
            if (autoCompleteCity.text.toString().isEmpty()){
                autoCompleteCity.error = getString(R.string.please_select_city)
                isValid = false
            }
            if (autoCompleteRegion.text.toString().isEmpty()){
                autoCompleteRegion.error = getString(R.string.please_select_region)
                isValid = false
            }
            if (autoCompleteSubRegion.text.toString().isEmpty()){
                autoCompleteSubRegion.error = getString(R.string.please_select_sub_region)
                isValid = false
            }
            autoCompletePurpose.setOnItemClickListener { parent, view, position, id ->
                autoCompletePurpose.error = null
            }
            autoCompleteCategoryType.setOnItemClickListener { parent, view, position, id ->
                autoCompleteCategoryType.error = null

            }
            autoCompletePriorityType.setOnItemClickListener { parent, view, position, id ->
                autoCompletePriorityType.error = null

            }
            autoCompleteFinishing.setOnItemClickListener { parent, view, position, id ->
                autoCompleteFinishing.error = null

            }
            autoCompleteFloorFinishingType.setOnItemClickListener { parent, view, position, id ->
                autoCompleteFloorFinishingType.error = null

            }
            autoCompleteType.setOnItemClickListener { parent, view, position, id ->
                autoCompleteType.error = null

            }
            autoCompleteCountry.setOnItemClickListener { parent, view, position, id ->
                autoCompleteCountry.error = null

            }
            autoCompleteCity.setOnItemClickListener { parent, view, position, id ->
                autoCompleteCity.error = null

            }
            autoCompleteRegion.setOnItemClickListener { parent, view, position, id ->
                autoCompleteRegion.error = null

            }
            autoCompleteSubRegion.setOnItemClickListener { parent, view, position, id ->
                autoCompleteSubRegion.error = null

            }
        }
        return isValid
    }

}