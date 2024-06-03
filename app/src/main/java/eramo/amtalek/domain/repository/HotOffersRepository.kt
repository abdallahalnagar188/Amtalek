package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.hotoffers.HotOffersResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface HotOffersRepository{
    suspend fun getHotOffers(countryId:String?): Flow<Resource<HotOffersResponse>>
}
