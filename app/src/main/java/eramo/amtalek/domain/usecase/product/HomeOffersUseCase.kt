package eramo.amtalek.domain.usecase.product

import eramo.amtalek.domain.model.OffersModel
import eramo.amtalek.domain.repository.ProductsRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeOffersUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(): Flow<Resource<List<OffersModel>>> {
        return repository.homeOffers()
    }
}