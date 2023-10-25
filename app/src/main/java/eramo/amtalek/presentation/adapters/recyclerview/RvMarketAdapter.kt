package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemMarketAdsBinding
import eramo.amtalek.databinding.ItemMarketPhotosBinding
import eramo.amtalek.databinding.ItemMarketTextAndPhotosBinding
import eramo.amtalek.databinding.ItemMarketTextBinding
import eramo.amtalek.domain.model.main.market.MarketPostType
import eramo.amtalek.domain.model.main.market.MarketPostsModel
import eramo.amtalek.util.TRUE
import eramo.amtalek.util.formatNumber
import eramo.amtalek.util.formatPrice
import me.thekusch.view.ReadMoreTextView
import javax.inject.Inject


class RvMarketAdapter @Inject constructor() : ListAdapter<MarketPostsModel, RecyclerView.ViewHolder>(PRODUCT_COMPARATOR) {

    private lateinit var listener: OnItemClickListener


    override fun submitList(list: MutableList<MarketPostsModel>?) {
        if (list != null) {
            listMessages = list
        }
        super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return when (listMessages[position].postType) {
            MarketPostType.ADVERTISEMENT -> MarketPostType.ADVERTISEMENT.code
            MarketPostType.TEXT -> MarketPostType.TEXT.code
            MarketPostType.PHOTOS -> MarketPostType.PHOTOS.code
            else -> MarketPostType.TEXT_AND_PHOTOS.code
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MarketPostType.ADVERTISEMENT.code -> {
                AdvertisementViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_market_ads, parent, false))
            }

            MarketPostType.TEXT.code -> {
                TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_market_text, parent, false))
            }

            MarketPostType.PHOTOS.code -> {
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
        fun bind(model: MarketPostsModel)
    }


    inner class AdvertisementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BindView {

        private val binding = ItemMarketAdsBinding.bind(itemView)

        override fun bind(model: MarketPostsModel) {
            binding.apply {

                tvUserName.text = model.userName
                Glide.with(itemView).load(model.userImageUrl).into(ivUserImage)

                tvDatePosted.text = model.datePosted

                Glide.with(itemView).load(model.postImageUrl).into(ivImagePost)

                tvPrice.text = itemView.context.getString(R.string.s_egp, formatPrice(model.price ?: 0.0))

                tvTitle.text = model.postTitle

                tvArea.text = itemView.context.getString(R.string.s_meter_square, formatNumber(model.area ?: 0))
                tvBathroom.text = model.bathroomsCount.toString()
                tvBed.text = model.bedsCount.toString()
                tvLocation.text = model.location

                tvCommentsCount.text = model.commentsCount.toString()
                tvLikesCount.text = model.likesCount.toString()

                if (model.isFavourite == TRUE) {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }
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

        private var binding = ItemMarketTextBinding.bind(itemView)

        override fun bind(model: MarketPostsModel) {
            binding.apply {

                tvUserName.text = model.userName
                Glide.with(itemView).load(model.userImageUrl).into(ivUserImage)

                tvDatePosted.text = model.datePosted

                tvPostBody.text =   model.postBody
                tvPostBody.isExpanded = false
//                tvPostBody.textMode = ReadMoreTextView.Companion.TextMode.LINE.ordinal
//                tvPostBody.anchorPoint = 3

                tvCommentsCount.text = model.commentsCount.toString()
                tvLikesCount.text = model.likesCount.toString()
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

        override fun bind(model: MarketPostsModel) {
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

        override fun bind(model: MarketPostsModel) {
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
        private var listMessages = mutableListOf<MarketPostsModel>()

        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<MarketPostsModel>() {
            override fun areItemsTheSame(
                oldItem: MarketPostsModel,
                newItem: MarketPostsModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MarketPostsModel,
                newItem: MarketPostsModel
            ) = oldItem == newItem
        }
    }
}