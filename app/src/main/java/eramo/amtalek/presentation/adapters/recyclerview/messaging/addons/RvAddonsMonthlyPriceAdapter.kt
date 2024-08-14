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

class RvAddonsMonthlyPriceAdapter @Inject constructor(
    private val onTotalPriceChanged: (Double) -> Unit // Callback to send total price to the fragment
) : ListAdapter<Data, RvAddonsMonthlyPriceAdapter.ProductViewHolder>(PRODUCT_COMPARATOR)
{

    private val totalPrices = mutableListOf<Double>() // List to track total prices for each item
    private val quantities = mutableListOf<Int>() // List to track quantities for each item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(
            ItemAdonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model, position)
    }

    inner class ProductViewHolder(private val binding: ItemAdonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var counter: Int = 0

        init {
            binding.btnAdd.setOnClickListener {
                counter++
                quantities[bindingAdapterPosition] = counter // Update quantity
                updatePrice(bindingAdapterPosition)
            }

            binding.btnMinus.setOnClickListener {
                if (counter > 0) {
                    counter--
                    quantities[bindingAdapterPosition] = counter // Update quantity
                    updatePrice(bindingAdapterPosition)
                }
            }
        }

        fun bind(model: Data, position: Int) {
            binding.apply {
                when (model.name) {
                    "normal_listings" -> tvAddonName.text = itemView.context.getString(R.string.normal_listings)
                    "featured_listings" -> tvAddonName.text = itemView.context.getString(R.string.featured_listings)
                    "projects" -> tvAddonName.text = itemView.context.getString(R.string.projects)
                    "messages" -> tvAddonName.text = itemView.context.getString(R.string.message)
                    else -> tvAddonName.text = model.name
                }

                tvAddonPrice.text = model.monthlyPrice.toString()
                counter = model.quantity ?: 0
                tvCounter.text = counter.toString()
                updatePrice(position)
            }
        }

        private fun updatePrice(position: Int) {
            val monthlyPrice = binding.tvAddonPrice.text.toString().toDouble()
            val totalPrice = monthlyPrice * counter
            binding.tvCounter.text = counter.toString()
            binding.totalPriceForAddon.text = totalPrice.toString()
            totalPrices[position] = totalPrice
            val aggregatedTotalPrice = totalPrices.sum()
            onTotalPriceChanged(aggregatedTotalPrice)
        }
    }

    override fun submitList(list: List<Data?>?) {
        super.submitList(list)
        totalPrices.clear()
        quantities.clear()
        list?.forEach { item ->
            totalPrices.add(0.0)
            quantities.add(
                item?.quantity ?: 0
            )
        }
    }

    fun getUpdatedDataList(): List<Data> {
        return currentList.mapIndexed { index, data ->
            data.copy(quantity = quantities[index])
        }
    }

    fun calculateTotalPrice(list: List<Data?>): Double {
        return list.sumOf { item ->
            val price = item?.monthlyPrice?.toDouble() ?: 0.0
            val quantity = quantities[currentList.indexOf(item)]
            price * quantity
        }
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

