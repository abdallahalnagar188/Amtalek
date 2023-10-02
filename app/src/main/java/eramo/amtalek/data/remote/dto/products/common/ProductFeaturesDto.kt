package eramo.amtalek.data.remote.dto.products.common

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.products.common.ProductFeaturesModel
import eramo.amtalek.util.LocalUtil

data class ProductFeaturesDto(
    @SerializedName("id") var id: String? = null,
    @SerializedName("product_id_fk") var productIdFk: String? = null,
    @SerializedName("feature_id_fk") var featureIdFk: String? = null,
    @SerializedName("feature_name_ar") var featureNameAr: String? = null,
    @SerializedName("feature_name_en") var featureNameEn: String? = null
) {
    fun toProductFeaturesModel(): ProductFeaturesModel {
        return ProductFeaturesModel(
            id = id ?: "",
            productIdFk = productIdFk ?: "",
            featureIdFk = featureIdFk ?: "",
            featureName =
            if (LocalUtil.isEnglish()) featureNameEn ?: "" else featureNameAr ?: ""
        )
    }
}
