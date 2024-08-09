package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.data.remote.dto.contactedAgent.message.Message
import eramo.amtalek.databinding.ItemChatReceiverBinding
import eramo.amtalek.databinding.ItemChatSenderBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class RvUsersChatAdapter @Inject constructor() :
    ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    private var listener: OnItemClickListener? = null
    private var agentImageUrl: String? = null
    private var senderName: String? = null


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
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("ar")) // Arabic locale
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Output in the device's default locale

            val date = model.messageTime?.let {
                try {
                    inputFormat.parse(it)
                } catch (e: ParseException) {
                    null // Handle the error gracefully
                }
            }
            val formattedTime = date?.let { outputFormat.format(it) } ?: ""

            binding.apply {
                tvMessage.text = model.message
                tvDataSender.text = formattedTime // Set the formatted time
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

            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

            val date = model.messageTime?.let { inputFormat.parse(it) }
            val formattedTime = date?.let { outputFormat.format(it) } ?: "Unknown time"
            binding.apply {
                agentImageUrl?.let {
                    Glide.with(binding.root.context)
                        .load(it)
                        .into(ivProfileMessage)
                }
                tvSender.text = senderName
                // Load the agent image
                tvMessage.text = model.message
                tvData.text = formattedTime // Set the formatted time
                root.setOnClickListener {
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(model)
                    }
                }
            }

        }
    }

    fun setAgentImageUrl(url: String) {
        agentImageUrl = url
    }

    fun setAgentName(name: String) {
        senderName = name
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
