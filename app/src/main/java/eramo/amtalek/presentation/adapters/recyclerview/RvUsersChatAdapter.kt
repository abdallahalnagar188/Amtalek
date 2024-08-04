package eramo.amtalek.presentation.adapters.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.databinding.ItemChatReceiverBinding
import eramo.amtalek.databinding.ItemChatSenderBinding
import eramo.amtalek.domain.model.social.messaging.ChatMessageType
import eramo.amtalek.presentation.ui.drawer.messaging.MessagingViewModel
import javax.inject.Inject

class RvUsersChatAdapter @Inject constructor() :
    ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    private var listener: OnItemClickListener? = null


    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message.messageType == "sender") {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemChatSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SenderViewHolder(binding)
        } else {
            val binding = ItemChatReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceiverViewHolder(binding)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BindView).bind(getItem(position))
    }

    override fun submitList(list: MutableList<Message>?) {
        super.submitList(list?.toList()) // Create a new list to prevent accidental mutations
    }

    interface BindView {
        fun bind(model: Message)
    }

    inner class SenderViewHolder(private val binding: ItemChatSenderBinding) : RecyclerView.ViewHolder(binding.root), BindView {
        override fun bind(model: Message) {
            binding.apply {
                tvMessage.text = model.message
                root.setOnClickListener {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(model)
                    }
                }
            }
        }
    }

    inner class ReceiverViewHolder(private val binding: ItemChatReceiverBinding) : RecyclerView.ViewHolder(binding.root), BindView {
        override fun bind(model: Message) {
            binding.apply {
                Glide.with(root.context).load(
                    model.link
                ).into(ivProfile)
                tvMessage.text = model.message
                root.setOnClickListener {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(model)
                    }
                }
            }
        }
    }

    fun sendMessage(message: String, messageId: Int, link: String) {
        val newMessage = Message(
            id = messageId,
            message = message,
            link = link,
            messageType = VIEW_TYPE_SENT.toString(),
            messageTime = System.currentTimeMillis().toString()
        )
        val updatedList = currentList.toMutableList().apply { add(newMessage) }
        submitList(updatedList)
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(model: Message)
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}
