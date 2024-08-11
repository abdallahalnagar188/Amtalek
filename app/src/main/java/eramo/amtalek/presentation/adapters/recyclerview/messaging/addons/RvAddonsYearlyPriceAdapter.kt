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

class RvAddonsYearlyPriceAdapter @Inject constructor(
    private val onTotalPriceUpdated: (Double) -> Unit
) : ListAdapter<Data, RvAddonsYearlyPriceAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {

    private val totalPrices = mutableListOf<Double>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(
            ItemAdonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model,position)
    }

    inner class ProductViewHolder(private val binding: ItemAdonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var counter: Int = 0

        init {
            binding.btnAdd.setOnClickListener {
                counter++
                updatePrice(bindingAdapterPosition)
            }

            binding.btnMinus.setOnClickListener {
                if (counter > 0) {
                    counter--
                    updatePrice(bindingAdapterPosition)
                }
            }
        }

        fun bind(model: Data,position: Int) {
            binding.apply {
                when (model.name) {
                    "normal_listings" -> tvAddonName.text = itemView.context.getString(R.string.normal_listings)
                    "featured_listings" -> tvAddonName.text = itemView.context.getString(R.string.featured_listings)
                    "projects" -> tvAddonName.text = itemView.context.getString(R.string.projects)
                    "messages" -> tvAddonName.text = itemView.context.getString(R.string.message)
                    else -> tvAddonName.text = model.name
                }

                tvAddonPrice.text = model.yearlyPrice.toString()
                tvCounter.text = counter.toString()
                updatePrice(position)
            }
        }

        private fun updatePrice(position: Int) {
            val yearlyPrice = binding.tvAddonPrice.text.toString().toDouble()
            val totalPrice = yearlyPrice * counter
            binding.tvCounter.text = counter.toString()
            binding.totalPriceForAddon.text = totalPrice.toString()

            totalPrices[position] = totalPrice // Update the total price for this item
            val aggregatedTotalPrice = totalPrices.sum() // Calculate the sum of all total prices
            onTotalPriceUpdated(aggregatedTotalPrice)
        }
    }

    override fun submitList(list: List<Data?>?) {
        super.submitList(list)
        totalPrices.clear() // Reset the total prices list
        list?.forEach { _ -> totalPrices.add(0.0) } // Initialize the total prices list with zeros
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
