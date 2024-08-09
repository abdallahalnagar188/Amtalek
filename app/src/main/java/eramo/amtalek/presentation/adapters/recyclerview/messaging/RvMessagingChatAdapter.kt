package eramo.amtalek.presentation.adapters.recyclerview.messaging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.data.remote.dto.contactedAgent.Data
import eramo.amtalek.databinding.ItemMessagingChatBinding
import eramo.amtalek.domain.model.drawer.MessagingChatModel
import javax.inject.Inject


class RvMessagingChatAdapter @Inject constructor() :
    ListAdapter<Data, RecyclerView.ViewHolder>(PRODUCT_COMPARATOR) {

    private lateinit var listener: OnItemClickListener

    companion object {
        private const val VIEW_TYPE_VALID = 1
        private const val VIEW_TYPE_BLURRED = 2

        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data) = oldItem == newItem

            override fun areContentsTheSame(oldItem: Data, newItem: Data) = oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).messageType == "valid") {
            VIEW_TYPE_VALID
        } else {
            VIEW_TYPE_BLURRED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_VALID -> {
                val binding = ItemMessagingChatBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ValidMessageViewHolder(binding)
            }
            VIEW_TYPE_BLURRED -> {
                val binding = ItemMessagingChatBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                BlurredMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is ValidMessageViewHolder -> holder.bind(message)
            is BlurredMessageViewHolder -> holder.bind(message)
        }
    }

    inner class ValidMessageViewHolder(private val binding: ItemMessagingChatBinding) :
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

        fun bind(model: Data) {
            binding.apply {
                tvName.text = model.name
                Glide.with(itemView).load(model.image).into(ivImage)
                // Other binding logic for valid messages
            }
        }
    }

    inner class BlurredMessageViewHolder(private val binding: ItemMessagingChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                // Optionally prevent clicking on blurred messages or show a toast
                listener.onChatClick(getItem(bindingAdapterPosition))
            }
        }

        fun bind(model: Data) {
            binding.apply {
                tvName.text = model.name
                Glide.with(itemView).load(model.image).into(ivImage)
                // Set text and image with low opacity for blurred messages
                tvName.alpha = 0.5f
                ivImage.alpha = 0.5f
                // Additional UI changes for blurred messages
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onChatClick(model: Data)
    }
}
