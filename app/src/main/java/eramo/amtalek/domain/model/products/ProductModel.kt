package eramo.amtalek.domain.model.products

import android.os.Parcelable
import eramo.amtalek.data.remote.dto.products.common.AllImagesDto
import eramo.amtalek.domain.model.products.common.ProductExtrasModel
import eramo.amtalek.domain.model.products.common.ProductFeaturesModel
import eramo.amtalek.util.state.UiText
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ProductModel(
    var productId: String,
    var productName: String,
    var slugTitle: String,
    var mainCatId: String,
    var subCatId: String,
    var manufacturerIdFk: String,
    var supplierIdFk: String,
    var mainTypeIdFk: String,
    var subTypeIdFk: String,
    var modelIdFk: String,
    var powerIdFk: String,
    var installationIdFk: String,
    var realPrice: String,
    var displayPrice: String,
    var totalTax: String,
    var displayTodate: String,
    var displayTodateS: String,
    var description: String,
    var comments: String,
    var updatedAt: String,
    var status: @RawValue UiText,
    var statusText: String = "",
    var featured: String,
    var hotCold: String,
    var hotColdName: String,
    var inStockValue: String,
    var inStockText: @RawValue UiText,
    var availableAmount: String,
    var userId: String,
    var shipping: String,
    var features: String,
    var extras: String,
    var instructionsUse: String,
    var modelNumber: String,
    var sku: String,
    var mainCat: String,
    var subCat: String,
    var mainActypeName: String,
    var subActypeName: String,
    var manufacturerName: String,
    var modelName: String,
    var powerName: String,
    var installationName: String,
    var installationCost: String,
    var coverFromArea: String,
    var coverToArea: String,
    var taxesPrice: Double,
    var allImageDtos: List<AllImagesDto>,
    var isNew: Boolean,
    var discount: Int,
    var productExtraModels: List<ProductExtrasModel>,
    var productFeatureModels: List<ProductFeaturesModel>,
    var isFav: Boolean,
    var extraProducts: List<ProductModel>
) : Parcelable