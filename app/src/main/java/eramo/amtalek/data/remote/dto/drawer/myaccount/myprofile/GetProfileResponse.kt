package eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.profile.ProfileModel
import kotlinx.parcelize.Parcelize
@Parcelize
data class GetProfileResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) : Parcelable

@Parcelize
data class DataX(
    @SerializedName("actor_type")
    var actorType: String?,
    @SerializedName("bio")
    var bio: String?,
    @SerializedName("birthday")
    var birthday: String?,
    @SerializedName("city")
    var city: Int?,
    @SerializedName("city_name")
    var cityName: String?,
    @SerializedName("country_name")
    var countryName: String?,
    @SerializedName("code")
    var code: String?,
    @SerializedName("code_expired_date")
    var codeExpiredDate: String?,
    @SerializedName("code_source")
    var codeSource: String?,
    @SerializedName("code_usage")
    var codeUsage: String?,
    @SerializedName("country")
    var country: Int?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("created_from")
    var createdFrom: String?,
    @SerializedName("dashboard_link")
    var dashboardLink: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("email_verified_at")
    var emailVerifiedAt: String?,
    @SerializedName("favorite_list")
    var favoriteList: List<Favorite>?,
    @SerializedName("firebase_token")
    var firebaseToken: String?,
    @SerializedName("first_name")
    var firstName: String?,
    @SerializedName("gender")
    var gender: String?,
    @SerializedName("has_city")
    var hasCity: HasCity?,
    @SerializedName("has_package")
    var hasPackage: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("last_name")
    var lastName: String?,
    @SerializedName("my_props")
    var myProps: List<MyProp>?,
    @SerializedName("offers")
    var offers: List<OffersItem>?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("region")
    var region: Int?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("user_image")
    var userImage: String?,
    @SerializedName("verified")
    var verified: String?
) : Parcelable{
    fun toProfile(): ProfileModel{
        return ProfileModel(
            id = id?:-1,
            firstName = firstName?:"",
            lastName = lastName?:"",
            email = email?:"",
            phone = phone?:"",
            image = userImage?:"",
            cityName = cityName?:"",
            countryName = countryName?:"",
            actorType = actorType?:"",
            dashboardLink = dashboardLink?:"",
            favList = favoriteList?: emptyList(),
            myProperties = myProps?: emptyList(),
            offers = offers?: emptyList(),
            isVerified = verified?:"",
            countryId = country?:-1,
            cityId = city?:-1,
            bio = bio?:"",
            hasPackage = hasPackage?:"",
        )
    }
}

@Parcelize
data class Data(
    @SerializedName("data")
    var `data`: DataX?
) : Parcelable

@Parcelize
data class HasCity(
    @SerializedName("country_id")
    var countryId: Int?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("deleted_at")
    var deletedAt: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("title")
    var title: Title?,
    @SerializedName("updated_at")
    var updatedAt: String?
) : Parcelable

@Parcelize
data class Favorite(
    @SerializedName("address")
    var address: String?,
    @SerializedName("bath_room_no")
    var bathRoomNo: Int?,
    @SerializedName("bed_rooms_no")
    var bedRoomsNo: Int?,
    @SerializedName("broker_details")
    var brokerDetails: BrokerDetails?,
    @SerializedName("category")
    var category: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("for_what")
    var forWhat: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("is_fav")
    var isFav: String?,
    @SerializedName("land_area")
    var landArea: Int?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("primary_image")
    var primaryImage: String?,
    @SerializedName("priority")
    var priority: String?,
    @SerializedName("property_type")
    var propertyType: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("rent_duration")
    var rentDuration: String?,
    @SerializedName("rent_price")
    var rentPrice: Int?,
    @SerializedName("sale_price")
    var salePrice: Int?,
    @SerializedName("sold")
    var sold: Boolean?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("acceptance")
    var acceptance: String?
) : Parcelable{
    fun toProperty(): PropertyModel{
        return PropertyModel(
            id = id?:0,
            imageUrl = primaryImage?:"",
            title = title?:"",
            type = forWhat?:"",
            isFavourite = isFav?:"",
            rentPrice = rentPrice?:0,
            sellPrice = salePrice?:0,
            bedsCount = bedRoomsNo?:0,
            bathroomsCount = bathRoomNo?:0,
            area = landArea?:0,
            brokerId = (brokerDetails?.id?:0).toString(),
            brokerLogoUrl = brokerDetails?.companyLogo?:"",
            location = city?:"",
            datePosted = createdAt?:"",
            isFeatured = priority?:"",
            rentDuration = rentDuration?:"",
            listingNumber = listingNumber?:"",
            currency = currency?:"",
            acceptance = acceptance?:"",
            sold = sold?:false,
            offerData = null

        )
    }
}

@Parcelize
data class MyProp(
    @SerializedName("address")
    var address: String?,
    @SerializedName("bath_room_no")
    var bathRoomNo: Int?,
    @SerializedName("bed_rooms_no")
    var bedRoomsNo: Int?,
    @SerializedName("broker_details")
    var brokerDetails: BrokerDetails?,
    @SerializedName("category")
    var category: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("for_what")
    var forWhat: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("is_fav")
    var isFav: String?,
    @SerializedName("land_area")
    var landArea: Int?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("primary_image")
    var primaryImage: String?,
    @SerializedName("priority")
    var priority: String?,
    @SerializedName("property_type")
    var propertyType: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("rent_duration")
    var rentDuration: String?,
    @SerializedName("rent_price")
    var rentPrice: Int?,
    @SerializedName("sale_price")
    var salePrice: Int?,
    @SerializedName("sold")
    var sold: Boolean?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("acceptance")
    var acceptance: String?
) : Parcelable{
    fun toPropertyModel(): PropertyModel {
        return PropertyModel(
            id = id?:0,
            imageUrl = primaryImage?:"",
            title = title?:"",
            type = forWhat?:"",
            isFavourite = isFav?:"",
            rentPrice = rentPrice?:0,
            sellPrice = salePrice?:0,
            bedsCount = bedRoomsNo?:0,
            bathroomsCount = bathRoomNo?:0,
            area = landArea?:0,
            brokerId = (brokerDetails?.id?:0).toString(),
            brokerLogoUrl = brokerDetails?.companyLogo?:"",
            location = city?:"",
            datePosted = createdAt?:"",
            isFeatured = priority?:"",
            rentDuration = rentDuration?:"",
            listingNumber = listingNumber?:"",
            currency = currency?:"",
            acceptance = acceptance?:"",
            sold = sold?:false,
            offerData = null
        )
    }
}

@Parcelize
data class OfferData(
    @SerializedName("offer")
    var offer: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("offer_status")
    var offerStatus: String?,

) : Parcelable

@Parcelize
data class OffersItem(
    @SerializedName("offer_data")
    var offerData: OfferData?,
    @SerializedName("property_data")
    var propertyData: List<PropertyData?>?
) : Parcelable{
    fun toProperty(): PropertyModel{
        return PropertyModel(
            id = propertyData?.get(0)?.id?:0,
            imageUrl = propertyData?.get(0)?.primaryImage?:"",
            title = propertyData?.get(0)?.title?:"",
            type = propertyData?.get(0)?.forWhat?:"",
            isFavourite = propertyData?.get(0)?.isFav?:"",
            rentPrice = propertyData?.get(0)?.rentPrice?:0,
            sellPrice = propertyData?.get(0)?.salePrice?:0,
            bedsCount = propertyData?.get(0)?.bedRoomsNo?:0,
            bathroomsCount = propertyData?.get(0)?.bathRoomNo?:0,
            area = propertyData?.get(0)?.landArea?:0,
            brokerId = (propertyData?.get(0)?.brokerDetails?.id?:0).toString(),
            brokerLogoUrl = propertyData?.get(0)?.brokerDetails?.companyLogo?:"",
            location = propertyData?.get(0)?.city?:"",
            datePosted = propertyData?.get(0)?.createdAt?:"",
            isFeatured = propertyData?.get(0)?.priority?:"",
            rentDuration = propertyData?.get(0)?.rentDuration?:"",
            listingNumber = propertyData?.get(0)?.listingNumber?:"",
            currency = propertyData?.get(0)?.currency?:"",
            acceptance = propertyData?.get(0)?.acceptance?:"",
            sold = propertyData?.get(0)?.sold?:false,
            offerData = offerData
        )
    }

}

@Parcelize
data class PropertyData(
    @SerializedName("acceptance")
    var acceptance: String?,
    @SerializedName("address")
    var address: String?,
    @SerializedName("bath_room_no")
    var bathRoomNo: Int?,
    @SerializedName("bed_rooms_no")
    var bedRoomsNo: Int?,
    @SerializedName("broker_details")
    var brokerDetails: BrokerDetails?,
    @SerializedName("category")
    var category: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("for_what")
    var forWhat: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("is_fav")
    var isFav: String?,
    @SerializedName("land_area")
    var landArea: Int?,
    @SerializedName("listing_number")
    var listingNumber: String?,
    @SerializedName("primary_image")
    var primaryImage: String?,
    @SerializedName("priority")
    var priority: String?,
    @SerializedName("property_type")
    var propertyType: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("rent_duration")
    var rentDuration: String?,
    @SerializedName("rent_price")
    var rentPrice: Int?,
    @SerializedName("sale_price")
    var salePrice: Int?,
    @SerializedName("sold")
    var sold: Boolean?,
    @SerializedName("title")
    var title: String?
) : Parcelable{

}

@Parcelize
data class BrokerDetails(
    @SerializedName("broker_type")
    var brokerType: String?,
    @SerializedName("company_logo")
    var companyLogo: String?,
    @SerializedName("company_name")
    var companyName: String?,
    @SerializedName("id")
    var id: Int?
) : Parcelable

@Parcelize
data class Title(
    @SerializedName("ar")
    var ar: String?,
    @SerializedName("en")
    var en: String?
) : Parcelable