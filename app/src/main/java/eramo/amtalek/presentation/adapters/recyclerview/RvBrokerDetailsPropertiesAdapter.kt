package eramo.amtalek.presentation.adapters.recyclerview

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
import eramo.amtalek.databinding.ItemPropertyPreviewBinding
import eramo.amtalek.presentation.ui.interfaces.FavClickListenerOriginalItem
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvBrokerDetailsPropertiesAdapter @Inject constructor() :
    ListAdapter<OriginalItem, RvBrokerDetailsPropertiesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private lateinit var favListener: FavClickListenerOriginalItem


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
                if (model.forWhat == "for_sale"){
                    tvPrice.text = itemView.context.getString(
                        R.string.s_egp,
                        formatPrice( model.salePrice?.toDouble() ?: 0.0)
                    )
                }else{
                    tvPrice.text = itemView.context.getString(
                        R.string.s_egp,
                        formatPrice( model.rentPrice?.toDouble() ?: 0.0)
                    )
                }
//                binding.tvBroker.text = model.brokerDetails?.brokerType
//                tvPrice.text = itemView.context.getString(
//                    R.string.s_egp,
//                    formatPrice( model.salePrice?.toDouble() ?: 0.0)
//                )
//                tvDurationRent.text = itemView.context.getString(
//                    R.string.s_egp,
//                    formatPrice( model.rentPrice?.toDouble() ?: 0.0)
//                )
                tvTitle.text = model.title
                tvLabel.text = (if (model.forWhat.equals("for_sale")){
                    itemView.context.getString(R.string.for_sell)
                } else {
                    itemView.context.getString(R.string.for_rent)
                }).toString()
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
                if (model.isFav == "1") {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }
                if (UserUtil.getUserType()=="broker"){
                    ivFav.visibility = View.GONE
                }
                else {
                    ivFav.visibility = View.VISIBLE
                }

                if (model.priority == "featured") {
                    tvFeatured.visibility = View.VISIBLE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background_gold)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gold)
                    binding.tvFeatured.text = itemView.context.getString(R.string.featured)

                    //ivFav.setImageResource(R.drawable.ic_heart_fill)

                } else {
                    tvFeatured.visibility = View.GONE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gray_low)

                   // ivFav.setImageResource(R.drawable.ic_heart)

                }
            }
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