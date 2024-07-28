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
import eramo.amtalek.data.remote.dto.property.allproperty.DataX
import eramo.amtalek.databinding.ItemPropertyPreviewBinding
import eramo.amtalek.domain.model.main.home.PropertyModelx
import eramo.amtalek.util.TRUE
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.enum.RentDuration
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvPropertiesAdapter @Inject constructor() :
    ListAdapter<DataX, RvPropertiesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

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
                        listener.onPropertyClick(it)
                    }
                }
            }
        }

        fun bind(model: DataX) {
            var isFav = model.isFav == "0"
            binding.apply {
                ivFav.setOnClickListener {
                    isFav = !isFav
                    if (isFav) ivFav.setImageResource(R.drawable.ic_heart_fill)
                    else ivFav.setImageResource(R.drawable.ic_heart)
                }

                tvPrice.text =
                    itemView.context.getString(R.string.s_currency, model.salePrice?.let { formatPrice(it.toDouble()) }, model.currency)
                tvTitle.text = model.title

                tvLabel.text = when (model.forWhat) {
                    PropertyType.FOR_SELL.key -> itemView.context.getString(R.string.for_sell)
                    PropertyType.FOR_RENT.key -> itemView.context.getString(R.string.for_rent)
                    PropertyType.FOR_BOTH.key -> itemView.context.getString(R.string.for_sell_or_rent)
                    else -> {
                        ""
                    }
                }

                when (model.forWhat) {
                    PropertyType.FOR_SELL.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvDurationRent.visibility = View.GONE
                    }

                    PropertyType.FOR_RENT.key -> {
                        tvPrice.visibility = View.GONE
                        tvDurationRent.visibility = View.VISIBLE
                        tvDurationRent.text =
                            model.rentDuration?.let { model.currency?.let { it1 ->
                                model.rentPrice?.toDouble()?.let { it2 ->
                                    getRentPrice(itemView.context, it, it2,
                                        it1
                                    )
                                }
                            } }

                    }

                    PropertyType.FOR_BOTH.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvDurationRent.visibility = View.VISIBLE
                        tvDurationRent.text =
                            model.rentDuration?.let { model.currency?.let { it1 ->
                                model.rentPrice?.let { it2 ->
                                    getRentPrice(itemView.context, it, it2.toDouble(),
                                        it1
                                    )
                                }
                            } }

                    }

                    else -> {

                    }
                }

                tvArea.text = itemView.context.getString(R.string.s_meter_square, model.landArea?.let { formatNumber(it) })
                tvBathroom.text = model.bathRoomNo.toString()
                tvBed.text = model.bedRoomsNo.toString()
                tvLocation.text = model.address
                tvDatePosted.text = model.createdAt

                Glide.with(itemView)
                    .load(model.primaryImage)
                    .placeholder(R.drawable.ic_no_image)
                    .into(ivImage)

                Glide.with(itemView)
                    .load(model.brokerDetails?.get(0)?.logo)
                    .into(ivBroker)

                if (model.isFav == "0") {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }
//
//                if (model. == TRUE) {
//                    tvFeatured.visibility = View.VISIBLE
//                    tvLabel.setBackgroundResource(R.drawable.property_label_background_gold)
//                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gold)
//                } else {
//                    tvFeatured.visibility = View.GONE
//                    tvLabel.setBackgroundResource(R.drawable.property_label_background)
//                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gray_low)
//                }
            }
        }
    }

    private fun getRentPrice(context: Context, duration: String, price: Double, currency: String): String {
        return when (duration) {
            RentDuration.DAILY.key -> {
                context.getString(R.string.s_daily_price, formatPrice(price), currency)
            }

            RentDuration.MONTHLY.key -> {
                context.getString(R.string.s_monthly_price, formatPrice(price), currency)
            }

            RentDuration.THREE_MONTHS.key -> {
                context.getString(R.string.s_3_months_price, formatPrice(price), currency)
            }

            RentDuration.SIX_MONTHS.key -> {
                context.getString(R.string.s_6_months_price, formatPrice(price), currency)
            }

            RentDuration.NINE_MONTHS.key -> {
                context.getString(R.string.s_9_months_price, formatPrice(price), currency)
            }

            RentDuration.YEARLY.key -> {
                context.getString(R.string.s_yearly_price, formatPrice(price), currency)
            }

            else -> ""
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onPropertyClick(model: DataX)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(
                oldItem: DataX,
                newItem: DataX
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DataX,
                newItem: DataX
            ) = oldItem == newItem
        }
    }
}