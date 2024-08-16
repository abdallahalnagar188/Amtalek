package eramo.amtalek.presentation.adapters.recyclerview.messaging

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eightbitlab.com.blurview.RenderScriptBlur
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
            override fun areItemsTheSame(oldItem: Data, newItem: Data) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Data, newItem: Data) = oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).messageType == "valid") VIEW_TYPE_VALID else VIEW_TYPE_BLURRED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMessagingChatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return if (viewType == VIEW_TYPE_VALID) {
            ValidMessageViewHolder(binding)
        } else {
            BlurredMessageViewHolder(binding)
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
                    listener.onChatClick(getItem(bindingAdapterPosition))
                }
            }
        }

        fun bind(model: Data) {
            binding.apply {
                tvName.text = model.name
                Glide.with(itemView).load(model.image).into(ivImage)
                blurView.visibility = View.GONE  // Hide blur for valid messages
            }
        }
    }

    inner class BlurredMessageViewHolder(private val binding: ItemMessagingChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                // Handle or prevent clicks on blurred messages
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener.onChatClick(getItem(bindingAdapterPosition))
                }
            }
        }

        fun bind(model: Data) {
            binding.apply {
                tvName.text = model.name
                Glide.with(itemView).load(model.image).into(ivImage)

                // Apply blur effect
                blurView.apply {
                    visibility = View.VISIBLE
                    setupWith(binding.root)
                        .setBlurAlgorithm(RenderScriptBlur(itemView.context))
                        .setBlurRadius(10f)
                        .setOverlayColor(Color.parseColor("#99000000"))
                        .setHasFixedTransformationMatrix(true)
                }
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

