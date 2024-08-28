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
    var verified: String?,
    @SerializedName("total_leads")
    var totalLeads: Int?,
    @SerializedName("total_impressions")
    var totalImpressions: Int?,
    @SerializedName("total_pages_views")
    var totalViews: Int?,
    @SerializedName("received_offers")
    var receivedOffers: List<ReceivedOffer>?,
    @SerializedName("current_package_info")
    var currentPackageInfo: CurrentPackageInfo?,
    @SerializedName("history_packages")
    var historyPackages: List<HistoryPackage>?,
    @SerializedName("history_packages_info")
    var historyPackagesInfo: List<HistoryPackagesInfo>?,
    @SerializedName("name")
    var name: String?
    ) : Parcelable {
    fun toProfile(): ProfileModel {
        return ProfileModel(
            id = id ?: -1,
            firstName = firstName ?: "",
            lastName = lastName ?: "",
            email = email ?: "",
            phone = phone ?: "",
            image = userImage ?: "",
            cityName = cityName ?: "",
            countryName = countryName ?: "",
            actorType = actorType ?: "",
            dashboardLink = dashboardLink ?: "",
            favList = favoriteList ?: emptyList(),
            myProperties = myProps ?: emptyList(),
            offers = offers ?: emptyList(),
            isVerified = verified ?: "",
            countryId = country ?: -1,
            cityId = city ?: -1,
            bio = bio ?: "",
            hasPackage = hasPackage ?: "",
            leadsNumber = totalLeads ?: 0,
            totalImpressions = totalImpressions ?: 0,
            totalViews = totalViews ?: 0,
            receivedOffer = receivedOffers ?: emptyList(),
            currentPackage = currentPackageInfo,
            historyPackages = historyPackagesInfo?: emptyList(),
            name= name?:""
        )
    }
}

@Parcelize
data class CurrentPackageInfo(
    @SerializedName("actual_payment")
    var actualPayment: String?,
    @SerializedName("expiration_date")
    var expirationDate: ExpirationDate?,
    @SerializedName("package_id")
    var packageId: Int?,
    @SerializedName("package_info")
    var packageInfo: List<PackageInfo?>?
) : Parcelable {
    @Parcelize
    data class ExpirationDate(
        @SerializedName("duration")
        var duration: String?,
        @SerializedName("expiration_date")
        var expirationDate: String?,
        @SerializedName("payment_method")
        var paymentMethod: String?
    ) : Parcelable

    @Parcelize
    data class PackageInfo(
        @SerializedName("base")
        var base: Int?,
        @SerializedName("reminder")
        var reminder: Int?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("used")
        var used: Int?
    ) : Parcelable
}

data class CurrentPackage(
    @SerializedName("accounting_action")
    var accountingAction: String?,
    @SerializedName("accounting_id")
    var accountingId: Int?,
    @SerializedName("accounting_module")
    var accountingModule: String?,
    @SerializedName("actual_payment")
    var actualPayment: Int?,
    @SerializedName("added_crm_agents")
    var addedCrmAgents: Int?,
    @SerializedName("added_emoney")
    var addedEmoney: String?,
    @SerializedName("added_featured_listings")
    var addedFeaturedListings: Int?,
    @SerializedName("added_leads_management")
    var addedLeadsManagement: Int?,
    @SerializedName("added_messages")
    var addedMessages: Int?,
    @SerializedName("added_normal_listings")
    var addedNormalListings: Int?,
    @SerializedName("added_projects")
    var addedProjects: Int?,
    @SerializedName("added_supervisors")
    var addedSupervisors: Int?,
    @SerializedName("assigned_to")
    var assignedTo: Any?,
    @SerializedName("client_id")
    var clientId: Int?,
    @SerializedName("client_type")
    var clientType: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("crm_agents")
    var crmAgents: Int?,
    @SerializedName("duration_from")
    var durationFrom: Any?,
    @SerializedName("duration_to")
    var durationTo: Any?,
    @SerializedName("emoney")
    var emoney: String?,
    @SerializedName("featured_listings")
    var featuredListings: Int?,
    @SerializedName("hr_module")
    var hrModule: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("lead_id")
    var leadId: Any?,
    @SerializedName("leads_management")
    var leadsManagement: Int?,
    @SerializedName("manual_payment_creator")
    var manualPaymentCreator: Any?,
    @SerializedName("messages")
    var messages: Int?,
    @SerializedName("normal_listings")
    var normalListings: Int?,
    @SerializedName("package_duration")
    var packageDuration: String?,
    @SerializedName("package_id")
    var packageId: Int?,
    @SerializedName("package_price")
    var packagePrice: Any?,
    @SerializedName("package_will_ended_at_date")
    var packageWillEndedAtDate: String?,
    @SerializedName("package_will_ended_at_time")
    var packageWillEndedAtTime: String?,
    @SerializedName("pass_assign_to_accounting")
    var passAssignToAccounting: Int?,
    @SerializedName("payment_by")
    var paymentBy: String?,
    @SerializedName("price_monthly")
    var priceMonthly: Int?,
    @SerializedName("price_yearly")
    var priceYearly: Int?,
    @SerializedName("projects")
    var projects: Int?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("supervisor_action")
    var supervisorAction: String?,
    @SerializedName("supervisor_id")
    var supervisorId: Int?,
    @SerializedName("supervisors")
    var supervisors: Int?,
    @SerializedName("updated_at")
    var updatedAt: String?
)

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
    var acceptance: String?,
    @SerializedName("sub_region")
    var subRegion: String?,
) : Parcelable {
    fun toProperty(): PropertyModel {
        return PropertyModel(
            id = id ?: 0,
            imageUrl = primaryImage ?: "",
            title = title ?: "",
            type = forWhat ?: "",
            isFavourite = isFav ?: "",
            rentPrice = rentPrice ?: 0,
            sellPrice = salePrice ?: 0,
            bedsCount = bedRoomsNo ?: 0,
            bathroomsCount = bathRoomNo ?: 0,
            area = landArea ?: 0,
            brokerId = (brokerDetails?.id ?: 0).toString(),
            brokerLogoUrl = brokerDetails?.companyLogo ?: "",
            location = "$city, $region, $subRegion",
            datePosted = createdAt ?: "",
            isFeatured = priority ?: "",
            rentDuration = rentDuration ?: "",
            listingNumber = listingNumber ?: "",
            currency = currency ?: "",
            acceptance = acceptance ?: "",
            sold = sold ?: false,
            offerData = null,
            region = region ?: "",
            subRegion = subRegion ?: "",
            vendorType = brokerDetails?.brokerType ?: "",
            senderName = brokerDetails?.companyName ?: "",
            senderPhone = brokerDetails?.companyLogo ?: "",
            offerPrice = "",
            offerDate = ""
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
    var acceptance: String?,
    @SerializedName("sub_region")
    var subRegion: String?,
) : Parcelable {
    fun toPropertyModel(): PropertyModel {
        return PropertyModel(
            id = id ?: 0,
            imageUrl = primaryImage ?: "",
            title = title ?: "",
            type = forWhat ?: "",
            isFavourite = isFav ?: "",
            rentPrice = rentPrice ?: 0,
            sellPrice = salePrice ?: 0,
            bedsCount = bedRoomsNo ?: 0,
            bathroomsCount = bathRoomNo ?: 0,
            area = landArea ?: 0,
            brokerId = (brokerDetails?.id ?: 0).toString(),
            brokerLogoUrl = brokerDetails?.companyLogo ?: "",
            location = city ?: "",
            datePosted = createdAt ?: "",
            isFeatured = priority ?: "",
            rentDuration = rentDuration ?: "",
            listingNumber = listingNumber ?: "",
            currency = currency ?: "",
            acceptance = acceptance ?: "",
            sold = sold ?: false,
            offerData = null,
            region = region ?: "",
            subRegion = subRegion ?: "",
            vendorType = brokerDetails?.brokerType ?: "",
            senderName = brokerDetails?.companyName ?: "",
            senderPhone = "",
            offerPrice = "",
            offerDate = ""
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
) : Parcelable {
    fun toProperty(): PropertyModel {
        return PropertyModel(
            id = propertyData?.get(0)?.id ?: 0,
            imageUrl = propertyData?.get(0)?.primaryImage ?: "",
            title = propertyData?.get(0)?.title ?: "",
            type = propertyData?.get(0)?.forWhat ?: "",
            isFavourite = propertyData?.get(0)?.isFav ?: "",
            rentPrice = propertyData?.get(0)?.rentPrice ?: 0,
            sellPrice = propertyData?.get(0)?.salePrice ?: 0,
            bedsCount = propertyData?.get(0)?.bedRoomsNo ?: 0,
            bathroomsCount = propertyData?.get(0)?.bathRoomNo ?: 0,
            area = propertyData?.get(0)?.landArea ?: 0,
            brokerId = (propertyData?.get(0)?.brokerDetails?.id ?: 0).toString(),
            brokerLogoUrl = propertyData?.get(0)?.brokerDetails?.companyLogo ?: "",
            location = propertyData?.get(0)?.city ?: "",
            datePosted = propertyData?.get(0)?.createdAt ?: "",
            isFeatured = propertyData?.get(0)?.priority ?: "",
            rentDuration = propertyData?.get(0)?.rentDuration ?: "",
            listingNumber = propertyData?.get(0)?.listingNumber ?: "",
            currency = propertyData?.get(0)?.currency ?: "",
            acceptance = propertyData?.get(0)?.acceptance ?: "",
            sold = propertyData?.get(0)?.sold ?: false,
            offerData = offerData,
            region = propertyData?.get(0)?.region ?: "",
            subRegion = propertyData?.get(0)?.subRegion ?: "",
            vendorType = propertyData?.get(0)?.brokerDetails?.brokerType ?: "",
            senderName = propertyData?.get(0)?.brokerDetails?.companyName ?: "",
            senderPhone = "",
            offerPrice = "",
            offerDate = ""
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
    var title: String?,
    @SerializedName("sub_region")
    var subRegion: String?,
) : Parcelable

@Parcelize
data class ReceivedOffer(
    @SerializedName("offer_time")
    var offerTime: String?,
    @SerializedName("offer_type")
    var offerType: String?,
    @SerializedName("property")
    var `property`: List<Property?>?,
    @SerializedName("sender_email")
    var senderEmail: String?,
    @SerializedName("sender_name")
    var senderName: String?,
    @SerializedName("sender_offer")
    var senderOffer: String?,
    @SerializedName("sender_offer_type")
    var senderOfferType: String?,
    @SerializedName("sender_phone")
    var senderPhone: String?
) : Parcelable {
    fun toProperty(): PropertyModel {
        return PropertyModel(
            id = property?.get(0)?.id ?: 0,
            imageUrl = property?.get(0)?.primaryImage ?: "",
            title = property?.get(0)?.title ?: "",
            type = property?.get(0)?.forWhat ?: "",
            isFavourite = property?.get(0)?.isFav ?: "",
            rentPrice = property?.get(0)?.rentPrice?.toIntOrNull() ?: 0,
            sellPrice = property?.get(0)?.salePrice ?: 0,
            bedsCount = property?.get(0)?.bedRoomsNo ?: 0,
            bathroomsCount = property?.get(0)?.bathRoomNo ?: 0,
            area = property?.get(0)?.landArea ?: 0,
            brokerId = (property?.get(0)?.brokerDetails?.id ?: 0).toString(),
            brokerLogoUrl = property?.get(0)?.brokerDetails?.companyLogo ?: "",
            location = property?.get(0)?.city ?: "",
            datePosted = property?.get(0)?.createdAt ?: "",
            isFeatured = property?.get(0)?.priority ?: "",
            rentDuration = property?.get(0)?.rentDuration ?: "",
            listingNumber = property?.get(0)?.listingNumber ?: "",
            currency = property?.get(0)?.currency ?: "",
            acceptance = property?.get(0)?.acceptance ?: "",
            sold = property?.get(0)?.sold ?: false,
            region = property?.get(0)?.region ?: "",
            subRegion = property?.get(0)?.subRegion ?: "",
            vendorType = property?.get(0)?.brokerDetails?.brokerType ?: "",
            offerData = null,
            senderName = senderName ?: "",
            senderPhone = senderPhone ?: "",
            offerPrice = senderOffer ?: "",
            offerDate = offerTime ?: ""
        )
    }
}

@Parcelize
data class Property(
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
    var rentPrice: String?,
    @SerializedName("sale_price")
    var salePrice: Int?,
    @SerializedName("sold")
    var sold: Boolean?,
    @SerializedName("sub_region")
    var subRegion: String?,
    @SerializedName("title")
    var title: String?
) : Parcelable {
    fun toPropertyModel(): PropertyModel {
        return PropertyModel(
            id = id ?: 0,
            imageUrl = primaryImage ?: "",
            title = title ?: "",
            type = forWhat ?: "",
            isFavourite = isFav ?: "",
            rentPrice = rentPrice?.toIntOrNull() ?: 0,
            sellPrice = salePrice ?: 0,
            bedsCount = bedRoomsNo ?: 0,
            bathroomsCount = bathRoomNo ?: 0,
            area = landArea ?: 0,
            brokerId = (brokerDetails?.id ?: 0).toString(),
            brokerLogoUrl = brokerDetails?.companyLogo ?: "",
            location = city ?: "",
            datePosted = createdAt ?: "",
            isFeatured = priority ?: "",
            rentDuration = rentDuration ?: "",
            listingNumber = listingNumber ?: "",
            currency = currency ?: "",
            acceptance = acceptance ?: "",
            sold = sold ?: false,
            offerData = null,
            region = region ?: "",
            subRegion = subRegion ?: "",
            vendorType = brokerDetails?.brokerType ?: "",
            senderName = brokerDetails?.companyName ?: "",
            senderPhone = "",
            offerPrice = "",
            offerDate = ""
        )
    }

}
@Parcelize
data class HistoryPackage(
    @SerializedName("accounting_action")
    var accountingAction: String?,
    @SerializedName("accounting_id")
    var accountingId: Int?,
    @SerializedName("accounting_module")
    var accountingModule: String?,
    @SerializedName("actual_payment")
    var actualPayment: Int?,
    @SerializedName("added_crm_agents")
    var addedCrmAgents: Int?,
    @SerializedName("added_emoney")
    var addedEmoney: String?,
    @SerializedName("added_featured_listings")
    var addedFeaturedListings: Int?,
    @SerializedName("added_leads_management")
    var addedLeadsManagement: Int?,
    @SerializedName("added_messages")
    var addedMessages: Int?,
    @SerializedName("added_normal_listings")
    var addedNormalListings: Int?,
    @SerializedName("added_projects")
    var addedProjects: Int?,
    @SerializedName("added_supervisors")
    var addedSupervisors: Int?,
    @SerializedName("assigned_to")
    var assignedTo: String?,
    @SerializedName("client_id")
    var clientId: Int?,
    @SerializedName("client_type")
    var clientType: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("crm_agents")
    var crmAgents: Int?,
    @SerializedName("duration_from")
    var durationFrom: String?,
    @SerializedName("duration_to")
    var durationTo: String?,
    @SerializedName("emoney")
    var emoney: String?,
    @SerializedName("featured_listings")
    var featuredListings: Int?,
    @SerializedName("hr_module")
    var hrModule: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("lead_id")
    var leadId: Int?,
    @SerializedName("leads_management")
    var leadsManagement: Int?,
    @SerializedName("manual_payment_creator")
    var manualPaymentCreator: String?,
    @SerializedName("messages")
    var messages: Int?,
    @SerializedName("normal_listings")
    var normalListings: Int?,
    @SerializedName("package_duration")
    var packageDuration: String?,
    @SerializedName("package_id")
    var packageId: Int?,
    @SerializedName("package_price")
    var packagePrice: Int?,
    @SerializedName("pass_assign_to_accounting")
    var passAssignToAccounting: Int?,
    @SerializedName("payment_by")
    var paymentBy: String?,
    @SerializedName("price_monthly")
    var priceMonthly: Int?,
    @SerializedName("price_yearly")
    var priceYearly: Int?,
    @SerializedName("projects")
    var projects: Int?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("supervisor_action")
    var supervisorAction: String?,
    @SerializedName("supervisor_id")
    var supervisorId: Int?,
    @SerializedName("supervisors")
    var supervisors: Int?,
    @SerializedName("updated_at")
    var updatedAt: String?
):Parcelable
@Parcelize
data class HistoryPackagesInfo(
    @SerializedName("actual_payment")
    var actualPayment: String?,
    @SerializedName("expiration_date")
    var expirationDate: ExpirationDate?,
    @SerializedName("package_details")
    var packageDetails: List<PackageDetail?>?
) :Parcelable{
    @Parcelize
    data class ExpirationDate(
        @SerializedName("date_of_package")
        var dateOfPackage: String?,
        @SerializedName("duration")
        var duration: String?,
        @SerializedName("expiration_date")
        var expirationDate: String?,
        @SerializedName("package_id")
        var packageId: Int?,
        @SerializedName("package_type")
        var packageType: String?,
        @SerializedName("payment_method")
        var paymentMethod: String?,
        @SerializedName("status")
        var status: String?
    ):Parcelable
@Parcelize
    data class PackageDetail(
        @SerializedName("base")
        var base: Int?,
        @SerializedName("reminder")
        var reminder: Int?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("used")
        var used: Int?
    ):Parcelable
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