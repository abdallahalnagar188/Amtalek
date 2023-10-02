package eramo.amtalek.domain.model

import eramo.amtalek.domain.model.products.CategoryModel

data class FilterCategoryModel(
    var id: String = "",
    var name: String = "",
    var all_manufacturer: List<CategoryModel> = arrayListOf()
)