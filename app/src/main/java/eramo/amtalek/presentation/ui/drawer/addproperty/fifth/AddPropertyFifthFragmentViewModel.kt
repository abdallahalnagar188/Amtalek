package eramo.amtalek.presentation.ui.drawer.addproperty.fifth

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eramo.amtalek.data.remote.dto.property.newResponse.addproperty.AddPropertyResponse
import eramo.amtalek.domain.model.project.AmenityModel
import eramo.amtalek.domain.model.property.CriteriaModel
import eramo.amtalek.domain.repository.AddPropertyRepository
import eramo.amtalek.domain.repository.certaria.PropertyAmenitiesRepository
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddPropertyFifthFragmentViewModel @Inject constructor(
    private val propertyAmenitiesRepository: PropertyAmenitiesRepository,
    private val addPropertyRepository: AddPropertyRepository,
    @ApplicationContext private val context: Context
):ViewModel(){

    var _propertyAmenitiesState: MutableStateFlow<UiState<List<AmenityModel>>> =MutableStateFlow(UiState.Empty())
    val propertyAmenitiesState: StateFlow<UiState<List<AmenityModel>>> = _propertyAmenitiesState

    var _addPropertyState: MutableStateFlow<UiState<AddPropertyResponse>> =MutableStateFlow(UiState.Empty())
    val addPropertyState: StateFlow<UiState<AddPropertyResponse>> = _addPropertyState

    private var propertyAmenitiesJob:Job? = null
    private var addPropertyJob:Job?=null


    fun getPropertyAmenities(){
        propertyAmenitiesJob?.cancel()
        propertyAmenitiesJob = viewModelScope.launch {
            withContext(coroutineContext){
                val response = propertyAmenitiesRepository.getAmenities()
                response.collect(){result->
                    when(result){
                        is Resource.Success->{
                            val list:MutableList<AmenityModel> = ArrayList()
                            for (item in result.data?.data!!){
                                if (item != null) {
                                    list.add(item.toAmenityModel())
                                }
                            }
                            _propertyAmenitiesState.value = UiState.Success(list)
                        }
                        is Resource.Error->{
                            _propertyAmenitiesState.value = UiState.Error(result.message!!)
                        }
                        is Resource.Loading->{
                            _propertyAmenitiesState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun addProperty(
        priority: String?,
        purpose: Int?,
        category: Int?,
        propertyType: Int?,
        countryId: Int?,
        cityId: Int?,
        regionId: Int?,
        subRegionId: Int?,
        buildingNum: Int?,
        floorNum: Int?,
        noFloors: Int?,
        apartmentNum: Int?,
        currency: String?,
        totalArea: Int?,
        receptionPieces: Int?,
        bedRoomsNo: Int?,
        bathRoomNo: Int?,
        livingRoom: Int?,
        kitchensNo: Int?,
        finishing: Int?,
        receptionFloorType: Int?,
        video: String?,
        location: String?,
        propertyTitleEn: String?,
        propertyTitleAr: String?,
        propertyDescriptionEn: String?,
        propertyDescriptionAr: String?,
        addressEn: String?,
        addressAr: String?,
        onSite: String?,
        forWhat: String?,
        salePrice: Int?,
        rentPrice: Int?,
        rentDuration: String?,
        amenities: String?,
        primaryImage: Uri?,
        sliders: List<Uri>?
    ){
        addPropertyJob?.cancel()
        addPropertyJob = viewModelScope.launch {
            withContext(coroutineContext){
                addPropertyRepository.addProperty(
                    priority = convertToRequestBody(priority),
                    purpose = convertToRequestBody(purpose.toString()),
                    category = convertToRequestBody(category.toString()),
                    propertyType = convertToRequestBody(propertyType.toString()),
                    countryId = convertToRequestBody(countryId.toString()),
                    cityId = convertToRequestBody(cityId.toString()),
                    regionId = convertToRequestBody(regionId.toString()),
                    subRegionId = convertToRequestBody(subRegionId.toString()),
                    buildingNum = convertToRequestBody(buildingNum.toString()),
                    floorNum = convertToRequestBody(floorNum.toString()),
                    noFloors = convertToRequestBody(noFloors.toString()),
                    apartmentNum = convertToRequestBody(apartmentNum.toString()),
                    currency = convertToRequestBody(currency),
                    totalArea = convertToRequestBody(totalArea.toString()),
                    receptionPieces = convertToRequestBody(receptionPieces.toString()),
                    bedRoomsNo = convertToRequestBody(bedRoomsNo.toString()),
                    bathRoomNo = convertToRequestBody(bathRoomNo.toString()),
                    livingRoom = convertToRequestBody(livingRoom.toString()),
                    kitchensNo = convertToRequestBody(kitchensNo.toString()),
                    finishing = convertToRequestBody(finishing.toString()),
                    receptionFloorType = convertToRequestBody(receptionFloorType.toString()),
                    video = convertToRequestBody(video),
                    location = convertToRequestBody(location),
                    propertyTitleEn = convertToRequestBody(propertyTitleEn),
                    propertyTitleAr = convertToRequestBody(propertyTitleAr),
                    propertyDescriptionEn = convertToRequestBody(propertyDescriptionEn),
                    propertyDescriptionAr = convertToRequestBody(propertyDescriptionAr),
                    addressEn = convertToRequestBody(addressEn),
                    addressAr = convertToRequestBody(addressAr),
                    onSite = convertToRequestBody(onSite),
                    forWhat = convertToRequestBody(forWhat),
                    salePrice = convertToRequestBody(salePrice.toString()),
                    rentPrice = convertToRequestBody(rentPrice.toString()),
                    rentDuration = convertToRequestBody(rentDuration.toString()),
                    amenities = convertToRequestBody(amenities),
                    primaryImage = convertFileToMultipart("primary_image",primaryImage)!!,
                    sliders = convertFilesToMultipart(context,"sliders[]",sliders!!)
                ).collect(){
                    when(it){
                        is Resource.Success->{
                            _addPropertyState.value = UiState.Success(it.data!!)
                        }
                        is Resource.Error->{
                            _addPropertyState.value = UiState.Error(it.message!!)
                        }
                        is Resource.Loading->{
                            _addPropertyState.value = UiState.Loading()
                        }
                    }
                }
            }
        }
    }

    fun convertToRequestBody(part: String?): RequestBody? {
        return try {
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), part!!)
        } catch (e: Exception) {
            null
        }

    }

    private fun convertFileToMultipart(img_keyName: String, imageUri: Uri?): MultipartBody.Part? {
        return if (imageUri != null) {
            val file = File(imageUri.path!!)
            val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData(img_keyName, file.name, requestBody)
        } else null
    }
    private fun convertFilesToMultipart(context: Context, img_keyName: String, imageUris: List<Uri>?): List<MultipartBody.Part>? {
        return imageUris?.mapNotNull { imageUri ->
            val filePath = getRealPathFromUri(context, imageUri)
            if (filePath != null) {
                val file = File(filePath)
                val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData(img_keyName, file.name, requestBody)
            } else {
                null
            }
        }
    }
    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        var realPath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            realPath = cursor.getString(columnIndex)
            cursor.close()
        }
        return realPath
    }

//    private fun convertFilesToMultipart(img_keyName: String, imageUris: List<Uri>?): List<MultipartBody.Part>? {
//        return imageUris?.mapNotNull { imageUri ->
//            val file = File(imageUri.path!!)
//            val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
//            MultipartBody.Part.createFormData(img_keyName, file.name, requestBody)
//        }
//    }
}