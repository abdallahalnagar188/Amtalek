package eramo.amtalek.data.remote

import eramo.amtalek.data.remote.dto.SuccessfulResponse
import eramo.amtalek.data.remote.dto.auth.CitiesResponse
import eramo.amtalek.data.remote.dto.auth.ContactUsResponse
import eramo.amtalek.data.remote.dto.auth.CountriesResponse
import eramo.amtalek.data.remote.dto.auth.OnBoardingDto
import eramo.amtalek.data.remote.dto.auth.RegionsResponse
import eramo.amtalek.data.remote.dto.bases.BaseResponse
import eramo.amtalek.data.remote.dto.bases.GeneralLoginResponse
import eramo.amtalek.data.remote.dto.broker.entity.BrokersResponse
import eramo.amtalek.data.remote.dto.brokersDetails.BrokersDetailsResponse
import eramo.amtalek.data.remote.dto.brokersProperties.BrokersPropertyResponse
import eramo.amtalek.data.remote.dto.contactBrokerDetails.ContactBrokerDetailsInPropertyDetails
import eramo.amtalek.data.remote.dto.drawer.AppInfoResponse
import eramo.amtalek.data.remote.dto.drawer.PolicyInfoResponse
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.GetProfileResponse
import eramo.amtalek.data.remote.dto.editprofile.EditProfileResponse
import eramo.amtalek.data.remote.dto.fav.AddOrRemoveFavResponse
import eramo.amtalek.data.remote.dto.general.ResultDto
import eramo.amtalek.data.remote.dto.home.HomeResponse
import eramo.amtalek.data.remote.dto.hotoffers.HotOffersResponse
import eramo.amtalek.data.remote.dto.myHome.extra_sections.HomeExtraSectionsResponse
import eramo.amtalek.data.remote.dto.myHome.featured_properety.HomeFeaturedPropertiesResponse
import eramo.amtalek.data.remote.dto.myHome.filter_by_city.HomeCitiesResponse
import eramo.amtalek.data.remote.dto.myHome.mostviewd.HomeMostViewsResponse
import eramo.amtalek.data.remote.dto.myHome.news.HomeNewsResponse
import eramo.amtalek.data.remote.dto.myHome.normal.HomeNormalPropertiesResponse
import eramo.amtalek.data.remote.dto.myHome.project.HomeProjectsResponse
import eramo.amtalek.data.remote.dto.myHome.sliders.HomeSlidersResponse
import eramo.amtalek.data.remote.dto.packages.PackagesResponse
import eramo.amtalek.data.remote.dto.packages.SubscribeToPackageResponse
import eramo.amtalek.data.remote.dto.project.ProjectDetailsResponse
import eramo.amtalek.data.remote.dto.property.SendToBrokerResponse
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.data.remote.dto.property.newResponse.addproperty.AddPropertyResponse
import eramo.amtalek.data.remote.dto.property.newResponse.amenities.AmenitiesResponse
import eramo.amtalek.data.remote.dto.property.newResponse.poperty_types.PropertyTypesResponse
import eramo.amtalek.data.remote.dto.property.newResponse.prop_details.MyPropertyDetailsResponse
import eramo.amtalek.data.remote.dto.property.newResponse.property_categories.PropertyCategoriesResponse
import eramo.amtalek.data.remote.dto.property.newResponse.property_finishing.PropertyFinishingResponse
import eramo.amtalek.data.remote.dto.property.newResponse.property_floor_finishing.PropertyFloorFinishingResponse
import eramo.amtalek.data.remote.dto.property.newResponse.property_purpose.PropertyPurposeResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_offer.SendOfferResponse
import eramo.amtalek.data.remote.dto.property.newResponse.send_prop_comment.SendPropertyCommentResponse
import eramo.amtalek.data.remote.dto.property.newResponse.submit_to_broker.SubmitToBrokerResponse
import eramo.amtalek.data.remote.dto.search.alllocations.AllLocationsResponse
import eramo.amtalek.data.remote.dto.search.currencies.CurrenciesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AmtalekApi {
    companion object {
        const val BASE_URL = "https://amtalek.com/amtalekadmin/public/api/"
    }

    //____________________________________________________________________________________________//
    // Auth

    @GET("SplashScreens")
    suspend fun onBoardingScreens(): Response<OnBoardingDto>


    @Multipart
    @POST("mobile/sign-up")
    suspend fun register(
        @Part("first_name") firstName: RequestBody?,
        @Part("last_name") lastName: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("confirm_password") confirmPassword: RequestBody?,
        @Part("birthday") birthday: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("country") countryId: RequestBody?,
        @Part("city") cityId: RequestBody?,
        @Part("region") regionId: RequestBody?,
        @Part("created_from") createdFrom: RequestBody?,
        @Part("accept_condition") acceptCondition: RequestBody?,
        @Part("not_ropot") notRobot: RequestBody?,
        @Part("iam") iam: RequestBody?,
        @Part("company_name") companyName: RequestBody?,
        @Part companyLogo: MultipartBody.Part?
    ): Response<SuccessfulResponse>

    @FormUrlEncoded
    @POST("mobile/send-email")
    suspend fun sendOtpCodeEmail(
        @Field("email") email: String,
        @Field("operation_type") operationType: String,
    ): Response<SuccessfulResponse>

    @FormUrlEncoded
    @POST("mobile/check-code")
    suspend fun checkOtpCode(
        @Field("email") email: String,
        @Field("code") otpCode: String,
        @Field("operation_type") operationType: String
    ): Response<SuccessfulResponse>

    @FormUrlEncoded
    @POST("mobile/forget-password")
    suspend fun changePasswordForgetPassword(
        @Field("email") email: String,
        @Field("code") code: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") rePassword: String
    ): Response<SuccessfulResponse>


    @FormUrlEncoded
    @POST("mobile/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("firebase_token") firebaseToken: String
    ): Response<BaseResponse<GeneralLoginResponse>>

    @POST("mobile/logout")
    suspend fun logout(
        @Header("Authorization") userToken: String,
    ): Response<SuccessfulResponse>

    @FormUrlEncoded
    @POST("mobile/update-password")
    suspend fun updatePassword(
        @Header("Authorization") userToken: String,
        @Field("current_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmPassword: String
    ): Response<SuccessfulResponse>

    @FormUrlEncoded
    @POST("mobile/suspend")
    suspend fun suspendAccount(
        @Header("Authorization") userToken: String,
        @Field("status") status: String
    ): Response<SuccessfulResponse>

    @GET("mobile/countries")
    suspend fun getCountries(): Response<CountriesResponse>

    @GET("mobile/cities/{countryId}")
    suspend fun getCities(@Path("countryId") countryId: String): Response<CitiesResponse>

    @GET("mobile/regions/{cityId}")
    suspend fun getRegions(@Path("cityId") cityId: String): Response<RegionsResponse>

    @GET("mobile/sub-regions/{regionId}")
    suspend fun getSubRegions(@Path("regionId") regionId: String): Response<RegionsResponse>

    @GET("mobile/contact-us")
    suspend fun contactUsInfo(): Response<ContactUsResponse>

    @FormUrlEncoded
    @POST("mobile/contact-us-message")
    suspend fun sendContactUsMessage(
        @Field("name") name: String,
        @Field("phone") mobileNumber: String,
        @Field("email") email: String,
        @Field("message") message: String,
        @Field("from") from: String,
        @Field("not_ropot") notRobot: String
    ): Response<SuccessfulResponse>


    //____________________________________________________________________________________________//
    // New Home
    @GET("mobile/mobile-home-featured-props")
    suspend fun getHomeFeaturedProperty(
        @Header("Authorization") userToken: String?,
        @Query("city_id") cityId: String,
    ): Response<HomeFeaturedPropertiesResponse>

    @GET("mobile/mobile-home-projects")
    suspend fun getHomeProjects(
        @Header("Authorization") userToken: String?,
        @Query("city_id") cityId: String,
    ): Response<HomeProjectsResponse>


    @GET("mobile/mobile-home-filter-by-city")
    suspend fun getFilterByCity(
        @Header("Authorization") userToken: String?,
        @Query("country_id") countryId: String,
    ): Response<HomeCitiesResponse>


    @GET("mobile/mobile-home-sliders")
    suspend fun getHomeSlider(
        @Header("Authorization") userToken: String?,
    ): Response<HomeSlidersResponse>


    @GET("mobile/mobile-home-prop-most-views")
    suspend fun getHomeMostViewedProperties(
        @Header("Authorization") userToken: String?,
        @Query("city_id") cityId: String,
    ): Response<HomeMostViewsResponse>

    @GET("mobile/mobile-home-normal-props")
    suspend fun getHomeNormalProperties(
        @Header("Authorization") userToken: String?,
        @Query("city_id") cityId: String,
    ): Response<HomeNormalPropertiesResponse>


    @GET("mobile/mobile-home-news")
    suspend fun getHomeNews(
        @Header("Authorization") userToken: String?,
    ): Response<HomeNewsResponse>

    @GET("mobile/mobile-home-extra-sections")
    suspend fun getHomeNewestSections(
        @Header("Authorization") userToken: String?,
        @Query("city_id") cityId: String,
    ): Response<HomeExtraSectionsResponse>


    @GET("mobile/all-properties/all?limit=10")
    suspend fun getAllProperties(): AllPropertyResponse

    //////////////////////////////////////////////////////////////////
    // new fav request for property
    @FormUrlEncoded
    @POST("mobile/favorite-property")
    suspend fun addOrRemoveFavProperty(
        @Header("Authorization") userToken: String?,
        @Field("property_id") propertyId: Int,
    ): Response<AddOrRemoveFavResponse>


    ////////////////////////////////////////////////////////////////////
    @GET("mobile/home")
    suspend fun getHome(
        @Header("Authorization") userToken: String?
    ): Response<HomeResponse>

    @FormUrlEncoded
    @POST("mobile/cities-filter")
    suspend fun getHomeFilteredByCity(
        @Header("Authorization") userToken: String?,
        @Field("city_id") cityId: String
    ): Response<HomeResponse>

    //____________________________________________________________________________________________//
    //hot offers
    @GET("mobile/hot-offers")
    suspend fun getHotOffers(
        @Header("Authorization") userToken: String?,
        @Query("country_id") countryId: String?,
    ): Response<HotOffersResponse>

    //packages
    @GET("packages/{type}")
    suspend fun getPackages(
        @Path("type") type: String,
    ): Response<PackagesResponse>

    @FormUrlEncoded
    @POST("subscribe-package")
    suspend fun subscribeToPackage(
        @Header("Authorization") userToken: String?,
        @Field("package_id") packageId: String,
        @Field("duration") duration: String,
        @Field("actor_type") actorType: String,
    ): Response<SubscribeToPackageResponse>


    //____________________________________________________________________________________________//
    // Add Property cer-tria

    @GET("mobile/property-types")
    suspend fun getPropertyTypes(): Response<PropertyTypesResponse>

    @GET("mobile/property-finishing")
    suspend fun getPropertyFinishing(): Response<PropertyFinishingResponse>

    @GET("mobile/property-puropse")
    suspend fun getPropertyPurpose(): Response<PropertyPurposeResponse>

    @GET("mobile/categories")
    suspend fun getPropertyCategories(): Response<PropertyCategoriesResponse>


    @GET("mobile/reception-floor-types")
    suspend fun getPropertyFloorFinishing(): Response<PropertyFloorFinishingResponse>

    @GET("mobile/aminities")
    suspend fun getPropertyAmenities(): Response<AmenitiesResponse>


    //____________________________________________________________________________________________//
    // Properties

    @GET("mobile/property/{propertyId}")
    suspend fun getPropertyDetails(
        @Header("Authorization") userToken: String?,
        @Path("propertyId") propertyId: String
    ): Response<MyPropertyDetailsResponse>

    @FormUrlEncoded
    @POST("mobile/send-property-comment")
    suspend fun sendPropertyComment(
        @Header("Authorization") userToken: String?,
        @Field("property_id") propertyId: String,
        @Field("message") message: String,
        @Field("starts") stars: Int,
        @Field("not_ropot") not_ropot: String = "yes",
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
    ): Response<SendPropertyCommentResponse>

    @FormUrlEncoded
    @POST("mobile/submit-to-broker")
    suspend fun sendMessageToPropertyOwner(
        @Header("Authorization") userToken: String?,
        @Field("property_id") propertyId: String,
        @Field("message") message: String,
        @Field("vendor_id") vendorId: String,
        @Field("not_ropot") not_ropot: String = "yes",
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
    ): Response<SubmitToBrokerResponse>

    @FormUrlEncoded
    @POST("mobile/send-offer")
    suspend fun sendPropertyOffer(
        @Header("Authorization") userToken: String?,
        @Field("property_id") propertyId: String,
        @Field("vendor_id") vendorId: String,
        @Field("not_ropot") not_ropot: String = "yes",
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("offer") offer: String,
        @Field("offer_type") offerType: String,
    ): Response<SendOfferResponse>


    @Multipart
    @POST("mobile/add-property-request")
    suspend fun addPropertyRequest(
        @Header("Authorization") userToken: String,
        @Part("priority") priority: RequestBody?,
        @Part("purpose") purpose: RequestBody?,
        @Part("category") category: RequestBody?,
        @Part("property_type") propertyType: RequestBody?,
        @Part("country") countryId: RequestBody?,
        @Part("city") cityId: RequestBody?,
        @Part("region") regionId: RequestBody?,
        @Part("sub_region") subRegionId: RequestBody?,
        @Part("building_num") buildingNum: RequestBody?,
        @Part("floor_num") floorNum: RequestBody?,
        @Part("no_floors") noFloors: RequestBody?,
        @Part("apartment_num") apartmentNum: RequestBody?,
        @Part("currency") currency: RequestBody?,
        @Part("total_area") totalArea: RequestBody?,
        @Part("reception_pieces") receptionPieces: RequestBody?,
        @Part("bed_rooms_no") bedRoomsNo: RequestBody?,
        @Part("bath_room_no") bathRoomNo: RequestBody?,
        @Part("living_room") livingRoom: RequestBody?,
        @Part("kitchens_no") kitchensNo: RequestBody?,
        @Part("finishing") finishing: RequestBody?,
        @Part("reception_floor_type") receptionFloorType: RequestBody?,
        @Part("video") video: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("property_title_en") propertyTitleEn: RequestBody?,
        @Part("property_title_ar") propertyTitleAr: RequestBody?,
        @Part("property_description_en") propertyDescriptionEn: RequestBody?,
        @Part("property_description_ar") propertyDescriptionAr: RequestBody?,
        @Part("address_en") addressEn: RequestBody?,
        @Part("address_ar") addressAr: RequestBody?,
        @Part("on_site") onSite: RequestBody?,
        @Part("for_what") forWhat: RequestBody?,
        @Part("sale_price") salePrice: RequestBody?,
        @Part("rent_price") rentPrice: RequestBody?,
        @Part("rent_duration") rentDuration: RequestBody?,
        @Part("amenities") amenities: RequestBody?,
        @Part primaryImage: MultipartBody.Part?,
        @Part sliders: List<MultipartBody.Part>?
    ): Response<AddPropertyResponse>

    //--------------------------------------------------------------------------------------------//
    //search
    @GET("mobile/all-locations")
    suspend fun getAllLocations(): Response<AllLocationsResponse>

    @GET("mobile/currencies")
    suspend fun getCurrencies(): Response<CurrenciesResponse>


    @Multipart
    @POST("mobile/search-property")
    suspend fun search(
        @Header("Authorization") userToken: String?,
        @Part("keyword") keyword: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("country") country: RequestBody?,
        @Part("currency") currency: RequestBody?,
        @Part("finishing") finishing: RequestBody?,
        @Part("max_area") maxArea: RequestBody?,
        @Part("min_area") minArea: RequestBody?,
        @Part("max_price") maxPrice: RequestBody?,
        @Part("min_price") minPrice: RequestBody?,
        @Part("min_bathes") minBathes: RequestBody?,
        @Part("min_beds") minBeds: RequestBody?,
        @Part("page") page: Int,
        @Part("price_arrange_keys") priceArrangeKeys: RequestBody?,
        @Part("property_type") propertyType: RequestBody?,
        @Part("purpose") purpose: RequestBody?,
        @Part("region") region: RequestBody?,
        @Part("sub_region") subRegion: RequestBody?,
        @Part("amenities") amenities: RequestBody?
    ): Response<eramo.amtalek.data.remote.dto.search.searchresponse.SearchResponse>
    ////------------------------------------------------------------------------------------------------//

    // ____________________________________________________________________________________________//
    // Drawer

    @GET("mobile/get-profile/{type}/{id}")
    suspend fun getProfile(
        @Header("Authorization") userToken: String,
        @Path("type") type: String,
        @Path("id") id: String
    ): Response<GetProfileResponse>


    @Multipart
    @POST("mobile/update-image")
    suspend fun updateProfilePics(
        @Header("Authorization") userToken: String?,
        @Part("image_key") imageKey: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<EditProfileResponse>

    @Multipart
    @POST("mobile/update-profile")
    suspend fun updateProfile(
        @Header("Authorization") userToken: String,
        @Part("first_name") firstName: RequestBody?,
        @Part("last_name") lastName: RequestBody?,
        @Part("phone") mobileNumber: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("country") countryId: RequestBody?,
        @Part("city") cityId: RequestBody?,
        @Part("bio") bio: RequestBody?,
        @Part profileImage: MultipartBody.Part?,
        @Part coverImage: MultipartBody.Part?
    ): Response<SuccessfulResponse>

    @FormUrlEncoded
    @POST("updateDeviceToken")
    suspend fun updateFirebaseDeviceToken(
        @Field("user_id") user_id: String,
        @Field("device_token") deviceToken: String
    ): Response<ResultDto>


    @FormUrlEncoded
    @POST("mobile/update-profile/user")
    suspend fun editProfile(
        @Header("Authorization") userToken: String?,
        @Field("first_name") firstName: String?,
        @Field("last_name") lastName: String?,
        @Field("phone") phone: String?,
        @Field("email") email: String?,
        @Field("country") countryId: String?,
        @Field("city") cityId: String?,
        @Field("not_ropot") not_ropot: String = "yes",
    ): Response<EditProfileResponse>


    @GET("getAppinfo")
    suspend fun getAppInfo(): Response<AppInfoResponse>

    @GET("getAppPolicy")
    suspend fun getAppPolicy(): Response<PolicyInfoResponse>

    @FormUrlEncoded
    @POST("contact_msg")
    suspend fun contactMsg(
        @Field("user_id") user_id: String,
        @Field("user_name") user_name: String,
        @Field("user_email") user_email: String,
        @Field("user_phone") user_phone: String,
        @Field("subject") subject: String,
        @Field("details") details: String
    ): Response<ResultDto>

    //-----------------------------------------------------------------------------------------------//
    // Projects
    @GET("mobile/project-details/{listing_number}")
    suspend fun getProjectDetails(
        @Path("listing_number") listingNumber: String
    ): Response<ProjectDetailsResponse>

    @FormUrlEncoded
    @POST("mobile/submit-to-broker")
    suspend fun sendToBroker(
        @Field("vendor_id") vendorId: String?,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("phone") phone: String?,
        @Field("message") message: String?
    ): Response<SendToBrokerResponse>


    //-----------------------------------------------------------------------------------------------//
    // Brokers
    @GET("mobile/brokers")
    suspend fun getBrokers(): BrokersResponse

    @GET("mobile/broker/{id}/broker")
    suspend fun getBrokersDetails(
        @Path("id") id: Int
    ): BrokersDetailsResponse

    @GET("mobile/brokers-properties/{id}")
    suspend fun getBrokersProperties(
        @Path("id") id: Int
    ): BrokersPropertyResponse


    @POST("web/contact-brokers-in-details")
    suspend fun sendContactRequest(
        @Body request: ContactBrokerDetailsInPropertyDetails
    ): Response<ResultDto>

}