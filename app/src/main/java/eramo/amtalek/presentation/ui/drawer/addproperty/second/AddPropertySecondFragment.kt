package eramo.amtalek.presentation.ui.drawer.addproperty.second

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddPropertySecondBinding
import eramo.amtalek.domain.model.property.addpropertymodels.AddPropertySecondModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.drawer.addproperty.third.AddPropertyThirdFragmentArgs
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromBottomAnimation


class AddPropertySecondFragment : BindingFragment<FragmentAddPropertySecondBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertySecondBinding::inflate
    val args by navArgs<AddPropertySecondFragmentArgs>()
    private val firstModel get() = args.firstModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        setupViews()
        clickListeners()
        loadPropertyTypes()
        loadRentDurations()
        Log.e("ARGGG", firstModel.toString(), )
    }


    private fun setupViews() {
        binding.apply {
            sellPriceTv.visibility = View.GONE
            etSellPrice.visibility = View.GONE
            tilSellPriceTv.visibility = View.GONE

            rentPriceTv.visibility = View.GONE
            etRentPrice.visibility = View.GONE
            tilRentPriceTv.visibility = View.GONE

            rentDurationTv.visibility = View.GONE
            autoCompleteEtRentDuration.visibility = View.GONE
            tilRentDurationTv.visibility = View.GONE
        }
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
                offerSpinner.error = getString(R.string.please_provide_your_offer_type)
                isValid = false
            }
            if (sellPriceTv.visibility == View.VISIBLE&&etSellPrice.text.isNullOrEmpty()){
                etSellPrice.error = getString(R.string.please_enter_a_sell_price)
                isValid = false
            }
            if (rentPriceTv.visibility == View.VISIBLE&&etRentPrice.text.isNullOrEmpty()){
                etSellPrice.error = getString(R.string.please_enter_a_rent_price)
                isValid = false
            }
            if (rentDurationTv.visibility == View.VISIBLE&&autoCompleteEtRentDuration.text.isNullOrEmpty()){
                autoCompleteEtRentDuration.error = getString(R.string.please_enter_a_rent_duration)
                isValid = false
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
    private fun loadRentDurations() {
        val offerTypes = resources.getStringArray(R.array.durations_array)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, offerTypes)
        binding.autoCompleteEtRentDuration.setAdapter(arrayAdapter)
        binding.autoCompleteEtRentDuration.inputType = InputType.TYPE_NULL
    }
    private fun clickListeners() {
        binding.btnNext.setOnClickListener(){
            if (formValidation()){
                val secondModel = AddPropertySecondModel(
                    addPropertyFirstModel = firstModel,
                    bathroomNumber = binding.etBathroomsNumber.text.toString().toInt(),
                    buildingNumber = binding.etBuildingNumber.text.toString().toInt(),
                    floorNumber = binding.etFloorNumber.text.toString().toInt(),
                    kitchenNumber = binding.etKitchensNumber.text.toString().toInt(),
                    livingRoomNumber = binding.etLivingRoom.text.toString().toInt(),
                    unitNumber = binding.etUnitNumber.text.toString().toInt(),
                    totalArea = binding.etTotalArea.text.toString().toInt(),
                    receptionPiecesNumber = binding.etReceptionPieces.text.toString().toInt(),
                    forWhat = binding.autoCompleteOfferType.text.toString(),
                    rentPrice = if (binding.rentPriceTv.visibility == View.VISIBLE) binding.etRentPrice.text.toString().toInt() else null,
                    sellPrice = if (binding.sellPriceTv.visibility == View.VISIBLE) binding.etSellPrice.text.toString().toInt() else null,
                    rentDuration =  if (binding.rentDurationTv.visibility == View.VISIBLE) binding.autoCompleteEtRentDuration.text.toString() else null
                )
                findNavController().navigate(R.id.addPropertyThirdFragment,AddPropertyThirdFragmentArgs(secondModel).toBundle(), navOptionsAnimation())
            }else{
                return@setOnClickListener
            }

        }
        binding.btnCancel.setOnClickListener(){
            findNavController().navigate(R.id.homeFragment,null, navOptionsFromBottomAnimation())
        }
        binding.apply {
            autoCompleteOfferType.setOnItemClickListener { parent, view, position, id ->
                if (binding.autoCompleteOfferType.text.toString() == context?.getString(R.string.sell)){
                    sellPriceTv.visibility = View.VISIBLE
                    etSellPrice.visibility = View.VISIBLE
                    tilSellPriceTv.visibility = View.VISIBLE

                    rentPriceTv.visibility = View.GONE
                    etRentPrice.visibility = View.GONE
                    tilRentPriceTv.visibility = View.GONE

                    rentDurationTv.visibility = View.GONE
                    autoCompleteEtRentDuration.visibility = View.GONE
                    tilRentDurationTv.visibility = View.GONE

                }else if (binding.autoCompleteOfferType.text.toString() == context?.getString(R.string.rentt)){

                    sellPriceTv.visibility = View.GONE
                    etSellPrice.visibility = View.GONE
                    tilSellPriceTv.visibility = View.GONE

                    rentPriceTv.visibility = View.VISIBLE
                    etRentPrice.visibility = View.VISIBLE
                    tilRentPriceTv.visibility = View.VISIBLE

                    rentDurationTv.visibility = View.VISIBLE
                    autoCompleteEtRentDuration.visibility = View.VISIBLE
                    tilRentDurationTv.visibility = View.VISIBLE
                }else if (binding.autoCompleteOfferType.text.toString() == context?.getString(R.string.rent_buy)){
                    sellPriceTv.visibility = View.VISIBLE
                    etSellPrice.visibility = View.VISIBLE
                    tilSellPriceTv.visibility = View.VISIBLE

                    rentPriceTv.visibility = View.VISIBLE
                    etRentPrice.visibility = View.VISIBLE
                    tilRentPriceTv.visibility = View.VISIBLE

                    rentDurationTv.visibility = View.VISIBLE
                    autoCompleteEtRentDuration.visibility = View.VISIBLE
                    tilRentDurationTv.visibility = View.VISIBLE
                }
            }
        }
    }

}