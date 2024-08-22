package eramo.amtalek.presentation.ui.search.searchresult

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
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


class RvSearchResultsPropertiesAdapter @Inject constructor() :
    PagingDataAdapter<PropertyModel, RvSearchResultsPropertiesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private lateinit var favListener:FavClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPropertyPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let {
            if (it != null) {
                holder.bind(it)
            }
        }
    }

    inner class ProductViewHolder(private val binding: ItemPropertyPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        if (it != null) {
                            listener.onPropertyClicks(it)
                        }
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
                if (model.vendorType == "broker") {
                    tvBroker.text = itemView.context.getString(R.string.agency)
                }else{
                    tvBroker.text = itemView.context.getString(R.string.user)
                }
                when (model.rentDuration) {
                    "daily" -> {
                        tvDurationRent.text =  itemView.context.getString(R.string.daily)
                    }
                    "monthly" -> {
                        tvDurationRent.text =  itemView.context.getString(R.string.monthly)
                    }
                    "3_months" -> {
                        tvDurationRent.text =  itemView.context.getString(R.string._3_months)
                    }
                    "6_months" -> {
                        tvDurationRent.text = itemView.context.getString(R.string._6_months)
                    }
                    "9_months" -> {
                        tvDurationRent.text = itemView.context.getString(R.string._9_months)
                    }
                    "yearly" -> {
                        tvDurationRent.text = itemView.context.getString(R.string.yearly)
                    }
                }
                when (model.type) {
                    "for_sale" -> {
                        tvPrice.text = itemView.context.getString(
                            R.string.s_currency,
                            formatPrice(model.sellPrice.toDouble()),
                            model.currency
                        )

                        tvTitle.text = model.title
                        tvArea.text = itemView.context.getString(R.string.s_meter_square, formatNumber(model.area))
                        tvBathroom.text = model.bathroomsCount.toString()
                        tvBed.text = model.bedsCount.toString()
                        tvLocation.text = model.location
                        tvDatePosted.text = model.datePosted
                        tvLabel.text = itemView.context.getString(R.string.for_sell)
                    }
                    "for_rent" -> {
                        tvLabel.text = itemView.context.getString(R.string.for_rent)
                        tvPrice.text = itemView.context.getString(
                            R.string.s_currency,
                            formatPrice(model.rentPrice.toDouble()),
                            model.currency
                        )
                        tvTitle.text = model.title
                        tvArea.text = itemView.context.getString(R.string.s_meter_square, formatNumber(model.area))
                        tvBathroom.text = model.bathroomsCount.toString()
                        tvBed.text = model.bedsCount.toString()
                        tvLocation.text = model.location
                        tvDatePosted.text = model.datePosted
                    }
                    else -> {
                        tvLabel.text = itemView.context.getString(R.string.forBoth)
                        tvPrice.text = itemView.context.getString(
                            R.string.s_currency,
                            formatPrice(model.sellPrice.toDouble()),
                            model.currency
                        )
                        tvDurationRent.text =
                            itemView.context.getString(
                                R.string.s_currency,
                                formatPrice(model.rentPrice.toDouble()),
                                model.currency
                            )
                        tvTitle.text = model.title
                        tvArea.text = itemView.context.getString(R.string.s_meter_square, formatNumber(model.area))
                        tvBathroom.text = model.bathroomsCount.toString()
                        tvBed.text = model.bedsCount.toString()
                        tvLocation.text = model.location
                        tvDatePosted.text = model.datePosted
                    }
                }


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
        fun onPropertyClicks(model: PropertyModel)
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