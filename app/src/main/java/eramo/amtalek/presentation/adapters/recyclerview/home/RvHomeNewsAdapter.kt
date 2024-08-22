package eramo.amtalek.presentation.adapters.recyclerview.home

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemHomeNewsBinding
import eramo.amtalek.domain.model.home.news.NewsModel
import eramo.amtalek.domain.model.main.home.NewsModelx
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
                val htmlContentTitle = model.title ?: ""
                val spannedTextTitle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(htmlContentTitle, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(htmlContentTitle)
                }
                binding.tvTitle.text = spannedTextTitle

                val htmlContent =  model.description ?: ""
                val spannedText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(htmlContent)
                }
                tvBody.text = spannedText

                Glide.with(itemView).load(model.image).placeholder(R.drawable.ic_no_image)
                    .into(ivImage)
                tvDate.text = model.createdAt

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