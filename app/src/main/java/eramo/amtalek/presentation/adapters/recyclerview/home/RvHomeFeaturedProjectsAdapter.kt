package eramo.amtalek.presentation.adapters.recyclerview.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemFeaturedProjectsBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.util.TRUE
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvHomeFeaturedProjectsAdapter @Inject constructor() :
    ListAdapter<MyFavouritesModel, RvHomeFeaturedProjectsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemFeaturedProjectsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemFeaturedProjectsBinding) :
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

        fun bind(model: MyFavouritesModel) {
            var isFav = model.isFavourite == TRUE
            binding.apply {
                ivFav.setOnClickListener {
                    isFav = !isFav
                    if (isFav) ivFav.setImageResource(R.drawable.ic_heart_fill)
                    else ivFav.setImageResource(R.drawable.ic_heart)
                }

                tvPrice.text = itemView.context.getString(R.string.s_egp, formatPrice(model.price))
                tvTitle.text = model.title

                tvLocation.text = model.location
                tvDatePosted.text = model.datePosted

                Glide.with(itemView)
                    .load(model.imageUrl)
                    .into(ivImage)

                Glide.with(itemView)
                    .load(model.brokerLogoUrl)
                    .into(ivBroker)

                if (model.isFavourite == TRUE) {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onFeaturedRealEstateClick(model: MyFavouritesModel)
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