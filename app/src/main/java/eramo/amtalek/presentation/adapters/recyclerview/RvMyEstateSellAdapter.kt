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
import eramo.amtalek.databinding.ItemMyEstateSellBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.util.TRUE
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvMyEstateSellAdapter @Inject constructor() :
    ListAdapter<MyFavouritesModel, RvMyEstateSellAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemMyEstateSellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemMyEstateSellBinding) :
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

        fun bind(model: MyFavouritesModel) {
            binding.apply {


                tvPrice.text = itemView.context.getString(R.string.s_egp, formatPrice(model.price))
                tvTitle.text = model.title
//                tvLabel.text = model.type
                tvArea.text = itemView.context.getString(R.string.s_meter_square, formatNumber(model.area))
                tvBathroom.text = model.bathroomsCount.toString()
                tvBed.text = model.bedsCount.toString()
                tvLocation.text = model.location
                tvDatePosted.text = model.datePosted

                Glide.with(itemView)
                    .load(model.imageUrl)
                    .into(ivImage)

                if (model.isFeatured == TRUE) {
                    tvFeatured.visibility = View.VISIBLE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background_gold)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gold)
                } else {
                    tvFeatured.visibility = View.GONE
                    tvLabel.setBackgroundResource(R.drawable.property_label_background)
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.gray_low)
                }

                ivEdit.setOnClickListener { listener.onEditPropertyClick(model) }
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onPropertyClick(model: MyFavouritesModel)
        fun onEditPropertyClick(model: MyFavouritesModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<MyFavouritesModel>() {
            override fun areItemsTheSame(
                oldItem: MyFavouritesModel,
                newItem: MyFavouritesModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MyFavouritesModel,
                newItem: MyFavouritesModel
            ) = oldItem == newItem
        }
    }
}