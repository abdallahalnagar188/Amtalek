package eramo.amtalek.domain.model.products

import eramo.amtalek.data.remote.dto.products.AllProductsDto

data class SubCatsModel(
    var catId: String? = null,
    var title: String? = null,
    var fromId: String? = null,
    var allProducts: ArrayList<AllProductsDto> = arrayListOf()
)