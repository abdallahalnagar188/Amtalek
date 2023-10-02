package eramo.amtalek.domain.repository

import androidx.paging.PagingData
import eramo.amtalek.data.remote.dto.NotificationDto
import eramo.amtalek.data.remote.dto.products.search.PriceResponse
import eramo.amtalek.domain.model.FilterCategoryModel
import eramo.amtalek.domain.model.OffersModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.*
import eramo.amtalek.domain.model.request.SearchRequest
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getUserNotifications(): Flow<Resource<List<NotificationDto>>>

    suspend fun getProductById(productId: String): Flow<Resource<ProductModel>>

    suspend fun homeProductsByUserId(): Flow<Resource<List<ProductModel>>>

    suspend fun homeFeaturedByUserId(): Flow<Resource<List<ProductModel>>>

    suspend fun allProductsByUserId(): Flow<PagingData<ProductModel>>

    suspend fun allCategorizationByUserId(catId: String): Flow<PagingData<ProductModel>>

    suspend fun homeProductsManufacturersByUserId(): Flow<Resource<List<CategoryModel>>>

    suspend fun allProductsManufacturersByUserId(): Flow<PagingData<CategoryModel>>

    suspend fun homeDealsByUserId(): Flow<Resource<Map<String, Any>>>

    suspend fun latestDealsByUserId(): Flow<PagingData<ProductModel>>

    suspend fun addFavourite(property_id: String): Flow<Resource<ResultModel>>

    suspend fun removeFavourite(property_id: String): Flow<Resource<ResultModel>>

    suspend fun userFavListByUserId(): Flow<Resource<List<ProductModel>>>

    suspend fun productFilter(searchRequest: SearchRequest): Flow<Resource<List<ProductModel>>>

    suspend fun productSearch(title: String): Flow<Resource<List<ProductModel>>>

    suspend fun maxProductPrice(): Flow<Resource<PriceResponse>>

    suspend fun minProductPrice(): Flow<Resource<PriceResponse>>

    suspend fun getFilterCategories(): Flow<Resource<List<FilterCategoryModel>>>

    suspend fun homeAds(): Flow<Resource<List<AdsModel>>>

    suspend fun homeOffers(): Flow<Resource<List<OffersModel>>>

}