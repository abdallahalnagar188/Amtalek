package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.project.BrokerDetail
import eramo.amtalek.data.remote.dto.project.Data
import eramo.amtalek.data.remote.dto.project.Project
import eramo.amtalek.data.remote.dto.project.ProjectDetailsResponse
import eramo.amtalek.data.remote.dto.property.allproperty.AllPropertyResponse
import eramo.amtalek.data.remote.dto.property.allproperty.DataX
import eramo.amtalek.databinding.ItemProjectPreviewBinding
import eramo.amtalek.domain.model.main.home.ProjectModelx
import eramo.amtalek.util.TRUE
import javax.inject.Inject


class RvProjectsAdapter @Inject constructor() :
    ListAdapter<DataX, RvProjectsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemProjectPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemProjectPreviewBinding) :
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
                tvDescription.text = model.description

                tvLocation.text = model.address
                tvDatePosted.text = model.createdAt

                Glide.with(itemView)
                    .load(model.primaryImage)
                    .placeholder(R.drawable.ic_no_image)
                    .into(ivImage)

                Glide.with(itemView)
                    .load(model.brokerDetails?.get(0)?.logo)
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