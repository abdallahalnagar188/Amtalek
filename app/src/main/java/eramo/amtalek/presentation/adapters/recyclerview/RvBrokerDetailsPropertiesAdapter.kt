package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.brokersProperties.OriginalItem
import eramo.amtalek.databinding.ItemPropertyPreviewBinding
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import eramo.amtalek.presentation.ui.interfaces.FavClickListenerOriginalItem
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvBrokerDetailsPropertiesAdapter @Inject constructor() :
    ListAdapter<OriginalItem, RvBrokerDetailsPropertiesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    //private lateinit var favListener: FavClickListenerOriginalItem


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

        fun bind(model: OriginalItem) {
            var isFav = model.isFav
            binding.apply {
//                ivFav.setOnClickListener {
//                    favListener.onFavClick(model)
//                    if (isFav =="0") {ivFav.setImageResource(R.drawable.ic_heart_fill)
//                        isFav = "1"
//                    }
//                    else {ivFav.setImageResource(R.drawable.ic_heart)
//                        isFav ="0"
//                    }
//                }
//                if (isFav == "1") {
//                    ivFav.setImageResource(R.drawable.ic_heart_fill)
//                } else {
//                    ivFav.setImageResource(R.drawable.ic_heart)
//                }

                tvPrice.text = itemView.context.getString(
                    R.string.s_egp,
                    model.salePrice ?: model.rentPrice
                )
                tvTitle.text = model.title
                tvLabel.text = model.propertyType
                tvArea.text = itemView.context.getString(R.string.s_meter_square,
                    model.landArea?.let { formatNumber(it) })
                tvBathroom.text = model.bathRoomNo.toString()
                tvBed.text = model.bedRoomsNo.toString()
                tvLocation.text = model.address
                tvDatePosted.text = model.createdAt

                Glide.with(itemView)
                    .load(model.primaryImage)
                    .into(ivImage)
                Glide.with(itemView).load(model.brokerDetails?.companyLogo).into(ivBroker)

                if (model.isFav == "") {
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

    fun setListener(
        listener: OnItemClickListener,
///        favListener: FavClickListenerOriginalItem
    ) {
        this.listener = listener
        //this.favListener = favListener
    }

    interface OnItemClickListener {
        fun onPropertyClick(model: OriginalItem)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<OriginalItem>() {
            override fun areItemsTheSame(
                oldItem: OriginalItem,
                newItem: OriginalItem
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: OriginalItem,
                newItem: OriginalItem
            ) = oldItem == newItem
        }
    }
}