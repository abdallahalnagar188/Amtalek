package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.adons.BuyAddonsResponse
import eramo.amtalek.presentation.ui.drawer.messaging.addons.CardReqoest
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface BuyAddonsRepo {
    suspend fun buyAddons(
        list: CardReqoest,
    ): Flow<Resource<BuyAddonsResponse>>
}