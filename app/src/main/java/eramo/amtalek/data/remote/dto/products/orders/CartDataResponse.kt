package eramo.amtalek.data.remote.dto.products.orders

import com.google.gson.annotations.SerializedName
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.products.common.AllImagesDto
import eramo.amtalek.domain.model.products.orders.CartDataModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.state.UiText

data class CartDataResponse(
    @SerializedName("cart_data") var cartDataDtos: ArrayList<CartDataDto> = arrayListOf(),
    @SerializedName("total_shipping") var total_shipping: Double? = null,
    @SerializedName("total_taxes") var total_taxes: Double? = null,
    @SerializedName("total_price") var total_price: Double? = null,
    @SerializedName("total") var total: Double? = null,
)

data class CartDataDto(
    @SerializedName("id") var id: String? = null,
    @SerializedName("product_id_fk") var productIdFk: String? = null,
    @SerializedName("product_qty") var productQty: String? = null,
    @SerializedName("product_price") var productPrice: String? = null,
    @SerializedName("with_installation") var withInstallation: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("date_ar") var dateAr: String? = null,
    @SerializedName("date_s") var dateS: String? = null,
    @SerializedName("manufacturer_ar_name") var manufacturerArName: String? = null,
    @SerializedName("manufacturer_en_name") var manufacturerEnName: String? = null,
    @SerializedName("model_ar_name") var modelArName: String? = null,
    @SerializedName("model_en_name") var modelEnName: String? = null,
    @SerializedName("power_ar_name") var powerArName: String? = null,
    @SerializedName("power_en_name") var powerEnName: String? = null,
    @SerializedName("in_stock") var inStock: String? = null,
    @SerializedName("available_amount") var availableAmount: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("shipping_price") var shippingPrice: String? = null,
    @SerializedName("product_name_en") var productNameEn: String? = null,
    @SerializedName("product_name_ar") var productNameAr: String? = null,
    @SerializedName("modeel_rkm") var modelRkm: String? = null,
    @SerializedName("installation_cost") var installation_cost: String? = null,
    @SerializedName("main_cat_ar_name") var mainCatArName: String? = null,
    @SerializedName("main_cat_en_name") var mainCatEnName: String? = null,
    @SerializedName("sub_cat_ar_name") var subCatArName: String? = null,
    @SerializedName("sub_cat_en_name") var subCatEnName: String? = null,
    @SerializedName("all_images") var allImageDtos: List<AllImagesDto>? = null,
) {
    fun toCartDataModel(): CartDataModel {
        return CartDataModel(
            id = id,
            productIdFk = productIdFk,
            mainCat = if (LocalUtil.isEnglish()) mainCatEnName else mainCatArName,
            productQty = productQty,
            productPrice = productPrice,
            withInstallation = withInstallation,
            userId = userId,
            dateAr = dateAr,
            dateS = dateS,
            manufacturerName = if (LocalUtil.isEnglish())
                manufacturerEnName else manufacturerArName,
            modelName = if (LocalUtil.isEnglish())
                modelEnName else modelArName,
            powerName = if (LocalUtil.isEnglish())
                powerEnName else powerArName,
            inStock = getStockText(),
            availableAmount = availableAmount,
            statusText = status,
            shippingPrice = shippingPrice,
            productName = if (LocalUtil.isEnglish())
                productNameEn else productNameAr,
            modelNumber = modelRkm,
            installation_cost = installation_cost,
            subCatName = if (LocalUtil.isEnglish())
                subCatEnName else subCatArName,
            allImageDtos = allImageDtos,
        )
    }

    private fun getStockText(): UiText {
        return if (inStock.equals("yes"))
            UiText.StringResource(R.string.in_stock_value, availableAmount as String)
        else
            UiText.StringResource(R.string.out_of_stock)
    }
}

data class CartCountResponse(
    @SerializedName("cart_count") var cart_count: String? = null,
    @SerializedName("notification_count") var notification_count: String? = null
)