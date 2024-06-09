package eramo.amtalek.presentation.adapters.recyclerview.offers

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
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvHotOffersForBothPropertiesAdapter @Inject constructor() :
    ListAdapter<PropertyModel, RvHotOffersForBothPropertiesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private lateinit var favListener:FavClickListener

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
                }
                tvLabel.text = itemView.context.getString(R.string.for_rent)
                tvPrice.text = itemView.context.getString(R.string.s_egp, formatPrice(model.sellPrice.toDouble()))
                tvTitle.text = model.title
                tvLabel.text = model.type
                tvArea.text = itemView.context.getString(R.string.s_meter_square, formatNumber(model.area))
                tvBathroom.text = model.bathroomsCount.toString()
                tvBed.text = model.bedsCount.toString()
                tvLocation.text = model.location
                tvDatePosted.text = model.datePosted
                tvLabel.text = itemView.context.getString(R.string.forBoth)

                Glide.with(itemView)
                    .load(model.imageUrl)
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
                if (UserUtil.getUserType()=="broker"){
                    ivFav.visibility = View.GONE
                }
                else {
                    ivFav.visibility = View.VISIBLE
                }
            }
        }
    }

    fun setListener(listener: OnItemClickListener,favClickListener: FavClickListener) {
        this.listener = listener
        this.favListener = favClickListener
    }

    interface OnItemClickListener {
        fun onPropertyClick(model: PropertyModel)
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