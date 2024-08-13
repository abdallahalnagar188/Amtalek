package eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile


import com.google.gson.annotations.SerializedName

data class GetProfileResponseSecond(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
) {
    data class Data(
        @SerializedName("data")
        var `data`: Data?
    ) {
        data class Data(
            @SerializedName("actor_type")
            var actorType: String?,
            @SerializedName("bio")
            var bio: Any?,
            @SerializedName("birthday")
            var birthday: String?,
            @SerializedName("city")
            var city: Int?,
            @SerializedName("city_name")
            var cityName: String?,
            @SerializedName("code")
            var code: String?,
            @SerializedName("code_expired_date")
            var codeExpiredDate: String?,
            @SerializedName("code_source")
            var codeSource: String?,
            @SerializedName("code_usage")
            var codeUsage: Any?,
            @SerializedName("country")
            var country: Int?,
            @SerializedName("country_name")
            var countryName: String?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("created_from")
            var createdFrom: String?,
            @SerializedName("current_package")
            var currentPackage: CurrentPackage?,
            @SerializedName("current_package_info")
            var currentPackageInfo: CurrentPackageInfo?,
            @SerializedName("dashboard_link")
            var dashboardLink: Any?,
            @SerializedName("email")
            var email: String?,
            @SerializedName("email_verified_at")
            var emailVerifiedAt: Any?,
            @SerializedName("favorite_list")
            var favoriteList: List<Favorite?>?,
            @SerializedName("firebase_token")
            var firebaseToken: Any?,
            @SerializedName("first_name")
            var firstName: String?,
            @SerializedName("gender")
            var gender: String?,
            @SerializedName("has_city")
            var hasCity: HasCity?,
            @SerializedName("has_country")
            var hasCountry: HasCountry?,
            @SerializedName("has_package")
            var hasPackage: String?,
            @SerializedName("history_packages")
            var historyPackages: List<HistoryPackage?>?,
            @SerializedName("history_packages_info")
            var historyPackagesInfo: List<HistoryPackagesInfo?>?,
            @SerializedName("id")
            var id: Int?,
            @SerializedName("last_name")
            var lastName: String?,
            @SerializedName("my_props")
            var myProps: List<MyProp?>?,
            @SerializedName("offers")
            var offers: List<Offer?>?,
            @SerializedName("phone")
            var phone: String?,
            @SerializedName("received_offers")
            var receivedOffers: List<ReceivedOffer?>?,
            @SerializedName("region")
            var region: Int?,
            @SerializedName("status")
            var status: String?,
            @SerializedName("total_impressions")
            var totalImpressions: Int?,
            @SerializedName("total_leads")
            var totalLeads: Int?,
            @SerializedName("total_pages_views")
            var totalPagesViews: Int?,
            @SerializedName("updated_at")
            var updatedAt: String?,
            @SerializedName("user_image")
            var userImage: String?,
            @SerializedName("verified")
            var verified: String?
        ) {
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
//
//            data class CurrentPackageInfo(
//                @SerializedName("actual_payment")
//                var actualPayment: String?,
//                @SerializedName("expiration_date")
//                var expirationDate: ExpirationDate?,
//                @SerializedName("package_id")
//                var packageId: Int?,
//                @SerializedName("package_info")
//                var packageInfo: List<PackageInfo?>?
//            ) {
//                data class ExpirationDate(
//                    @SerializedName("duration")
//                    var duration: String?,
//                    @SerializedName("expiration_date")
//                    var expirationDate: String?,
//                    @SerializedName("payment_method")
//                    var paymentMethod: String?
//                )
//
//                data class PackageInfo(
//                    @SerializedName("base")
//                    var base: Int?,
//                    @SerializedName("reminder")
//                    var reminder: Int?,
//                    @SerializedName("title")
//                    var title: String?,
//                    @SerializedName("used")
//                    var used: Int?
//                )
//            }

            data class Favorite(
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
                @SerializedName("sub_region")
                var subRegion: String?,
                @SerializedName("title")
                var title: String?
            ) {
                data class BrokerDetails(
                    @SerializedName("broker_type")
                    var brokerType: String?,
                    @SerializedName("company_logo")
                    var companyLogo: String?,
                    @SerializedName("company_name")
                    var companyName: String?,
                    @SerializedName("id")
                    var id: Int?
                )
            }

            data class HasCity(
                @SerializedName("country_id")
                var countryId: Int?,
                @SerializedName("created_at")
                var createdAt: String?,
                @SerializedName("deleted_at")
                var deletedAt: Any?,
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
            ) {
                data class Title(
                    @SerializedName("ar")
                    var ar: String?,
                    @SerializedName("en")
                    var en: String?
                )
            }

            data class HasCountry(
                @SerializedName("created_at")
                var createdAt: String?,
                @SerializedName("deleted_at")
                var deletedAt: Any?,
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
            ) {
                data class Title(
                    @SerializedName("ar")
                    var ar: String?,
                    @SerializedName("en")
                    var en: String?
                )
            }

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

            data class HistoryPackagesInfo(
                @SerializedName("actual_payment")
                var actualPayment: String?,
                @SerializedName("expiration_date")
                var expirationDate: ExpirationDate?,
                @SerializedName("package_details")
                var packageDetails: List<PackageDetail?>?
            ) {
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
                )

                data class PackageDetail(
                    @SerializedName("base")
                    var base: Int?,
                    @SerializedName("reminder")
                    var reminder: Int?,
                    @SerializedName("title")
                    var title: String?,
                    @SerializedName("used")
                    var used: Int?
                )
            }

            data class MyProp(
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
                @SerializedName("sub_region")
                var subRegion: String?,
                @SerializedName("title")
                var title: String?
            ) {
                data class BrokerDetails(
                    @SerializedName("broker_type")
                    var brokerType: String?,
                    @SerializedName("company_logo")
                    var companyLogo: String?,
                    @SerializedName("company_name")
                    var companyName: String?,
                    @SerializedName("id")
                    var id: Int?
                )
            }

            data class Offer(
                @SerializedName("offer_data")
                var offerData: OfferData?,
                @SerializedName("property_data")
                var propertyData: List<PropertyData?>?
            ) {
                data class OfferData(
                    @SerializedName("offer")
                    var offer: String?,
                    @SerializedName("offer_status")
                    var offerStatus: String?,
                    @SerializedName("status")
                    var status: String?
                )

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
                    @SerializedName("sub_region")
                    var subRegion: String?,
                    @SerializedName("title")
                    var title: String?
                ) {
                    data class BrokerDetails(
                        @SerializedName("broker_type")
                        var brokerType: String?,
                        @SerializedName("company_logo")
                        var companyLogo: String?,
                        @SerializedName("company_name")
                        var companyName: String?,
                        @SerializedName("id")
                        var id: Int?
                    )
                }
            }

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
            ) {
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
                    var rentDuration: Any?,
                    @SerializedName("rent_price")
                    var rentPrice: Any?,
                    @SerializedName("sale_price")
                    var salePrice: Int?,
                    @SerializedName("sold")
                    var sold: Boolean?,
                    @SerializedName("sub_region")
                    var subRegion: String?,
                    @SerializedName("title")
                    var title: String?
                ) {
                    data class BrokerDetails(
                        @SerializedName("broker_type")
                        var brokerType: String?,
                        @SerializedName("company_logo")
                        var companyLogo: String?,
                        @SerializedName("company_name")
                        var companyName: String?,
                        @SerializedName("id")
                        var id: Int?
                    )
                }
            }
        }
    }
}