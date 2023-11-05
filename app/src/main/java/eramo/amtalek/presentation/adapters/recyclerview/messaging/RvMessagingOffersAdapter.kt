package eramo.amtalek.presentation.adapters.recyclerview.messaging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemMessagingOffersBinding
import eramo.amtalek.domain.model.drawer.MessagingOffersModel
import eramo.amtalek.util.formatPrice
import javax.inject.Inject


class RvMessagingOffersAdapter @Inject constructor() :
    ListAdapter<MessagingOffersModel, RvMessagingOffersAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemMessagingOffersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemMessagingOffersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onChatClick(it)
                    }
                }
            }
        }

        fun bind(model: MessagingOffersModel) {
            binding.apply {

                tvNameSender.text = model.senderName
                tvMessageSender.text = model.lastMessage
                tvCount.text = model.unseenCount.toString()
                tvDate.text = model.date

                tvOfferTitle.text = model.offerTitle
                tvOfferPrice.text = itemView.context.getString(R.string.s_egp, formatPrice(model.offerPrice.toDouble()))

                Glide.with(itemView).load(model.senderImageUrl).into(ivImageSender)
                Glide.with(itemView).load(model.offerImageUrl).into(ivImageOffer)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onChatClick(model: MessagingOffersModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<MessagingOffersModel>() {
            override fun areItemsTheSame(
                oldItem: MessagingOffersModel,
                newItem: MessagingOffersModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MessagingOffersModel,
                newItem: MessagingOffersModel
            ) = oldItem == newItem
        }
    }
}