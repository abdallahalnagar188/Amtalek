package eramo.amtalek.presentation.adapters.recyclerview.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemHomeNewsBinding
import eramo.amtalek.domain.model.main.home.NewsModel
import javax.inject.Inject


class RvHomeNewsAdapter @Inject constructor() :
    ListAdapter<NewsModel, RvHomeNewsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemHomeNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemHomeNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onNewsClick(it)
                    }
                }
            }
        }

        fun bind(model: NewsModel) {
            binding.apply {
                tvTitle.text = model.title
                tvBody.text = model.body

                Glide.with(itemView).load(model.imageUrl).placeholder(R.drawable.ic_no_image)
                    .into(ivImage)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onNewsClick(model: NewsModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<NewsModel>() {
            override fun areItemsTheSame(
                oldItem: NewsModel,
                newItem: NewsModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NewsModel,
                newItem: NewsModel
            ) = oldItem == newItem
        }
    }
}