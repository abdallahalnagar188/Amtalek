package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.myHome.news.allnews.DataX
import eramo.amtalek.databinding.ItemProjectBinding
import javax.inject.Inject

class RvNewsAdapter @Inject constructor() :
    PagingDataAdapter<DataX, RvNewsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition)?.let {
                        listener.onNewsClick(it)
                    }
                }
            }
        }

        fun bind(model: DataX) {
            binding.apply {
                tvTitle.text = model.title
                tvDesc.text = model.description

                // Optionally handle additional data fields if needed
                // tvLocation.text = model.country
                // tvDatePosted.text = model.createdAt

                Glide.with(itemView)
                    .load(model.image)
                    .placeholder(R.drawable.ic_no_image)
                    .into(ivNews)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onNewsClick(model: DataX)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(
                oldItem: DataX,
                newItem: DataX
            ) = oldItem.id == newItem.id  // Assuming each NewsModel has a unique ID

            override fun areContentsTheSame(
                oldItem: DataX,
                newItem: DataX
            ) = oldItem == newItem
        }
    }
}
