package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.property.newResponse.addproperty.AddPropertyResponse
import eramo.amtalek.domain.repository.AddPropertyRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import javax.inject.Inject

class AddPropertyRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
):AddPropertyRepository{
    override suspend fun addProperty(
        priority: RequestBody?,
        purpose: RequestBody?,
        category: RequestBody?,
        propertyType: RequestBody?,
        countryId: RequestBody?,
        cityId: RequestBody?,
        regionId: RequestBody?,
        subRegionId: RequestBody?,
        buildingNum: RequestBody?,
        floorNum: RequestBody?,
        noFloors: RequestBody?,
        apartmentNum: RequestBody?,
        currency: RequestBody?,
        totalArea: RequestBody?,
        receptionPieces: RequestBody?,
        bedRoomsNo: RequestBody?,
        bathRoomNo: RequestBody?,
        livingRoom: RequestBody?,
        kitchensNo: RequestBody?,
        finishing: RequestBody?,
        receptionFloorType: RequestBody?,
        video: RequestBody?,
        location: RequestBody?,
        propertyTitleEn: RequestBody?,
        propertyTitleAr: RequestBody?,
        propertyDescriptionEn: RequestBody?,
        propertyDescriptionAr: RequestBody?,
        addressEn: RequestBody?,
        addressAr: RequestBody?,
        onSite: RequestBody?,
        forWhat: RequestBody?,
        salePrice: RequestBody?,
        rentPrice: RequestBody?,
        rentDuration: RequestBody?,
        amenities: RequestBody?,
        primaryImage: MultipartBody.Part?,
        sliders: List<MultipartBody.Part>?
    ): Flow<Resource<AddPropertyResponse>> {
        return flow {
            val result = toResultFlow {
                return@toResultFlow amtalekApi.addPropertyRequest(
                    userToken = UserUtil.getUserToken(),
                    priority = priority,
                    purpose = purpose,
                    category = category,
                    propertyType = propertyType,
                    countryId = countryId,
                    cityId = cityId,
                    regionId = regionId,
                    subRegionId = subRegionId,
                    buildingNum = buildingNum,
                    floorNum = floorNum,
                    noFloors = noFloors,
                    apartmentNum = apartmentNum,
                    currency = currency,
                    totalArea = totalArea,
                    receptionPieces = receptionPieces,
                    bedRoomsNo = bedRoomsNo,
                    bathRoomNo = bathRoomNo,
                    livingRoom = livingRoom,
                    kitchensNo = kitchensNo,
                    finishing = finishing,
                    receptionFloorType = receptionFloorType,
                    video = video,
                    location = location,
                    propertyTitleEn = propertyTitleEn,
                    propertyTitleAr = propertyTitleAr,
                    propertyDescriptionEn = propertyDescriptionEn,
                    propertyDescriptionAr = propertyDescriptionAr,
                    addressEn = addressEn,
                    addressAr = addressAr,
                    onSite = onSite,
                    forWhat = forWhat,
                    salePrice = salePrice,
                    rentPrice = rentPrice,
                    rentDuration = rentDuration,
                    amenities = amenities,
                    primaryImage = primaryImage,
                    sliders = sliders
                )
            }
            result.collect(){apiState->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        emit(Resource.Success(apiState.data))
                    }
                }

            }
        }
    }
}