package eramo.amtalek.presentation.ui.drawer.addproperty.fifth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddPropertyFifthBinding
import eramo.amtalek.domain.model.property.addpropertymodels.AddPropertyFourthModel
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.main.home.details.projects.AmenitiesAdapter
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddPropertyFifthFragment : BindingFragment<FragmentAddPropertyFifthBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertyFifthBinding::inflate
    val args by navArgs<AddPropertyFifthFragmentArgs>()
    private val fourthModel get() = args.fourthModel

    val viewModel by viewModels<AddPropertyFifthFragmentViewModel>()
    @Inject
    lateinit var amenitiesAdapter: SelectAmenitiesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        requestData()
        fetchData()
        clickListeners()
        Log.e("ARGGG4", fourthModel.toString(), )
    }

    private fun clickListeners() {
        binding.btnSubmit.setOnClickListener(){
            bindDataToRequestBody()
        }
    }

    private fun bindDataToRequestBody() {
        viewModel.addProperty(
            propertyTitleEn = fourthModel.addPropertyThirdModel.addPropertySecondModel.addPropertyFirstModel.propertyNameInEnglish,
            propertyTitleAr = fourthModel.addPropertyThirdModel.addPropertySecondModel.addPropertyFirstModel.propertyNameInArabic,
            addressEn = fourthModel.addPropertyThirdModel.addPropertySecondModel.addPropertyFirstModel.propertyAddressInEnglish,
            addressAr = fourthModel.addPropertyThirdModel.addPropertySecondModel.addPropertyFirstModel.propertyAddressInArabic,
            video = fourthModel.addPropertyThirdModel.addPropertySecondModel.addPropertyFirstModel.propertyVideoUrl,
            location = fourthModel.addPropertyThirdModel.addPropertySecondModel.addPropertyFirstModel.propertyLocation,
            buildingNum = fourthModel.addPropertyThirdModel.addPropertySecondModel.buildingNumber,
            floorNum = fourthModel.addPropertyThirdModel.addPropertySecondModel.floorNumber,
            noFloors = fourthModel.addPropertyThirdModel.addPropertySecondModel.floorNumber,
            apartmentNum = fourthModel.addPropertyThirdModel.addPropertySecondModel.buildingNumber,
            totalArea = fourthModel.addPropertyThirdModel.addPropertySecondModel.totalArea,
            livingRoom = fourthModel.addPropertyThirdModel.addPropertySecondModel.livingRoomNumber,
            receptionPieces = fourthModel.addPropertyThirdModel.addPropertySecondModel.receptionPiecesNumber,
            kitchensNo = fourthModel.addPropertyThirdModel.addPropertySecondModel.kitchenNumber,
            bathRoomNo = fourthModel.addPropertyThirdModel.addPropertySecondModel.bathroomNumber,
            bedRoomsNo = fourthModel.addPropertyThirdModel.addPropertySecondModel.bedroomNumber,
            forWhat = fourthModel.addPropertyThirdModel.addPropertySecondModel.forWhat,
            rentPrice = fourthModel.addPropertyThirdModel.addPropertySecondModel.rentPrice,
            salePrice = fourthModel.addPropertyThirdModel.addPropertySecondModel.sellPrice,
            rentDuration = fourthModel.addPropertyThirdModel.addPropertySecondModel.rentDuration,
            purpose = fourthModel.addPropertyThirdModel.purposeId,
            category = fourthModel.addPropertyThirdModel.categoryId,
            priority = fourthModel.addPropertyThirdModel.priorityId,
            propertyType = fourthModel.addPropertyThirdModel.propertyType,
            countryId = fourthModel.addPropertyThirdModel.countryId,
            cityId = fourthModel.addPropertyThirdModel.cityId,
            regionId = fourthModel.addPropertyThirdModel.regionId,
            subRegionId = fourthModel.addPropertyThirdModel.subRegionId,
            finishing = fourthModel.addPropertyThirdModel.finishingId,
            receptionFloorType = fourthModel.addPropertyThirdModel.floorFinishingId,
            primaryImage = fourthModel.primaryImage,
            sliders = fourthModel.sliderImages,
            propertyDescriptionAr = fourthModel.descriptionInArabic,
            propertyDescriptionEn = fourthModel.descriptionInEnglish,
            amenities = amenitiesAdapter.selectionList.toString(),
            currency = "1",//static so far
            onSite = "yes"//static
        )
    }

    private fun fetchData() {
        fetchGetAmenitiesState()
        fetchAddPropertyState()
    }

    private fun fetchAddPropertyState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.addPropertyState.collect(){
                    when(it){
                        is UiState.Success->{
                            Toast.makeText(requireContext(), "Property added successfully", Toast.LENGTH_SHORT).show()
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Error->{
                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Empty->Unit
                    }
                }
            }
        }
    }

    private fun fetchGetAmenitiesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.propertyAmenitiesState.collect(){
                    when (it){
                        is UiState.Success->{
                            amenitiesAdapter.saveData(it.data!!)
                            binding.amenitiesRv.adapter = amenitiesAdapter
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Error->{
                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Empty->Unit
                    }
                }
            }
        }
    }

    private fun requestData() {
        viewModel.getPropertyAmenities()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.add_property_5_5)
            ivBack.setOnClickListener { findNavController().popBackStack()}
        }
    }
}