package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemChatReceiverBinding
import eramo.amtalek.databinding.ItemChatSenderBinding
import eramo.amtalek.domain.model.social.messaging.ChatMessageModel
import eramo.amtalek.domain.model.social.messaging.ChatMessageType
import javax.inject.Inject


class RvUsersChatAdapter @Inject constructor() :
    ListAdapter<ChatMessageModel, RecyclerView.ViewHolder>(PRODUCT_COMPARATOR) {

    private lateinit var listener: OnItemClickListener

    override fun submitList(list: MutableList<ChatMessageModel>?) {
        if (list != null) {
            listMessages = list
        }
        super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return when (listMessages[position].type) {
            ChatMessageType.SENDER -> ChatMessageType.SENDER.code
            ChatMessageType.RECEIVER -> ChatMessageType.RECEIVER.code
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ChatMessageType.SENDER.code -> {
                SenderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_sender, parent, false))
            }

            else -> {
                ReceiverViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_chat_receiver, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BindView).bind(getItem(position))
    }

    interface BindView {
        fun bind(model: ChatMessageModel)
    }

    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BindView {

        private val binding = ItemChatSenderBinding.bind(itemView)

        override fun bind(model: ChatMessageModel) {
            binding.apply {
                tvMessage.text = model.message
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

    inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BindView {

        private var binding = ItemChatReceiverBinding.bind(itemView)

        override fun bind(model: ChatMessageModel) {
            binding.apply {
                tvMessage.text = model.message
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

    fun sendMessage(message: String) {
        val list = currentList.toMutableList()

        list.add(
            ChatMessageModel(
                ChatMessageType.RECEIVER, message
            )
        )

        submitList(list)
    }


    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        //        fun onHeaderClick(model: DataModel)
        fun onPhotosClickPhotosPost(model: ChatMessageModel)
    }

    // DiffCallback
    companion object {
        private var listMessages = mutableListOf<ChatMessageModel>()

        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<ChatMessageModel>() {
            override fun areItemsTheSame(
                oldItem: ChatMessageModel,
                newItem: ChatMessageModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ChatMessageModel,
                newItem: ChatMessageModel
            ) = oldItem == newItem
        }
    }
}