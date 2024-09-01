package eramo.amtalek.presentation.adapters.recyclerview.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemFeaturedProjectsBinding
import eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
import eramo.amtalek.domain.model.main.home.ProjectModelx
import eramo.amtalek.util.TRUE
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvHomeFeaturedProjectsAdapter @Inject constructor() :
    ListAdapter<ProjectModel, RvHomeFeaturedProjectsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
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
                        listener.onFeaturedProjectClick(it)
                    }
                }
            }
        }

        fun bind(model: ProjectModel) {
            binding.apply {
                tvTitle.text = model.title
                tvDescription.text = model.description
                tvBroker.text = itemView.context.getString(R.string.agency)

                tvLocation.text = model.location
                Log.e("TAGgg", model.location, )
                val price = model.priceFrom?.toDouble()?.let { formatPrice(it) }
                tvPrice.text = itemView.context.getString(R.string.s_baoind,price)

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

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onFeaturedProjectClick(model: ProjectModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<ProjectModel>() {
            override fun areItemsTheSame(
                oldItem: ProjectModel,
                newItem: ProjectModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ProjectModel,
                newItem: ProjectModel
            ) = oldItem == newItem
        }
    }
}