package eramo.amtalek.data.repository

import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.myHome.news.newscateg.NewsCategoryResponse
import eramo.amtalek.domain.repository.NewsCategoryRepo
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsCategoryRepoImpl(private val api: AmtalekApi): NewsCategoryRepo {
    override suspend fun getAllNewsCategories(id: Int): Flow<Resource<NewsCategoryResponse>> {
        return flow {
            val result = toResultFlow {
                api.getAllNewsCategories(id)
            }
            result.collect {
                when (it) {
                    is ApiState.Loading -> emit(Resource.Loading())
                    is ApiState.Error -> emit(Resource.Error(it.message!!))
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }
                }
            }
        }
    }
}