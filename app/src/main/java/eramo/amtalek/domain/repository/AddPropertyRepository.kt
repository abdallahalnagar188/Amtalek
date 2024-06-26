package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.property.newResponse.addproperty.AddPropertyResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

interface AddPropertyRepository {
    suspend fun addProperty(
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
    ): Flow<Resource<AddPropertyResponse>>
}