package eramo.amtalek.presentation.adapters.recyclerview

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
import eramo.amtalek.databinding.ItemSeeMoreProjectsBinding
import eramo.amtalek.databinding.ItemSeeMorePropertiesByCityBinding
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvProjectsAdapter @Inject constructor() :
    ListAdapter<DataX, RvProjectsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
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
                        listener.onPropertyClick(it)
                    }
                }
            }
        }

        fun bind(model: DataX) {
            binding.apply {
                tvTitle.text = model.title
                tvDescription.text =model.city + " - " + model.region
                val price = model.priceFrom?.toDouble()?.let { formatPrice(it) }
                tvPrice.text = itemView.context.getString(R.string.s_egp,price)

//                tvLocation.text = model.country
//                tvDatePosted.text = model.createdAt

                Glide.with(itemView)
                    .load(model.image)
                    .placeholder(R.drawable.ic_no_image)
                    .into(ivImage)

                Glide.with(itemView)
                    .load(model.agentData?.get(0)?.logo)
                    .into(ivBroker)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onPropertyClick(model: DataX)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(
                oldItem: DataX,
                newItem: DataX
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DataX,
                newItem: DataX
            ) = oldItem == newItem
        }
    }
}