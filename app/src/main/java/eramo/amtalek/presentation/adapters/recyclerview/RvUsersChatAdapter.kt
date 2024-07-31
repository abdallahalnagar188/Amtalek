package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.databinding.ItemChatReceiverBinding
import eramo.amtalek.databinding.ItemChatSenderBinding
import eramo.amtalek.domain.model.social.messaging.ChatMessageModel
import eramo.amtalek.domain.model.social.messaging.ChatMessageType
import javax.inject.Inject


class RvUsersChatAdapter @Inject constructor() :
    ListAdapter<Message, RecyclerView.ViewHolder>(MESSAGE_COMPARATOR) {

    private var listMessages = mutableListOf<Message>()
    private lateinit var listener: OnItemClickListener

    override fun getItemViewType(position: Int): Int {
        return when (listMessages[position].messageType) {
            ChatMessageType.SENDER.toString() -> ChatMessageType.SENDER.code
            ChatMessageType.RECEIVER.toString() -> ChatMessageType.RECEIVER.code
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ChatMessageType.SENDER.code -> {
                SenderViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_chat_sender, parent, false)
                )
            }
            ChatMessageType.RECEIVER.code -> {
                ReceiverViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_chat_receiver, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BindView).bind(getItem(position))
    }

    override fun submitList(list: MutableList<Message>?) {
        if (list != null) {
            listMessages = list
        }
        super.submitList(list)
    }

    interface BindView {
        fun bind(model: Message)
    }

    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BindView {
        private val binding = ItemChatSenderBinding.bind(itemView)

        override fun bind(model: Message) {
            binding.apply {
                tvMessage.text = model.message
            }
        }

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition)?.let {
                        listener.onItemClick(it)
                    }
                }
            }
        }
    }

    inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BindView {
        private val binding = ItemChatReceiverBinding.bind(itemView)

        override fun bind(model: Message) {
            binding.apply {
                tvMessage.text = model.message
            }
        }

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition)?.let {
                        listener.onItemClick(it)
                    }
                }
            }
        }
    }

    fun sendMessage(message: String, messageId: Int, link: String) {
        val list = currentList.toMutableList()
        val newMessage = Message(
            id = messageId,
            message = message,
            link = link,
            // Assuming you want to set the new message as sender type
            messageType = ChatMessageType.SENDER.code.toString(),
            messageTime = System.currentTimeMillis().toString()
        )
        list.add(newMessage)
        submitList(list)
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(model: Message)
    }

    companion object {
        private val MESSAGE_COMPARATOR = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
        }
    }
}
