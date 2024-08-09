package eramo.amtalek.presentation.adapters.recyclerview.messaging.addons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.data.remote.dto.adons.Data
import eramo.amtalek.databinding.ItemAdonsBinding
import javax.inject.Inject


class RvAddonsYearlyPriceAdapter @Inject constructor() :
    ListAdapter<Data, RvAddonsYearlyPriceAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemAdonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemAdonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        init {
//            binding.root.setOnClickListener {
//                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
//                    getItem(bindingAdapterPosition).let {
//                        listener.onChatClick(it)
//                    }
//                }
//            }
//        }

        fun bind(model: Data) {
            binding.apply {
                tvAddonName.text = model.name
                tvAddonPrice.text = model.yearlyPrice.toString()

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