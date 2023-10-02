package eramo.amtalek.data.remote.dto.products

import com.google.gson.annotations.SerializedName
import eramo.amtalek.domain.model.products.PaymentTypesModel
import eramo.amtalek.util.LocalUtil

data class PaymentTypesResponse(
    @SerializedName("payment_types") var paymentTypeDtos: ArrayList<PaymentTypesDto> = arrayListOf()
)

data class PaymentTypesDto(
    @SerializedName("pay_id") var payId: String? = null,
    @SerializedName("title_en") var titleEn: String? = null,
    @SerializedName("title_ar") var titleAr: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("img_url") var imgUrl: String? = null,
    @SerializedName("pay_type_n") var pay_type_n: String? = null
) {
    fun toPaymentTypesModel(): PaymentTypesModel {
        return PaymentTypesModel(
            payId = payId ?: "",
            title = if (LocalUtil.isEnglish()) titleEn ?: "" else titleAr ?: "",
            image = image ?: "",
            imgUrl = imgUrl ?: "",
            pay_type_n = pay_type_n ?: ""
        )
    }
}