package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.data.remote.dto.products.orders.CustomerPromoCodes
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.products.PaymentTypesModel
import eramo.amtalek.domain.model.products.orders.AllProductExtrasModel
import eramo.amtalek.domain.model.products.orders.OrderModel
import eramo.amtalek.domain.model.request.OrderRequest
import eramo.amtalek.domain.repository.OrderRepository
import eramo.amtalek.util.*
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrderRepositoryImpl(private val EventsApi: EventsApi) : OrderRepository {

    override suspend fun customerPromoCodes(): Flow<Resource<List<CustomerPromoCodes>>> {
        return flow {
            val result = toResultFlow { EventsApi.customerPromoCodes(UserUtil.getUserId()) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> emit(Resource.Success(it.data?.customerPromocodes))
                }
            }
        }
    }

    override suspend fun productExtras(productId: String): Flow<Resource<List<AllProductExtrasModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.productExtras(productId) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list =
                            apiState.data?.allProductExtraDtos?.map { it.toAllProductExtrasModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun saveOrderRequest(orderRequest: OrderRequest): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow { EventsApi.saveOrderRequest(orderRequest) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model=it.data?.toResultModel()
                        if (it.data?.success == 1) emit(Resource.Success(model))
                        else emit(Resource.Error(UiText.DynamicString(model?.message ?: "Error")))
                    }
                }
            }
        }
    }

    override suspend fun allMyOrders(): Flow<Resource<List<OrderModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.allMyOrders(UserUtil.getUserId()) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allOrders?.map { it.toOrderModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun getOrderById(orderId: String): Flow<Resource<List<OrderModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.getOrderById(orderId) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allOrders?.map { it.toOrderModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun cancelProductOrder(order_id: String): Flow<Resource<ResultModel>> {
        return flow {
            val result =
                toResultFlow { EventsApi.cancelProductOrder(UserUtil.getUserId(), order_id) }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model=it.data?.toResultModel()
                        if (it.data?.success == 1) emit(Resource.Success(model))
                        else emit(
                            Resource.Error(
                                UiText.DynamicString(
                                    model?.message ?: "Error"
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun paymentTypes(): Flow<Resource<List<PaymentTypesModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.paymentTypes() }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.paymentTypeDtos?.map { it.toPaymentTypesModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }
}