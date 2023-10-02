package eramo.amtalek.data.remote.dto.products

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.FilterCategoryModel
import eramo.amtalek.util.LocalUtil

data class FilterCategoriesResponse(
    @SerializedName("all_main_cats") var allMainManufacturer: ArrayList<FilterCategoriesDto> = arrayListOf()
)

data class FilterCategoriesDto(
    @SerializedName("cat_id") var id: String? = null,
    @SerializedName("name_ar") var name_ar: String? = null,
    @SerializedName("name_en") var name_en: String? = null,
    @SerializedName("sub_cats") var all_manufacturer: ArrayList<AllCategories>? = null
) {
    fun toFilterCategoriesModel(): FilterCategoryModel {
        return FilterCategoryModel(
            id = id ?: "",
            name = if (LocalUtil.isEnglish()) name_en ?: "" else name_ar ?: "",
            all_manufacturer = all_manufacturer?.map { it.toCategoryModel() } ?: emptyList()
        )
    }
}