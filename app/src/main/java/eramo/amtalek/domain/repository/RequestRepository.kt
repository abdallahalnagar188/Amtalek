package eramo.amtalek.domain.repository

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.model.drawer.myaccount.RequestModel
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface RequestRepository {

    suspend fun questionsRequest(
        user_name: String,
        user_email: String,
        user_phone: String,
        message: String
    ): Flow<Resource<ResultModel>>

    suspend fun newRequests(): Flow<Resource<List<RequestModel>>>

    suspend fun repliedRequests(): Flow<Resource<List<RequestModel>>>

    suspend fun cancelledRequests(): Flow<Resource<List<RequestModel>>>

}