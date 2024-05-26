package eramo.amtalek.presentation.adapters.recyclerview

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
import eramo.amtalek.databinding.ItemFeaturedRealEstateBinding
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.enum.RentDuration
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvSimilarPropertiesAdapter @Inject constructor() :
    ListAdapter<eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel, RvSimilarPropertiesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemFeaturedRealEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemFeaturedRealEstateBinding) :
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

        fun bind(model: eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel) {
            var isFav = model.isFavourite == ""
            binding.apply {
                ivFav.setOnClickListener {
                    isFav = !isFav
                    if (isFav) ivFav.setImageResource(R.drawable.ic_heart_fill)
                    else ivFav.setImageResource(R.drawable.ic_heart)
                }

                tvPrice.text = itemView.context.getString(R.string.s_egp, formatPrice(model.sellPrice.toDouble()))
                tvTitle.text = model.title

                tvLabel.text = when (model.type) {
                    PropertyType.FOR_SELL.key -> itemView.context.getString(R.string.for_sell)
                    PropertyType.FOR_RENT.key -> itemView.context.getString(R.string.for_rent)
                    PropertyType.FOR_BOTH.key -> itemView.context.getString(R.string.for_sell_or_rent)
                    else -> {
                        ""
                    }
                }


                when (model.type) {
                    PropertyType.FOR_SELL.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvPriceRent.visibility = View.GONE
                    }

                    PropertyType.FOR_RENT.key -> {
                        tvPrice.visibility = View.GONE
                        tvPriceRent.visibility = View.VISIBLE
                        tvPriceRent.text = getRentPrice(itemView.context, model.rentDuration, model.rentPrice.toDouble())

                    }

                    PropertyType.FOR_BOTH.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvPriceRent.visibility = View.VISIBLE
                        tvPriceRent.text = getRentPrice(itemView.context, model.rentDuration, model.rentPrice.toDouble())

                    }

                    else -> {

                    }
                }

                tvArea.text = itemView.context.getString(R.string.s_meter_square, formatNumber(model.area))
                tvBathroom.text = model.bathroomsCount.toString()
                tvBed.text = model.bedsCount.toString()
                tvLocation.text = model.location
                tvDatePosted.text = model.datePosted

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
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gold)
                }else{
                    tvFeatured.visibility = View.GONE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gray_low)
                }
                if (model.isFavourite == "") {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }
            }

        }
    }

    private fun getRentPrice(context: Context, duration: String, price: Double): String {
        return when (duration) {
            RentDuration.DAILY.key -> {
               " ${formatPrice(price)} ${context.getString(R.string.egp_daily)}"
            }

            RentDuration.MONTHLY.key -> {
                " ${formatPrice(price)} ${context.getString(R.string.egp_month)}"
            }

            RentDuration.THREE_MONTHS.key -> {
                " ${formatPrice(price)} ${context.getString(R.string.egp_3_month)}"
            }

            RentDuration.SIX_MONTHS.key -> {
                " ${formatPrice(price)} ${context.getString(R.string.egp_6_month)}"
            }

            RentDuration.NINE_MONTHS.key -> {
                " ${formatPrice(price)} ${context.getString(R.string.egp_9_month)}"
            }

            RentDuration.YEARLY.key -> {
                " ${formatPrice(price)} ${context.getString(R.string.yearly)}"
            }

            else -> ""
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onFeaturedRealEstateClick(model: eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel>() {
            override fun areItemsTheSame(
                oldItem: eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel,
                newItem: eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel,
                newItem: eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
            ) = oldItem == newItem
        }
    }
}