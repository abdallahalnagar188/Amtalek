package eramo.amtalek.presentation.adapters.recyclerview.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.project.allProjects.DataX
import eramo.amtalek.databinding.ItemProjectPreviewBinding
import eramo.amtalek.databinding.ItemProjectPreviewInSeeMoreBinding
import eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
import eramo.amtalek.util.formatPrice
import javax.inject.Inject

class RvHotOffersForBothProjectsAdapter @Inject constructor() :
    ListAdapter<ProjectModel, RvHotOffersForBothProjectsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemProjectPreviewInSeeMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemProjectPreviewInSeeMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onProjectClick(it)
                    }
                }
            }
        }


            fun bind(model: ProjectModel) {
                binding.apply {
                    tvTitle.text = model.title
                    tvDescription.text =model.location
                    val price = model.priceFrom?.toDouble()?.let { formatPrice(it) }
                    tvPrice.text = itemView.context.getString(R.string.s_egp,price)

//                tvLocation.text = model.country
//                tvDatePosted.text = model.createdAt

                    Glide.with(itemView)
                        .load(model.imageUrl)
                        .placeholder(R.drawable.ic_no_image)
                        .into(ivImage)

                    Glide.with(itemView)
                        .load(model.brokerLogoUrl)
                        .into(ivBroker)
                }
            }

    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onProjectClick(model: ProjectModel)
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