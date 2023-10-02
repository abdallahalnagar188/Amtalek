package eramo.amtalek.data.remote.dto.products

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.products.CategoryModel
import eramo.amtalek.util.LocalUtil

data class AllCategoriesResponse(
    @SerializedName("all_cats") var all_cats: ArrayList<AllCategories> = arrayListOf()
)

data class AllCategories(
    @SerializedName("cat_id") var manufacturerId: String? = null,
    @SerializedName("title_ar") var titleAr: String? = null,
    @SerializedName("title_en") var titleEn: String? = null,
    @SerializedName("from_id") var fromId: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("all_products") var allProducts: ArrayList<AllProductsDto>? = null
) {
    fun toCategoryModel(): CategoryModel {
        return CategoryModel(
            manufacturerId = manufacturerId ?: "",
            title = if (LocalUtil.isEnglish()) titleEn ?: "" else titleAr ?: "",
            fromId = fromId ?: "",
            image = image ?: "",
            allProducts = allProducts?.map { it.toProductModel() } ?: emptyList()
        )
    }
}