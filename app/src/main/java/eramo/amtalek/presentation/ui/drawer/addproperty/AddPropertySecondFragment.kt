package eramo.amtalek.presentation.ui.drawer.addproperty

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddPropertySecondBinding
import eramo.amtalek.presentation.ui.BindingFragment


class AddPropertySecondFragment : BindingFragment<FragmentAddPropertySecondBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertySecondBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        clickListeners()
        loadPropertyTypes()
    }
    private fun formValidation(): Boolean {
        var isValid = true
        binding.apply {
            if (etBuildingNumber.text.isNullOrEmpty()){
                etBuildingNumber.error = getString(R.string.please_enter_a_building_number)
                isValid = false
            }
            if (etFloorNumber.text.isNullOrEmpty()){
                etFloorNumber.error = getString(R.string.please_enter_a_floor_number)
                isValid = false
            }
            if (etUnitNumber.text.isNullOrEmpty()){
                etUnitNumber.error = getString(R.string.please_enter_a_unit_number)
                isValid = false
            }
            if (etLivingRoom.text.isNullOrEmpty()){
                etLivingRoom.error =  getString(R.string.please_enter_a_address)
            }
            if (etTotalArea.text.isNullOrEmpty()){
                etTotalArea.error = getString(R.string.please_enter_the_total_area)
                isValid = false
            }
            if (etReceptionPieces.text.isNullOrEmpty()){
                etReceptionPieces.error = getString(R.string.please_enter_the_number_of_reception_pieces)
                isValid = false
            }
            if (etBathroomsNumber.text.isNullOrEmpty()){
                etBathroomsNumber.error = getString(R.string.please_enter_a_bathrooms_number)
                isValid = false
            }
            if (etBedroomsNumber.text.isNullOrEmpty()){
                etBedroomsNumber.error = getString(R.string.please_enter_a_bedrooms_number)
                isValid = false
            }
            if (etKitchensNumber.text.isNullOrEmpty()){
                etKitchensNumber.error = getString(R.string.please_enter_the_kitchens_number)
                isValid = false
            }
            if (autoCompleteOfferType.text.isNullOrEmpty()){
                autoCompleteOfferType.error = getString(R.string.please_select_a_property_type)
            }
        }
        return isValid
    }
    private fun setupToolBar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.add_property_2_5)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }
    private fun loadPropertyTypes() {
        val offerTypes = resources.getStringArray(R.array.property_type)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, offerTypes)
        binding.autoCompleteOfferType.setAdapter(arrayAdapter)
        binding.autoCompleteOfferType.inputType = InputType.TYPE_NULL
    }
    private fun clickListeners() {
        binding.btnNext.setOnClickListener(){
            if (formValidation()){

            }else{
                return@setOnClickListener
            }

        }
        binding.btnCancel.setOnClickListener(){
            findNavController().popBackStack()
        }
    }

}