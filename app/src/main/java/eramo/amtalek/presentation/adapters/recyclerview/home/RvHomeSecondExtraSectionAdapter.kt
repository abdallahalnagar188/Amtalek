package eramo.amtalek.presentation.adapters.recyclerview.home

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
import eramo.amtalek.databinding.ItemPropertyPreviewBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.main.home.PropertyModelx
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.util.TRUE
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.enum.RentDuration
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvHomeSecondExtraSectionAdapter @Inject constructor() :
    ListAdapter<PropertyModel, RvHomeSecondExtraSectionAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListenerSecondSection
    private lateinit var favListener: FavClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPropertyPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemPropertyPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onItemClicked2(it)
                    }
                }
            }
        }

        fun bind(model: PropertyModel) {
            var isFav = "0"
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


                when (model.type) {
                    PropertyType.FOR_SELL.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvDurationRent.visibility = View.GONE
                    }

                    PropertyType.FOR_RENT.key -> {
                        tvPrice.visibility = View.GONE
                        tvDurationRent.visibility = View.VISIBLE
                        tvDurationRent.text = getRentPrice(itemView.context, model.rentDuration, model.rentPrice.toDouble(),model.currency)

                    }

                    PropertyType.FOR_BOTH.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvDurationRent.visibility = View.VISIBLE
                        tvDurationRent.text = getRentPrice(itemView.context, model.rentDuration, model.rentPrice.toDouble(),model.currency)

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

                if (model.isFavourite == "1") {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }

                if (model.isFeatured == "featured") {
                    tvFeatured.visibility = View.VISIBLE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background_gold)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gold)
                } else {
                    tvFeatured.visibility = View.GONE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gray_low)
                }
            }
        }
    }

    private fun getRentPrice(context: Context, duration: String, price: Double,currency: String): String {
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

    fun setListener(listener: OnItemClickListenerSecondSection, favClickListener: FavClickListener) {
        this.listener = listener
        this.favListener = favClickListener

    }

    interface OnItemClickListenerSecondSection {
        fun onItemClicked2(model: PropertyModel)
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