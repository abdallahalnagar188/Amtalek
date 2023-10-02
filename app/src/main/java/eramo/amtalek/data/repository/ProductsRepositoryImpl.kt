package eramo.amtalek.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import eramo.amtalek.R
import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.data.remote.dto.NotificationDto
import eramo.amtalek.data.remote.dto.products.search.PriceResponse
import eramo.amtalek.data.remote.paging.PagingCategories
import eramo.amtalek.data.remote.paging.PagingDeals
import eramo.amtalek.data.remote.paging.PagingManufacturers
import eramo.amtalek.data.remote.paging.PagingProducts
import eramo.amtalek.domain.model.FilterCategoryModel
import eramo.amtalek.domain.model.OffersModel
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.*
import eramo.amtalek.domain.model.request.SearchRequest
import eramo.amtalek.domain.repository.ProductsRepository
import eramo.amtalek.util.*
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductsRepositoryImpl(private val EventsApi: EventsApi) : ProductsRepository {

    override suspend fun getUserNotifications(): Flow<Resource<List<NotificationDto>>> {
        return flow {
            val result = toResultFlow { EventsApi.getUserNotifications(UserUtil.getUserId()) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> emit(Resource.Success(apiState.data))
                }
            }
        }
    }

    override suspend fun getProductById(productId: String): Flow<Resource<ProductModel>> {
        return flow {
            val result =
                toResultFlow { EventsApi.getProductById(productId, UserUtil.getUserId()) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val model = apiState.data?.allProducts?.get(0)?.toProductModel()
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }

    override suspend fun homeProductsByUserId(): Flow<Resource<List<ProductModel>>> {
        return flow {
            val result =
                toResultFlow {
                    EventsApi.allProductsByUserId(
                        "1",
                        "4",
                        UserUtil.getUserId(),
                        Constants.TEXT_NO
                    )
                }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allProducts?.map { it.toProductModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun homeFeaturedByUserId(): Flow<Resource<List<ProductModel>>> {
        return flow {
            val result =
                toResultFlow {
                    EventsApi.allProductsByUserId(
                        "1",
                        "4",
                        UserUtil.getUserId(),
                        Constants.TEXT_YES
                    )
                }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allProducts?.map { it.toProductModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun allProductsByUserId(): Flow<PagingData<ProductModel>> {
        return Pager(
            config = pagingConfig(),
            pagingSourceFactory = { PagingProducts(EventsApi) }
        ).flow
    }

    override suspend fun allCategorizationByUserId(catId: String): Flow<PagingData<ProductModel>> {
        return Pager(
            config = pagingConfig(),
            pagingSourceFactory = { PagingCategories(EventsApi, catId) }
        ).flow
    }

    override suspend fun homeProductsManufacturersByUserId(): Flow<Resource<List<CategoryModel>>> {
        return flow {
            val result =
                toResultFlow {
                    EventsApi.allProductsManufacturersByUserId(
                        "1",
                        "100",
                        UserUtil.getUserId()
                    )
                }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.all_cats?.map { it.toCategoryModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun allProductsManufacturersByUserId(): Flow<PagingData<CategoryModel>> {
        return Pager(
            config = pagingConfig(),
            pagingSourceFactory = { PagingManufacturers(EventsApi) }
        ).flow
    }

    override suspend fun homeDealsByUserId(): Flow<Resource<Map<String, Any>>> {
        return flow {
            val result =
                toResultFlow { EventsApi.latestDealsByUserId("1", "4", UserUtil.getUserId()) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allProducts?.map { it.toProductModel() }
                        val endTime = apiState.data?.latestDeals
                        val map = HashMap<String, Any>()
                        map["list"] = list as List<ProductModel>
                        map["endTime"] = endTime as String
                        emit(Resource.Success(map))
                    }
                }
            }
        }
    }

    override suspend fun latestDealsByUserId(): Flow<PagingData<ProductModel>> {
        return Pager(
            config = pagingConfig(),
            pagingSourceFactory = { PagingDeals(EventsApi) }
        ).flow
    }

    override suspend fun addFavourite(property_id: String): Flow<Resource<ResultModel>> {
        return flow {
            val result =
                toResultFlow { EventsApi.addFavourite(UserUtil.getUserId(), property_id) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        if (it.data?.success == 1) emit(Resource.Success(model))
                        else emit(Resource.Error(UiText.DynamicString(model?.message ?: "Error")))
                    }
                }
            }
        }
    }

    override suspend fun removeFavourite(property_id: String): Flow<Resource<ResultModel>> {
        return flow {
            val result =
                toResultFlow { EventsApi.removeFavourite(UserUtil.getUserId(), property_id) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data?.toResultModel()
                        if (it.data?.success == 1) emit(Resource.Success(model))
                        else emit(Resource.Error(UiText.DynamicString(model?.message ?: "Error")))
                    }
                }
            }
        }
    }

    override suspend fun userFavListByUserId(): Flow<Resource<List<ProductModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.userFavListByUserId(UserUtil.getUserId()) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        apiState.data?.allFavList?.let { favList ->
                            val list = favList.map { it.toProductModel() }
                            emit(Resource.Success(list))
                        } ?: emit(Resource.Success(emptyList()))
                    }
                }
            }
        }
    }

    override suspend fun productFilter(searchRequest: SearchRequest): Flow<Resource<List<ProductModel>>> {
        return flow {
            val result =
                toResultFlow { EventsApi.productFilter(UserUtil.getUserId(), searchRequest) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        apiState.data?.dataFounded?.let { founded ->
                            val list = founded.map { it.toProductModel() }
                            emit(Resource.Success(list))
                        } ?: emit(Resource.Error(UiText.StringResource(R.string.no_results_found)))
                    }
                }
            }
        }
    }

    override suspend fun productSearch(title: String): Flow<Resource<List<ProductModel>>> {
        return flow {
            val result =
                toResultFlow { EventsApi.productSearch(UserUtil.getUserId(), title) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.all_data?.map { it.toProductModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun maxProductPrice(): Flow<Resource<PriceResponse>> {
        return flow {
            val result = toResultFlow { EventsApi.maxProductPrice() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> emit(Resource.Success(apiState.data))
                }
            }
        }
    }

    override suspend fun minProductPrice(): Flow<Resource<PriceResponse>> {
        return flow {
            val result = toResultFlow { EventsApi.minProductPrice() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> emit(Resource.Success(apiState.data))
                }
            }
        }
    }

    override suspend fun getFilterCategories(): Flow<Resource<List<FilterCategoryModel>>>{
        return flow {
            val result =
                toResultFlow {
                    EventsApi.getFilterCategories(
                        "1",
                        "100",
                        UserUtil.getUserId()
                    )
                }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allMainManufacturer?.map { it.toFilterCategoriesModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun homeAds(): Flow<Resource<List<AdsModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.homeAds() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.map { it.toAdsModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun homeOffers(): Flow<Resource<List<OffersModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.allSpecialOffers() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.partners?.map { it.toOffersModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }
}