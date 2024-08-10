package eramo.amtalek.presentation.adapters.recyclerview.messaging.addons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.adons.Data
import eramo.amtalek.databinding.ItemAdonsBinding
import javax.inject.Inject

class RvAddonsMonthlyPriceAdapter @Inject constructor() :
    ListAdapter<Data, RvAddonsMonthlyPriceAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(
            ItemAdonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }

    inner class ProductViewHolder(private val binding: ItemAdonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var counter: Int = 0

        init {
            binding.btnMinus.setOnClickListener {
                getItem(bindingAdapterPosition).let {
                    counter++
                    updatePrice(it)
                }
            }

            binding.btnAdd.setOnClickListener {
                getItem(bindingAdapterPosition).let {
                    if (counter > 0) {
                        counter--
                        updatePrice(it)
                    }
                }
            }
        }

        fun bind(model: Data) {
            binding.apply {

                if (model.name.toString()== "normal_listings") {
                    tvAddonName.text= itemView.context.getString(R.string.normal_listings)
                }else if (model.name.toString()== "featured_listings") {
                    tvAddonName.text= itemView.context.getString(R.string.featured_listings)
                }else if (model.name== "projects") {
                    tvAddonName.text= itemView.context.getString(R.string.projects)
                }else if (model.name== "messages") {
                    tvAddonName.text= itemView.context.getString(R.string.message)
                }
                tvAddonName.text = model.name
                tvAddonPrice.text = model.monthlyPrice.toString()
                tvCounter.text = counter.toString()
                updatePrice(model)
            }
        }

        private fun updatePrice(model: Data) {
            val monthlyPrice = model.monthlyPrice?.toIntOrNull() ?: 0
            val totalPrice = monthlyPrice * counter
            binding.tvCounter.text = counter.toString()
            binding.totalPriceForAddon.text = totalPrice.toString()
            listener?.onPriceUpdate(model, totalPrice)
        }
    }

    fun setListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onPriceUpdate(model: Data, totalPrice: Int)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Data, newItem: Data) =
                oldItem == newItem
        }
    }
}

