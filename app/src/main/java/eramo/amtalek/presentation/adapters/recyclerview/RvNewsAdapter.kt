package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemProjectBinding
import eramo.amtalek.domain.model.home.news.NewsModel
import javax.inject.Inject


class RvNewsAdapter @Inject constructor() :
    ListAdapter<NewsModel, RvNewsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemProjectBinding) :
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

        fun bind(model: NewsModel) {
            binding.apply {
                tvTitle.text = model.title
                tvDesc.text = model.description


//                tvLocation.text = model.country
//                tvDatePosted.text = model.createdAt

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
        fun onPropertyClick(model: NewsModel)
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