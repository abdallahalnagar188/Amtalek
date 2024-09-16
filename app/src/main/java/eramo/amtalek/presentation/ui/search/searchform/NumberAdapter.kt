package eramo.amtalek.presentation.ui.search.searchform


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemNumberBinding


class NumberAdapter(
    private val numbers: List<Int>,
    private val onClick: (Int?) -> Unit // Handle nullable Int
) : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {

    var selectedPosition: Int = RecyclerView.NO_POSITION // Track selected item position

    inner class NumberViewHolder(private val binding: ItemNumberBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(number: Int, isSelected: Boolean) {
            // Display +6 for the last item
            binding.tvNumber.text = if (number == 0) "+6" else number.toString()

            // Change background based on selection
            binding.root.setBackgroundResource(
                if (isSelected) R.drawable.bg_number_in_search_if_selected
                else R.drawable.bg_number_in_search
            )

            // Set click listener for item selection
            binding.root.setOnClickListener {
                val previousSelectedPosition = selectedPosition
                if (bindingAdapterPosition == selectedPosition) {
                    // Deselect the item and reset the selected position
                    selectedPosition = RecyclerView.NO_POSITION
                    onClick(null) // Pass null when deselected
                } else {
                    // Select the new item
                    selectedPosition = bindingAdapterPosition
                    onClick(number) // Pass the selected number
                }

                // Notify changes in previously selected and newly selected items
                notifyItemChanged(previousSelectedPosition) // Unselect the previous one
                notifyItemChanged(selectedPosition) // Select the new one
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val binding = ItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NumberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.bind(numbers[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = numbers.size

}





