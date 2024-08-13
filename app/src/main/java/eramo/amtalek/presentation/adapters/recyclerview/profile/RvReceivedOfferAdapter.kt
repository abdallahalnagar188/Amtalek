package eramo.amtalek.presentation.adapters.recyclerview.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemPropertyPreviewResevedOfferBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.enum.RentDuration
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvReceivedOfferAdapter @Inject constructor() :
    ListAdapter<PropertyModel, RvReceivedOfferAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private lateinit var favListener: FavClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPropertyPreviewResevedOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemPropertyPreviewResevedOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onFeaturedRealEstateClick(it)
                    }
                }
            }
        }

        fun bind(model: PropertyModel) {
            var isFav = model.isFavourite
            binding.apply {
                ivFav.setOnClickListener {
                    favListener.onFavClick(model)
                    if (isFav =="0") {ivFav.setImageResource(R.drawable.ic_heart_fill)
                        isFav = "1"
                    }
                    else {ivFav.setImageResource(R.drawable.ic_heart)
                        isFav ="0"
                    }
                }
                if (isFav == "1") {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }
                tvOfferValue.text = " "+model.offerPrice
                tvNameValue.text = " "+model.senderName
                tvPhoneValue.text = " "+model.senderPhone
                tvDateValue.text = " "+model.offerDate
                tvBroker.text = " "+model.vendorType
//                if (model.offerData?.offerStatus =="yes"){
//                    tvStatusValue.setTextColor(itemView.context.getColor(R.color.green))
//                }
//                tvStatusValue.text = " "+model.offerData?.status
                tvPrice.text = itemView.context.getString(R.string.s_currency, formatPrice(model.sellPrice.toDouble()),model.currency)
                tvTitle.text = model.title
                tvLabel.text = when (model.type) {
                    PropertyType.FOR_SELL.key -> itemView.context.getString(R.string.for_sell)
                    PropertyType.FOR_RENT.key -> itemView.context.getString(R.string.for_rent)
                    PropertyType.FOR_BOTH.key -> itemView.context.getString(R.string.forBoth)
                    else -> {
                        ""
                    }
                }
                when (model.rentDuration) {
                    "daily" -> {
                        tvDurationRent.text =   "${formatPrice(model.rentPrice.toDouble())} ${itemView.context.getString(R.string.egp_daily)}"
                    }
                    "monthly" -> {
                        tvDurationRent.text = "${formatPrice(model.rentPrice.toDouble())} ${itemView.context.getString(R.string.monthly)}"
                    }
                    "3_months" -> {
                        tvDurationRent.text =   "${formatPrice(model.rentPrice.toDouble())} ${itemView.context.getString(R.string.egp_3_month)}"
                    }
                    "6_months" -> {
                        tvDurationRent.text =  "${formatPrice(model.rentPrice.toDouble())} ${itemView.context.getString(R.string.egp_6_month)}"
                    }
                    "9_months" -> {
                        tvDurationRent.text =  "${formatPrice(model.rentPrice.toDouble())} ${itemView.context.getString(R.string.egp_9_month)}"
                    }
                    "yearly" -> {
                        tvDurationRent.text =  "${formatPrice(model.rentPrice.toDouble())} ${itemView.context.getString(R.string.egp_daily)}"
                    }
                    ""->{
                        tvDurationRent.visibility = View.GONE
                    }
                }

                tvArea.text = itemView.context.getString(R.string.s_meter_square, formatNumber(model.area))
                tvBathroom.text = model.bathroomsCount.toString()
                tvBed.text = model.bedsCount.toString()


                Glide.with(itemView)
                    .load(model.imageUrl)
                    .placeholder(R.drawable.ic_no_image)
                    .into(ivImage)

                Glide.with(itemView)
                    .load(model.brokerLogoUrl)
                    .into(ivBroker)

                if (model.isFeatured == "featured"){
                    tvFeatured.visibility = View.VISIBLE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background_gold)
                    root.strokeColor = ContextCompat.getColor(itemView.context,R.color.gold)
                }else{
                    tvFeatured.visibility = View.GONE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background)
                    root.strokeColor = ContextCompat.getColor(itemView.context,R.color.gray_low)
                }
            }

        }
    }

    private fun getRentPrice(context: Context, duration: String, price: Double,currency:String): String {
        return when (duration) {
            RentDuration.DAILY.key -> {
                context.getString(R.string.s_daily_price, formatPrice(price),currency)
            }

            RentDuration.MONTHLY.key -> {
                context.getString(R.string.s_monthly_price, formatPrice(price),currency)
            }

            RentDuration.THREE_MONTHS.key -> {
                context.getString(R.string.s_3_months_price, formatPrice(price),currency)
            }

            RentDuration.SIX_MONTHS.key -> {
                context.getString(R.string.s_6_months_price, formatPrice(price),currency)
            }

            RentDuration.NINE_MONTHS.key -> {
                context.getString(R.string.s_9_months_price, formatPrice(price),currency)
            }

            RentDuration.YEARLY.key -> {
                context.getString(R.string.s_yearly_price, formatPrice(price),currency)
            }

            else -> ""
        }
    }

    fun setListener(listener: OnItemClickListener,favListener: FavClickListener) {
        this.listener = listener
        this.favListener = favListener

    }

    interface OnItemClickListener {
        fun onFeaturedRealEstateClick(model: PropertyModel)
    }


    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<PropertyModel>() {
            override fun areItemsTheSame(
                oldItem: PropertyModel,
                newItem: PropertyModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PropertyModel,
                newItem: PropertyModel
            ) = oldItem == newItem
        }
    }
}