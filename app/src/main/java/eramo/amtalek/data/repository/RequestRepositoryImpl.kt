package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.drawer.myaccount.RequestModel
import eramo.amtalek.domain.repository.RequestRepository
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.state.UiText
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RequestRepositoryImpl(private val EventsApi: EventsApi) : RequestRepository {

    override suspend fun questionsRequest(
        user_name: String,
        user_email: String,
        user_phone: String,
        message: String
    ): Flow<Resource<ResultModel>> {
        return flow {
            val result = toResultFlow {
                EventsApi.questionsRequest(
                    UserUtil.getUserId(),
                    user_name,
                    user_email,
                    user_phone,
                    message
                )
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val dataModel = it.data?.toResultModel()
                        if (dataModel?.success == 1) emit(Resource.Success(dataModel))
                        else emit(
                            Resource.Error(
                                UiText.DynamicString(
                                    dataModel?.message ?: "Error"
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun newRequests(): Flow<Resource<List<RequestModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.newRequests(UserUtil.getUserId()) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allRequestDtos?.map { it.toRequestModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun repliedRequests(): Flow<Resource<List<RequestModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.repliedRequests(UserUtil.getUserId()) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allRequestDtos?.map { it.toRequestModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }

    override suspend fun cancelledRequests(): Flow<Resource<List<RequestModel>>> {
        return flow {
            val result = toResultFlow { EventsApi.cancelledRequests(UserUtil.getUserId()) }
            result.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(apiState.message!!))
                    is ApiState.Success -> {
                        val list = apiState.data?.allRequestDtos?.map { it.toRequestModel() }
                        emit(Resource.Success(list))
                    }
                }
            }
        }
    }
}