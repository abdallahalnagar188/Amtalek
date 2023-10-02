package eramo.amtalek.data.repository

import eramo.amtalek.data.local.EventsDao
import eramo.amtalek.data.local.entity.CartDataEntity
import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.data.remote.dto.products.orders.*
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.domain.model.products.orders.CartDataModel
import eramo.amtalek.domain.model.request.OrderRequest
import eramo.amtalek.domain.repository.CartRepository
import eramo.amtalek.util.*
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CartRepositoryImpl(
    private val EventsApi: EventsApi,
    private val dao: EventsDao
) : CartRepository {

    override suspend fun addToCartDB(
        productModel: ProductModel,
        product_qty: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                if (dao.isProductExist(productModel.productId))
                    dao.updateQuantity(productModel.productId)
                else
                    dao.insertCartItem(
                        CartDataEntity(
                            productIdFk = productModel.productId,
                            productName = productModel.productName,
                            mainCat = productModel.mainCat,
                            status = productModel.statusText,
                            modelNumber = productModel.modelNumber,
                            productQty = product_qty.toInt(),
                            productPrice = productModel.displayPrice,
                            withInstallation = Constants.TEXT_NO,
                            manufacturerEnName = productModel.manufacturerName,
                            modelEnName = productModel.modelName,
                            powerEnName = productModel.powerName,
                            inStock = productModel.inStockValue,
                            availableAmount = productModel.availableAmount,
                            shippingPrice = productModel.shipping,
                            installation_cost = productModel.installationCost,
                            allImageEntity = productModel.allImageDtos.map { it.toAllImagesEntity() },
                            quantityPrice = productModel.displayPrice
                        )
                    )
                emit(Resource.Success(ResultModel(1, "Success")))
            } catch (e: Exception) {
                emit(Resource.Error(UiText.DynamicString(e.message!!)))
            }
        }
    }

    override suspend fun addToCartApi(
        product_id: String,
        product_qty: String,
        product_price: String,
        with_installation: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                EventsApi.addToCart(
                    UserUtil.getUserId(), product_id, product_qty,
                    product_price, with_installation
                )
            }
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

    override suspend fun getCartDataDB(): Flow<Resource<Map<String, Any>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val list = dao.getCartList().map { it.toCartDataModel() }
                val map = HashMap<String, Any>()
                map["list"] = list
                map["taxes"] = "0.0"
                map["shipping"] = "0.0"
                map["total"] = "0.0"
                emit(Resource.Success(map))
            } catch (e: Exception) {
                emit(Resource.Error(UiText.DynamicString(e.message!!)))
            }
        }
    }

    override suspend fun getCartDataApi(): Flow<Resource<Map<String, Any>>> {
        return flow {
            val result = toResultFlow { EventsApi.getCartData(UserUtil.getUserId()) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val map = HashMap<String, Any>()
                        if (apiState.data?.cartDataDtos.isNullOrEmpty()) {
                            map["list"] = ArrayList<CartDataModel>(emptyList())
                        } else {
                            val list = apiState.data?.cartDataDtos?.map { it.toCartDataModel() }
                            val taxes = apiState.data?.total_taxes ?: 0.0
                            val shipping = apiState.data?.total_shipping ?: 0.0
                            val total = apiState.data?.total_price ?: 0.0
                            map["list"] = list as List<CartDataModel>
                            map["taxes"] = taxes.toString()
                            map["shipping"] = shipping.toString()
                            map["total"] = total.toString()
                        }
                        emit(Resource.Success(map))
                    }
                }
            }
        }
    }

    override suspend fun updateCartItemDB(
        main_id: String,
        product_id: String,
        product_qty: String,
        product_price: String,
        with_installation: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                val item = dao.getCartItemById(product_id)
                item.productQty = product_qty.toInt()
                item.productPrice = product_price
                item.withInstallation = with_installation
                dao.updateCartItem(item)
                emit(Resource.Success(ResultModel(1, "Success")))
            } catch (e: Exception) {
                emit(Resource.Error(UiText.DynamicString(e.message!!)))
            }
        }
    }

    override suspend fun updateCartItemApi(
        main_id: String,
        product_id: String,
        product_qty: String,
        product_price: String,
        with_installation: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                EventsApi.updateCartItem(
                    UserUtil.getUserId(),
                    main_id,
                    product_id,
                    product_qty,
                    product_price,
                    with_installation
                )
            }
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

    override suspend fun removeCartItemDB(main_id: String): Flow<Resource<ResultModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                dao.removeCartItemById(main_id.toInt())
                emit(Resource.Success(ResultModel(1, "Success")))
            } catch (e: Exception) {
                emit(Resource.Error(UiText.DynamicString(e.message!!)))
            }
        }
    }

    override suspend fun removeCartItemApi(main_id: String): Flow<Resource<ResultModel>> {
        return flow {
            val result =
                toResultFlow { EventsApi.removeCartItem(UserUtil.getUserId(), main_id) }
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

    override suspend fun removeAllCartDB(): Flow<Resource<ResultModel>> {
        return flow {
            try {
                emit(Resource.Loading())
                dao.clearCartList()
                emit(Resource.Success(ResultModel(1, "Success")))
            } catch (e: Exception) {
                emit(Resource.Error(UiText.DynamicString(e.message!!)))
            }
        }
    }

    override suspend fun removeAllCartApi(): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow { EventsApi.removeAllCart(UserUtil.getUserId()) }
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

    override suspend fun getCartCountDB(): Flow<Resource<CartCountResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                var cartCount = 0
                dao.getCartList().map { cartCount += it.productQty!! }
                emit(Resource.Success(CartCountResponse(cartCount.toString())))
            } catch (e: Exception) {
                emit(Resource.Error(UiText.DynamicString(e.message!!)))
            }
        }
    }

    override suspend fun getCartCountApi(): Flow<Resource<CartCountResponse>> {
        return flow {
            val result = toResultFlow { EventsApi.getCartCount(UserUtil.getUserId()) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> emit(Resource.Success(it.data))
                }
            }
        }
    }

    override suspend fun switchLocalCartToRemote(): Flow<Resource<ResultModel>> {
        return flow {
            emit(Resource.Loading())
            val orderList = dao.getCartList().map { it.toOrderItem() }
            val result = toResultFlow {
                EventsApi.addListToCart(
                    OrderRequest(
                        UserUtil.getUserId(),
                        orderItemList = orderList
                    )
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        dao.clearCartList()
                        emit(Resource.Success(it.data?.toResultModel()))
                    }
                }
            }
        }
    }
}