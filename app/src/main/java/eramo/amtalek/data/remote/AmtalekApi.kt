package eramo.amtalek.data.remote

import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.NotificationDto
import eramo.amtalek.data.remote.dto.SuccessfulResponse
import eramo.amtalek.data.remote.dto.auth.*
import eramo.amtalek.data.remote.dto.drawer.AppInfoResponse
import eramo.amtalek.data.remote.dto.drawer.PolicyInfoResponse
import eramo.amtalek.data.remote.dto.drawer.myaccount.AllRequestsResponse
import eramo.amtalek.data.remote.dto.drawer.myaccount.EditProfileResponse
import eramo.amtalek.data.remote.dto.drawer.myaccount.GetProfileResponse
import eramo.amtalek.data.remote.dto.general.ResultDto
import eramo.amtalek.data.remote.dto.home.HomeResponse
import eramo.amtalek.data.remote.dto.products.*
import eramo.amtalek.data.remote.dto.products.orders.*
import eramo.amtalek.data.remote.dto.products.search.PriceResponse
import eramo.amtalek.data.remote.dto.products.search.SearchResponse
import eramo.amtalek.data.remote.dto.property.PropertyDetailsResponse
import eramo.amtalek.domain.model.auth.GetProfileModel
import eramo.amtalek.domain.model.request.OrderRequest
import eramo.amtalek.domain.model.request.SearchRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AmtalekApi {
    companion object {
        const val BASE_URL = "https://amtalek.com/amtalekadmin/public/api/"
        const val IMAGE_URL_SPECIAL_OFFERS = "https://Events.shop/uploads/special_offers/"
    }

    //____________________________________________________________________________________________//
    // Auth

    @GET("SplashScreens")
    suspend fun onBoardingScreens(): Response<OnBoardingDto>

    @Multipart
    @POST("mobile/sign-up")
    suspend fun register(
        @Part("first_name") firstName: String,
        @Part("last_name") lastName: String,
        @Part("phone") phone: String?,
        @Part("email") email: String?,
        @Part("password") password: String?,
        @Part("confirm_password") confirmPassword: String?,
        @Part("gender") gender: String?,
        @Part("country") countryId: String?,
        @Part("city") cityId: String?,
        @Part("region") regionId: String?,
        @Part("created_from") createdFrom: String?,
        @Part("accept_condition") acceptCondition: String?,
        @Part("not_ropot") notRobot: String?,
        @Part("company_name") companyName: String?,
        @Part("iam") iam: String,
        @Part companyLogo:MultipartBody.Part?

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
    ): Response<LoginResponse>

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
    // Home

    @GET("mobile/home")
    suspend fun getHome(
        @Header("Authorization") userToken: String?
    ): Response<HomeResponse>

    @FormUrlEncoded
    @POST("mobile/cities-filter")
    suspend fun getHomeFilteredByCity(
        @Header("Authorization") userToken: String?,
        @Field("city_id")cityId: String
    ): Response<HomeResponse>


    //____________________________________________________________________________________________//
    // Properties

    @GET("mobile/property-details/{propertyId}")
    suspend fun getPropertyDetails(
        @Header("Authorization") userToken: String?,
//        @Path("propertyId") propertyId:String = "38"
        @Path("propertyId") propertyId:String
    ): Response<PropertyDetailsResponse>

    // ____________________________________________________________________________________________//
    // Drawer

    @GET("mobile/get-profile")
    suspend fun getProfile(
        @Header("Authorization") userToken: String,
    ): Response<GetProfileResponse>

    @Multipart
    @POST("mobile/update-profile")
    suspend fun updateProfile(
        @Header("Authorization") userToken:String,
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

//    @FormUrlEncoded
//    @POST("getProfile")
//    suspend fun getProfile(@Field("user_id") user_id: String): Response<Member>

    @Multipart
    @POST("edit_profile")
    suspend fun editProfile(
        @Part("user_id") user_id: RequestBody?,
        @Part("user_pass") user_pass: RequestBody?,
        @Part("user_name") user_name: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("country_id") countryId: RequestBody?,
        @Part("city_id") cityId: RequestBody?,
        @Part("region_id") regionId: RequestBody?,
        @Part("user_email") user_email: RequestBody?,
        @Part("user_phone") user_phone: RequestBody?,
        @Part m_image: MultipartBody.Part?
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

    //____________________________________________________________________________________________//
    // Products

    @GET("getUserNotifications/{userId}")
    suspend fun getUserNotifications(@Path("userId") userId: String): Response<List<NotificationDto>>

    @GET("Oneproduct/{productId}/{userId}")
    suspend fun getProductById(
        @Path("productId") productId: String,
        @Path("userId") userId: String
    ): Response<AllProductsResponse>

    @GET("Allproducts")
    suspend fun allProductsByUserId(
        @Query("page") page: String,
        @Query("perpage") perpage: String,
        @Query("user_id") user_id: String,
        @Query("featured") featured: String,
    ): Response<AllProductsResponse>

    @GET("Allproducts_one_cats")
    suspend fun allCategorizationByUserId(
        @Query("page") page: String,
        @Query("perpage") perpage: String,
        @Query("cat_id") cat_id: String,
        @Query("user_id") user_id: String
    ): Response<CategoriesResponse>

    @GET("Allproducts_manufacturers")
    suspend fun allProductsManufacturersByUserId(
        @Query("page") page: String,
        @Query("perpage") perpage: String,
        @Query("user_id") user_id: String
    ): Response<AllCategoriesResponse>

    @GET("LatestDeals")
    suspend fun latestDealsByUserId(
        @Query("page") page: String,
        @Query("perpage") perpage: String,
        @Query("user_id") user_id: String
    ): Response<AllProductsResponse>

    @FormUrlEncoded
    @POST("add_favourite")
    suspend fun addFavourite(
        @Field("user_id") user_id: String?,
        @Field("product_id_fk") property_id: String?
    ): Response<ResultDto>

    @FormUrlEncoded
    @POST("remove_favourite")
    suspend fun removeFavourite(
        @Field("user_id") user_id: String?,
        @Field("product_id_fk") property_id: String?
    ): Response<ResultDto>

    @GET("UserFavList/{userId}")
    suspend fun userFavListByUserId(@Path("userId") userId: String): Response<AllFavListResponse>

    @POST("product_search/{userId}")
    suspend fun productFilter(
        @Path("userId") userId: String,
        @Body searchRequest: SearchRequest
    ): Response<SearchResponse>

    @FormUrlEncoded
    @POST("search_product_title/{userId}")
    suspend fun productSearch(
        @Path("userId") userId: String,
        @Field("title") title: String
    ): Response<SearchResponse>

    @GET("MaxProductPrice")
    suspend fun maxProductPrice(): Response<PriceResponse>

    @GET("MinProductPrice")
    suspend fun minProductPrice(): Response<PriceResponse>

    @GET("getCategories")
    suspend fun getFilterCategories(
        @Query("page") page: String,
        @Query("perpage") perpage: String,
        @Query("user_id") user_id: String
    ): Response<FilterCategoriesResponse>

    @GET("home_ads")
    suspend fun homeAds(): Response<List<AdsDto>>

    @GET("AllSpecialOffers")
    suspend fun allSpecialOffers(): Response<OffersResponse>

    //____________________________________________________________________________________________//
    // Request

    @FormUrlEncoded
    @POST("Question_Request")
    suspend fun questionsRequest(
        @Field("user_id") user_id: String,
        @Field("user_name") user_name: String,
        @Field("user_email") user_email: String,
        @Field("user_phone") user_phone: String,
        @Field("message") message: String
    ): Response<ResultDto>

    @GET("My_new_Requests/{userId}")
    suspend fun newRequests(@Path("userId") userId: String): Response<AllRequestsResponse>

    @GET("My_replied_Requests/{userId}")
    suspend fun repliedRequests(@Path("userId") userId: String): Response<AllRequestsResponse>

    @GET("My_cancelled_Requests/{userId}")
    suspend fun cancelledRequests(@Path("userId") userId: String): Response<AllRequestsResponse>

    //____________________________________________________________________________________________//
    // Orders

    @GET("customer_promocodes/{userId}")
    suspend fun customerPromoCodes(@Path("userId") userId: String): Response<CustomerPromoCodesResponse>

    @GET("productExtras/{productId}")
    suspend fun productExtras(@Path("productId") productId: String): Response<ExtrasProductResponse>

    @POST("saveProductOrders")
    suspend fun saveOrderRequest(@Body orderRequest: OrderRequest): Response<ResultDto>

    @GET("all_my_orders/{userId}")
    suspend fun allMyOrders(@Path("userId") userId: String): Response<AllOrderResponse>

    @GET("get_order_by_id/{orderId}")
    suspend fun getOrderById(@Path("orderId") orderId: String): Response<AllOrderResponse>

    @FormUrlEncoded
    @POST("cancel_product_order")
    suspend fun cancelProductOrder(
        @Field("user_id") user_id: String,
        @Field("order_id") order_id: String
    ): Response<ResultDto>

    @GET("PaymentTypes")
    suspend fun paymentTypes(): Response<PaymentTypesResponse>

    //____________________________________________________________________________________________//
    // Cart

    @FormUrlEncoded
    @POST("add_cart")
    suspend fun addToCart(
        @Field("user_id") user_id: String,
        @Field("product_id_fk") product_id: String,
        @Field("product_qty") product_qty: String,
        @Field("product_price") product_price: String,
        @Field("with_installation") with_installation: String
    ): Response<ResultDto>

    @POST("AddTocart")
    suspend fun addListToCart(@Body orderRequest: OrderRequest): Response<ResultDto>

    @GET("GetCartData/{userId}")
    suspend fun getCartData(@Path("userId") userId: String): Response<CartDataResponse>

    @FormUrlEncoded
    @POST("update_cart")
    suspend fun updateCartItem(
        @Field("user_id") user_id: String,
        @Field("main_id") main_id: String,
        @Field("product_id_fk") product_id: String,
        @Field("product_qty") product_qty: String,
        @Field("product_price") product_price: String,
        @Field("with_installation") with_installation: String
    ): Response<ResultDto>

    @FormUrlEncoded
    @POST("remove_from_cart")
    suspend fun removeCartItem(
        @Field("user_id") user_id: String,
        @Field("main_id") main_id: String
    ): Response<ResultDto>

    @FormUrlEncoded
    @POST("remove_all_cart")
    suspend fun removeAllCart(@Field("user_id") user_id: String): Response<ResultDto>

    @GET("CountCart/{userId}")
    suspend fun getCartCount(@Path("userId") userId: String): Response<CartCountResponse>

}