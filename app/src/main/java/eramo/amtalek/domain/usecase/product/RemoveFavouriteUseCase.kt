package eramo.amtalek.domain.usecase.product

import eramo.amtalek.domain.model.ResultModel
import eramo.amtalek.domain.repository.ProductsRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoveFavouriteUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(property_id: String): Flow<Resource<ResultModel>> {
        return repository.removeFavourite(property_id)
    }
}