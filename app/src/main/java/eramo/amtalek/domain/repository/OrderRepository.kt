package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.products.orders.CustomerPromoCodes
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.PaymentTypesModel
import eramo.amtalek.domain.model.products.orders.AllProductExtrasModel
import eramo.amtalek.domain.model.products.orders.OrderModel
import eramo.amtalek.domain.model.request.OrderRequest
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    suspend fun customerPromoCodes(): Flow<Resource<List<CustomerPromoCodes>>>

    suspend fun productExtras(productId: String): Flow<Resource<List<AllProductExtrasModel>>>

    suspend fun saveOrderRequest(orderRequest: OrderRequest): Flow<Resource<ResultModel>>

    suspend fun allMyOrders(): Flow<Resource<List<OrderModel>>>

    suspend fun getOrderById(orderId: String): Flow<Resource<List<OrderModel>>>

    suspend fun cancelProductOrder(order_id: String): Flow<Resource<ResultModel>>

    suspend fun paymentTypes(): Flow<Resource<List<PaymentTypesModel>>>

}