package eramo.amtalek.data.repository.certaria

import com.google.android.gms.common.api.Api
import eramo.amtalek.data.remote.AmtalekApi
import eramo.amtalek.data.remote.dto.property.newResponse.property_purpose.PropertyPurposeResponse
import eramo.amtalek.domain.repository.certaria.PropertyPurposeRepository
import eramo.amtalek.util.state.ApiState
import eramo.amtalek.util.state.Resource
import eramo.amtalek.util.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PropertyPurposeRepositoryImpl @Inject constructor(
    val amtalekApi: AmtalekApi
) : PropertyPurposeRepository {
    override suspend fun getPropertyPurpose(): Flow<Resource<PropertyPurposeResponse>> {
        return flow {
            val result = toResultFlow { amtalekApi.getPropertyPurpose() }
            result.collect() {
                when (it) {
                    is ApiState.Success -> {
                        val model = it.data
                        emit(Resource.Success(model))
                    }

                    is ApiState.Error -> {
                        emit(Resource.Error(it.message!!))
                    }

                    is ApiState.Loading -> {
                        emit(Resource.Loading())
                    }
                }
            }
        }
    }
}