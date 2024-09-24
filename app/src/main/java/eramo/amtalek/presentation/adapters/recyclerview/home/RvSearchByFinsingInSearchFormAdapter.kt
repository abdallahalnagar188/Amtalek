package eramo.amtalek.presentation.adapters.recyclerview.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemFinishingSearchFormBinding
import eramo.amtalek.databinding.ItemPropTypeBinding
import eramo.amtalek.databinding.ItemPropTypeSearchResultBinding
import eramo.amtalek.domain.model.property.CriteriaModel
import javax.inject.Inject

class RvSearchByFinsingInSearchFormAdapter @Inject constructor() :
    ListAdapter<CriteriaModel, RvSearchByFinsingInSearchFormAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {

    private var listener: OnItemClickListener? = null
    var selectedPosition: Int = -1
    var isItemSelected = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemFinishingSearchFormBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, position) } // Pass position to bind method
    }

    inner class ProductViewHolder(private val binding: ItemFinishingSearchFormBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    val position = bindingAdapterPosition
                    val model = getItem(position)
                    listener?.onItemClick(model)
                    isItemSelected = !isItemSelected

                    // Update the selected item ID and notify changes
                    val previousPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(previousPosition) // Refresh previously selected item
                    notifyItemChanged(selectedPosition) // Refresh newly selected item
                }
            }
        }

        fun bind(model: CriteriaModel, position: Int) {
            binding.apply {
                tvPropType.text = model.title

                // Change the background based on selection
                val backgroundDrawable = if (position == selectedPosition && isItemSelected) {
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_finishing_search_if_selected)
                } else {
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_finishing_in_search)
                }
                binding.root.background = backgroundDrawable
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(model: CriteriaModel)
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<CriteriaModel>() {
            override fun areItemsTheSame(
                oldItem: CriteriaModel,
                newItem: CriteriaModel
            ) = oldItem.id == newItem.id // Compare by unique identifier

            override fun areContentsTheSame(
                oldItem: CriteriaModel,
                newItem: CriteriaModel
            ) = oldItem == newItem
        }
    }
}
