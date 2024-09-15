package eramo.amtalek.presentation.ui.search.searchform

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R

class NumberAdapter(
    private val numbers: List<Int>,
    private val onClick: (Int?) -> Unit // Handle nullable Int
) : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION // Track selected item position

    inner class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNumber: TextView = itemView.findViewById(R.id.tvNumber)

        fun bind(number: Int, isSelected: Boolean) {
            // Display +6 for the last item
            tvNumber.text = if (number == 0) "+6" else number.toString()

            // Change background based on selection
            if (isSelected) {
                itemView.setBackgroundResource(R.drawable.bg_number_in_search_if_selected)
            } else {
                itemView.setBackgroundResource(R.drawable.bg_number_in_search)
            }

            // Set click listener for item selection
            itemView.setOnClickListener {
                val previousSelectedPosition = selectedPosition
                if (adapterPosition == selectedPosition) {
                    // Deselect the item and reset the selected position
                    selectedPosition = RecyclerView.NO_POSITION
                    onClick(null) // Pass null when deselected
                } else {
                    // Select the new item
                    selectedPosition = adapterPosition
                    onClick(number) // Pass the selected number
                }

                // Notify changes in previously selected and newly selected items
                notifyItemChanged(previousSelectedPosition) // Unselect the previous one
                notifyItemChanged(selectedPosition) // Select the new one
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_number, parent, false)
        return NumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.bind(numbers[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = numbers.size
}




