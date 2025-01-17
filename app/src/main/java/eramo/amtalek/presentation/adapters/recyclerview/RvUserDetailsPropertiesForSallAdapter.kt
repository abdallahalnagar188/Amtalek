package eramo.amtalek.presentation.adapters.recyclerview

import android.annotation.SuppressLint
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
import eramo.amtalek.data.remote.dto.brokersProperties.OriginalItem
import eramo.amtalek.data.remote.dto.userDetials.SubmittedPropsForRent
import eramo.amtalek.data.remote.dto.userDetials.SubmittedPropsForSale
import eramo.amtalek.databinding.ItemPropertyPreviewBinding
import eramo.amtalek.presentation.ui.interfaces.FavClickListenerOriginalItem
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.enum.PropertyType
import eramo.amtalek.util.enum.RentDuration
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject

class RvUserDetailsPropertiesForSallAdapter @Inject constructor() :
    ListAdapter<Any, RvUserDetailsPropertiesForSallAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {

    private lateinit var listener: OnItemClickListener
    private lateinit var favListener: FavClickListenerOriginalItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPropertyPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is SubmittedPropsForSale -> holder.bindSale(item)
            is SubmittedPropsForRent -> holder.bindRent(item)
        }
    }

    inner class ProductViewHolder(private val binding: ItemPropertyPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(bindingAdapterPosition)
                    when (item) {
                        is SubmittedPropsForSale -> listener.onPropertyClick(item)
                        is SubmittedPropsForRent -> listener.onPropertyClick(item)
                    }
                }
            }
        }

        fun bindSale(model: SubmittedPropsForSale) {
            var isFav = model.is_fav
            binding.apply {
                ivFav.setOnClickListener {
                    favListener.onFavClick(model)

                    if(UserUtil.isUserLogin()){
                        isFav = if (isFav == "0") {
                            ivFav.setImageResource(R.drawable.ic_heart_fill)
                            "1"
                        } else {
                            ivFav.setImageResource(R.drawable.ic_heart)
                            "0"
                        }
                    }
                }

                if (isFav == "1") {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }

                tvPrice.text = itemView.context.getString(
                    R.string.s_egp,
                    model.sale_price?.let { formatPrice(it.toDouble()) }
                )
              if(model.rent_price==0){
                  tvDurationRent.visibility = View.GONE
              }
                tvPrice.visibility = View.VISIBLE
                tvDurationRent.text = itemView.context.getString(
                    R.string.s_egp,
                    formatPrice(model.rent_price?.toDouble() ?: 0.0)
                )
                if(model.for_what == "for_sale"){
                    tvLabel.text = itemView.context.getString(R.string.for_sell)
                }else if(model.for_what == "for_rent"){
                    tvLabel.text = itemView.context.getString(R.string.for_rent)
                }else{
                    tvLabel.text = itemView.context.getString(R.string.forBoth)
                }
                tvTitle.text = model.title
              //  tvLabel.text = itemView.context.getString(R.string.for_sell)
                tvArea.text = itemView.context.getString(
                    R.string.s_meter_square,
                    model.land_area?.let { formatNumber(it) }
                )
                tvBathroom.text = model.bath_room_no.toString()
                tvBed.text = model.bed_rooms_no.toString()
                tvLocation.text = model.address
                //tvDatePosted.text = model.created_at

                tvBroker.text = itemView.context.getString(R.string.user)
                when (model.for_what) {
                    PropertyType.FOR_SELL.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvDurationRent.visibility = View.GONE
                    }

                    PropertyType.FOR_RENT.key -> {
                        tvPrice.visibility = View.GONE
                        tvDurationRent.visibility = View.VISIBLE
                        tvDurationRent.text = model.rent_duration?.let {
                            model.currency?.let { it1 ->
                                model.rent_price?.toDouble()?.let { it2 ->
                                    getRentPrice(itemView.context, it, it2, it1)
                                }
                            }
                        }
                    }

                    PropertyType.FOR_BOTH.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvDurationRent.visibility = View.VISIBLE
                        tvDurationRent.text = model.rent_duration?.let {
                            model.currency?.let { it1 ->
                                model.rent_price?.let { it2 ->
                                    getRentPrice(itemView.context, it, it2.toDouble(), it1)
                                }
                            }
                        }
                    }

                    else -> {}
                }

                Glide.with(itemView)
                    .load(model.primary_image)
                    .into(ivImage)
                Glide.with(itemView).load(model.broker_details?.get(0)?.logo).into(ivBroker)

                if (model.normal_featured == "featured") {
                    tvFeatured.visibility = View.VISIBLE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background_gold)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gold)
                    binding.tvFeatured.text = itemView.context.getString(R.string.featured)
                } else {
                    tvFeatured.visibility = View.GONE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gray_low)
                }
            }
        }

        fun bindRent(model: SubmittedPropsForRent) {
            var isFav = model.is_fav ?: "0"
            val root = binding.root

            binding.apply {
                ivFav.setOnClickListener {
                    isFav = if (isFav == "0") {
                        ivFav.setImageResource(R.drawable.ic_heart_fill)
                        "1"
                    } else {
                        ivFav.setImageResource(R.drawable.ic_heart)
                        "0"
                    }
                }

                if (isFav == "1") {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }
                if(model.for_what == "for_sale"){
                    tvLabel.text = itemView.context.getString(R.string.for_sell)
                }else if(model.for_what == "for_rent"){
                    tvLabel.text = itemView.context.getString(R.string.for_rent)
                }else{
                    tvLabel.text = itemView.context.getString(R.string.forBoth)
                }


                when (model.for_what) {
                    PropertyType.FOR_SELL.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvDurationRent.visibility = View.GONE
                    }

                    PropertyType.FOR_RENT.key -> {
                        tvPrice.visibility = View.GONE
                        tvDurationRent.visibility = View.VISIBLE
                        tvDurationRent.text = model.rent_duration?.let {
                            model.currency?.let { it1 ->
                                model.rent_price?.toDouble()?.let { it2 ->
                                    getRentPrice(itemView.context, it, it2, it1)
                                }
                            }
                        }
                    }

                    PropertyType.FOR_BOTH.key -> {
                        tvPrice.visibility = View.VISIBLE
                        tvDurationRent.visibility = View.VISIBLE
                        tvDurationRent.text = model.rent_duration?.let {
                            model.currency?.let { it1 ->
                                model.rent_price?.let { it2 ->
                                    getRentPrice(itemView.context, it, it2.toDouble(), it1)
                                }
                            }
                        }
                    }

                    else -> {}
                }
                tvPrice.visibility = View.GONE
                tvDurationRent.visibility = View.VISIBLE
//                tvPrice.text = itemView.context.getString(
//                    R.string.s_egp,
//                    model.sale_price?.let { formatPrice(it.toDouble()) }
//                )
//                if(model.sale_price =="0.0"){
//                    tvPrice.visibility = View.GONE
//                }
//                tvDurationRent.text = itemView.context.getString(
//                    R.string.s_egp,
//                    formatPrice(model.rent_price?.toDouble() ?: 0.0)
//                )
                tvTitle.text = model.title
               // tvLabel.text = itemView.context.getString(R.string.for_rent)
                tvArea.text = itemView.context.getString(
                    R.string.s_meter_square,
                    model.land_area?.let { formatNumber(it) }
                )
                tvBathroom.text = model.bath_room_no.toString()
                tvBed.text = model.bed_rooms_no.toString()
                tvLocation.text = model.address
               // tvDatePosted.text = model.created_at
                tvBroker.text = itemView.context.getString(R.string.user)

                Glide.with(itemView)
                    .load(model.primary_image)
                    .into(ivImage)
                Glide.with(itemView).load(model.broker_details?.get(0)?.logo).into(ivBroker)

                if (model.normal_featured == "featured") {
                    tvFeatured.visibility = View.VISIBLE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background_gold)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gold)
                    binding.tvFeatured.text = itemView.context.getString(R.string.featured)
                } else {
                    tvFeatured.visibility = View.GONE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gray_low)
                }
            }
        }
            // Similar bind logic for SubmittedPropsForRent
            // Adjust this method based on your UI needs

    }

    private fun getRentPrice(context: Context, duration: String, price: Double, currency: String): String {
        return when (duration) {
            RentDuration.DAILY.key -> context.getString(R.string.s_daily_price, formatPrice(price), currency)
            RentDuration.MONTHLY.key -> context.getString(R.string.s_monthly_price, formatPrice(price), currency)
            RentDuration.THREE_MONTHS.key -> context.getString(R.string.s_3_months_price, formatPrice(price), currency)
            RentDuration.SIX_MONTHS.key -> context.getString(R.string.s_6_months_price, formatPrice(price), currency)
            RentDuration.NINE_MONTHS.key -> context.getString(R.string.s_9_months_price, formatPrice(price), currency)
            RentDuration.YEARLY.key -> context.getString(R.string.s_yearly_price, formatPrice(price), currency)
            else -> ""
        }
    }

    fun setListener(
        listener: OnItemClickListener,
        favListener: FavClickListenerOriginalItem
    ) {
        this.listener = listener
        this.favListener = favListener
    }

    interface OnItemClickListener {
        fun onPropertyClick(model: Any)
    }
    interface FavClickListenerOriginalItem{
        fun onFavClick(model:SubmittedPropsForSale)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }
        }
    }
}
