package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.brokersDetails.Project
import eramo.amtalek.databinding.ItemLatestProjectsBinding
import eramo.amtalek.databinding.ItemProjectPreviewInSeeMoreBinding
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvCompletedProjectsAdapter @Inject constructor() :
    ListAdapter<Project, RvCompletedProjectsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
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

        fun bind(model: Project) {
            binding.apply {
                tvTitle.text = model.title
                tvDescription.text = model.description
                val price = model.price_from?.toDouble()?.let { formatPrice(it) }
                tvPrice.text = itemView.context.getString(R.string.s_egp,price)
                tvBroker.text = model.agent_data?.name
//                tvLocation.text = model.country
//                tvDatePosted.text = model.createdAt

                Glide.with(itemView)
                    .load(model.image)
                    .placeholder(R.drawable.ic_no_image)
                    .into(ivImage)

                Glide.with(itemView)
                    .load(model.agent_data?.logo)
                    .into(ivBroker)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onProjectClick(model: Project)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Project>() {
            override fun areItemsTheSame(
                oldItem: Project,
                newItem: Project
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: Project,
                newItem: Project
            ) = oldItem == newItem
        }
    }
}