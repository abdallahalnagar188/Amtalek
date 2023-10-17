package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.databinding.ItemNotificationBinding
import eramo.amtalek.domain.model.extentions.NotificationsModel
import javax.inject.Inject

class RvNotificationsAdapter @Inject constructor() :
    ListAdapter<NotificationsModel, RvNotificationsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onNotificationClick(it)
                    }
                }
            }
        }

        fun bind(model: NotificationsModel) {
            binding.apply {
                tvTitle.text = model.title
                tvBody.text = model.body
                tvDate.text = model.date

                Glide.with(itemView)
                    .load(model.imageUrl)
                    .into(ivImage)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onNotificationClick(model: NotificationsModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<NotificationsModel>() {
            override fun areItemsTheSame(
                oldItem: NotificationsModel,
                newItem: NotificationsModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: NotificationsModel,
                newItem: NotificationsModel
            ) = oldItem == newItem
        }
    }
}