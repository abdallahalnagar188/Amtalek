package eramo.amtalek.domain.repository

import eramo.amtalek.data.remote.dto.adons.AddonsResponse
import eramo.amtalek.data.remote.dto.adons.BuyAddonsRequest
import eramo.amtalek.data.remote.dto.adons.BuyAddonsResponse
import eramo.amtalek.presentation.ui.drawer.messaging.addons.CardRecoest
import eramo.amtalek.presentation.ui.drawer.messaging.addons.ItemCard
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow

interface BuyAddonsRepo {
    suspend fun buyAddons(
        list: CardRecoest,
    ): Flow<Resource<BuyAddonsResponse>>
}