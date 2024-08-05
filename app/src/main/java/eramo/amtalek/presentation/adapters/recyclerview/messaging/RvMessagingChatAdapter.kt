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
    ListAdapter<Data, RvMessagingChatAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemMessagingChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemMessagingChatBinding) :
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
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onChatClick(model: Data)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(
                oldItem: Data,
                newItem: Data
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: Data,
                newItem: Data
            ) = oldItem == newItem
        }
    }
}