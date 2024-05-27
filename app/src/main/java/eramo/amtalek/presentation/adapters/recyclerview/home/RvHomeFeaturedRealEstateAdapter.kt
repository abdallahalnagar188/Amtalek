package eramo.amtalek.presentation.adapters.recyclerview.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemFeaturedRealEstateBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.domain.model.main.home.PropertyModelx
import eramo.amtalek.presentation.adapters.recyclerview.RvSimilarPropertiesAdapter
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.util.TRUE
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.enum.RentDuration
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvHomeFeaturedRealEstateAdapter @Inject constructor() :
    ListAdapter<PropertyModel, RvHomeFeaturedRealEstateAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private lateinit var favListener: FavClickListener


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
                        tvPriceRent.visibility = View.GONE
                    }

                    PropertyType.FOR_RENT.key -> {
                        tvPrice.visibility = View.GONE
                        tvPriceRent.visibility = View.VISIBLE
                        tvPriceRent.text = getRentPrice(itemView.context, model.rentDuration, model.rentPrice.toDouble(),model.currency)

                    }

                    PropertyType.FOR_BOTH.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvPriceRent.visibility = View.VISIBLE
                        tvPriceRent.text = getRentPrice(itemView.context, model.rentDuration, model.rentPrice.toDouble(),model.currency)

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