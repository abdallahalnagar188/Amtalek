package eramo.amtalek.domain.repository.search

import eramo.amtalek.data.remote.dto.search.currencies.CurrenciesResponse
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface CurrenciesRepository {
    suspend fun getAllCurrencies(): Flow<Resource<CurrenciesResponse>>
}