package eramo.amtalek.domain.usecase.product

import eramo.amtalek.domain.model.products.ProductModel
import eramo.amtalek.domain.repository.ProductsRepository
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProductByIdUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(productId: String): Flow<Resource<ProductModel>> {
        return repository.getProductById(productId)
    }
}