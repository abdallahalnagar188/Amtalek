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
import eramo.amtalek.data.remote.dto.userDetials.Data
import eramo.amtalek.databinding.ItemPropertyPreviewBinding
import eramo.amtalek.presentation.ui.interfaces.FavClickListenerOriginalItem
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvUserDetailsPropertiesAdapter @Inject constructor() :
    ListAdapter<Data, RvUserDetailsPropertiesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
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

        fun bind(model: Data) {
            var isFav = model.submitted_props_for_sale?.get(0)?.is_fav
            binding.apply {
        //       favListener.onFavClick(model)
                ivFav.setOnClickListener {
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

                tvPrice.text = itemView.context.getString(
                    R.string.s_egp,
                    model.submitted_props_for_sale?.get(0)?.sale_price?.let { formatPrice( it.toDouble()) }
                )
                tvDurationRent.text = itemView.context.getString(
                    R.string.s_egp,
              //      formatPrice( model.submitted_props_for_rent?.get(0)?.rent_price?.toDouble() ?: 0.0)
                )
                tvTitle.text = model.submitted_props_for_sale?.get(0)?.title
                tvLabel.text = (if (model.submitted_props_for_sale?.get(0)?.for_what == "for_sale"){
                    itemView.context.getString(R.string.for_sell)
                } else {
                    itemView.context.getString(R.string.for_rent)
                }).toString()
                tvArea.text = itemView.context.getString(R.string.s_meter_square,
                    model.submitted_props_for_sale?.get(0)?.land_area?.let { formatNumber(it) })
                tvBathroom.text = model.submitted_props_for_sale?.get(0)?.bath_room_no.toString()
                tvBed.text = model.submitted_props_for_sale?.get(0)?.bed_rooms_no.toString()
                tvLocation.text = model.submitted_props_for_sale?.get(0)?.address
                tvDatePosted.text = model.submitted_props_for_sale?.get(0)?.created_at

                Glide.with(itemView)
                    .load(model.submitted_props_for_sale?.get(0)?.primary_image)
                    .into(ivImage)
                Glide.with(itemView).load(model.submitted_props_for_sale?.get(0)?.broker_details?.get(0)?.logo).into(ivBroker)

                if (model.submitted_props_for_sale?.get(0)?.is_fav == "") {
                    tvFeatured.visibility = View.VISIBLE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background_gold)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gold)
                    binding.tvFeatured.text = itemView.context.getString(R.string.featured)

                    ivFav.setImageResource(R.drawable.ic_heart_fill)

                } else {
                    tvFeatured.visibility = View.GONE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gray_low)

                    ivFav.setImageResource(R.drawable.ic_heart)

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
        fun onPropertyClick(model: Data)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(
                oldItem: Data,
                newItem: Data
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: Data,
                newItem: Data
            ) = oldItem == newItem
        }
    }
}