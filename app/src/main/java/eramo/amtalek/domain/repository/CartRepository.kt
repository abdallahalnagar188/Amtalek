package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.products.orders.CartCountResponse
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun addToCartDB(
        productModel: ProductModel,
        product_qty: String
    ): Flow<Resource<ResultModel>>

    suspend fun addToCartApi(
        product_id: String,
        product_qty: String,
        product_price: String,
        with_installation: String
    ): Flow<Resource<ResultModel>>

    suspend fun getCartDataDB(): Flow<Resource<Map<String, Any>>>

    suspend fun getCartDataApi(): Flow<Resource<Map<String, Any>>>

    suspend fun updateCartItemDB(
        main_id: String,
        product_id: String,
        product_qty: String,
        product_price: String,
        with_installation: String
    ): Flow<Resource<ResultModel>>

    suspend fun updateCartItemApi(
        main_id: String,
        product_id: String,
        product_qty: String,
        product_price: String,
        with_installation: String
    ): Flow<Resource<ResultModel>>

    suspend fun removeCartItemDB(main_id: String): Flow<Resource<ResultModel>>

    suspend fun removeCartItemApi(main_id: String): Flow<Resource<ResultModel>>

    suspend fun removeAllCartDB(): Flow<Resource<ResultModel>>

    suspend fun removeAllCartApi(): Flow<Resource<ResultModel>>

    suspend fun getCartCountDB(): Flow<Resource<CartCountResponse>>

    suspend fun getCartCountApi(): Flow<Resource<CartCountResponse>>

    suspend fun switchLocalCartToRemote(): Flow<Resource<ResultModel>>

}