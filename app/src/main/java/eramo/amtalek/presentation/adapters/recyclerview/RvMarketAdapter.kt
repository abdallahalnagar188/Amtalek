package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemMarketAdsBinding
import eramo.amtalek.databinding.ItemMarketPhotosBinding
import eramo.amtalek.databinding.ItemMarketTextAndPhotosBinding
import eramo.amtalek.databinding.ItemMarketTextBinding
import eramo.amtalek.util.MarketPostType
import eramo.amtalek.util.MarketPostsTypeModel
import javax.inject.Inject


class RvMarketAdapter @Inject constructor() : ListAdapter<MarketPostsTypeModel, RecyclerView.ViewHolder>(PRODUCT_COMPARATOR) {

    private lateinit var listener: OnItemClickListener


    override fun submitList(list: MutableList<MarketPostsTypeModel>?) {
        if (list != null) {
            listMessages = list
        }
        super.submitList(list)
    }

    //    override fun getItemViewType(position: Int): Int {
//        return when (listMessages[position].postType) {
//            ADVERTISEMENT -> ADVERTISEMENT
//            TEXT -> TEXT
//            PHOTOS -> PHOTOS
//            else -> TEXT_AND_PHOTOS
//        }
//    }
    override fun getItemViewType(position: Int): Int {
        return when (listMessages[position].postType) {
            MarketPostType.ADVERTISEMENT -> MarketPostType.ADVERTISEMENT.ordinal
            MarketPostType.TEXT -> MarketPostType.TEXT.ordinal
            MarketPostType.PHOTOS -> MarketPostType.PHOTOS.ordinal
            else -> MarketPostType.TEXT_AND_PHOTOS.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MarketPostType.ADVERTISEMENT.ordinal -> {
                AdvertisementViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_market_ads, parent, false))
            }

            MarketPostType.TEXT.ordinal -> {
                TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_market_text, parent, false))
            }

            MarketPostType.PHOTOS.ordinal -> {
                PhotosViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_market_photos, parent, false))
            }

            else -> {
                TextAndPhotosViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_market_text_and_photos, parent, false))
            }
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BindView).bind(getItem(position))
    }

    interface BindView {
        fun bind(model: MarketPostsTypeModel)
    }


    inner class AdvertisementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BindView {

        private val binding = ItemMarketAdsBinding.bind(itemView)

        override fun bind(model: MarketPostsTypeModel) {
            binding.apply {

            }
        }

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
//                        listener.onHeaderClick(it)
                    }
                }
            }
        }
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BindView {

        private val binding = ItemMarketTextBinding.bind(itemView)

        override fun bind(model: MarketPostsTypeModel) {
            binding.apply {

            }
        }

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
//                        listener.onItemClick(it)
                    }
                }
            }
        }
    }

    inner class PhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BindView {

        private val binding = ItemMarketPhotosBinding.bind(itemView)

        override fun bind(model: MarketPostsTypeModel) {
            binding.apply {

            }
        }

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
//                        listener.onItemClick(it)
                    }
                }
            }
        }
    }

    inner class TextAndPhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BindView {

        private val binding = ItemMarketTextAndPhotosBinding.bind(itemView)

        override fun bind(model: MarketPostsTypeModel) {
            binding.apply {

            }
        }

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
//                        listener.onItemClick(it)
                    }
                }
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
//        fun onHeaderClick(model: DataModel)
//        fun onItemClick(model: DataModel)
    }

    // DiffCallback
    companion object {
        private var listMessages = mutableListOf<MarketPostsTypeModel>()

        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<MarketPostsTypeModel>() {
            override fun areItemsTheSame(
                oldItem: MarketPostsTypeModel,
                newItem: MarketPostsTypeModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MarketPostsTypeModel,
                newItem: MarketPostsTypeModel
            ) = oldItem == newItem
        }
    }
}