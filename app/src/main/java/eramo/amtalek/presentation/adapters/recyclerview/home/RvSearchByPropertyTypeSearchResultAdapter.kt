package eramo.amtalek.presentation.adapters.recyclerview.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemPropTypeBinding
import eramo.amtalek.databinding.ItemPropTypeSearchResultBinding
import eramo.amtalek.domain.model.property.CriteriaModel
import javax.inject.Inject

class RvSearchByPropertyTypeSearchResultAdapter @Inject constructor() :
    ListAdapter<CriteriaModel, RvSearchByPropertyTypeSearchResultAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {


    private lateinit var listener: OnItemClickListener
    var selectedPosition: Int = -1
    var isSelected:Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPropTypeSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, position) } // Pass position to bind method
    }

    inner class ProductViewHolder(private val binding: ItemPropTypeSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    val position = bindingAdapterPosition
                    val model = getItem(position)
                    listener.onItemClick(model)
                    isSelected = !isSelected

                    // Update the selected item ID and notify changes
                    val previousPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        }

        fun bind(model: CriteriaModel, position: Int) {
            binding.apply {
                tvPropType.text = model.title
                Glide.with(itemView)
                    .load(model.image)
                    .into(ivPropType)
                tvPropTypeCount.text = model.propertyCount.toString()

                // Change the background based on selection
                val backgroundDrawable = if (position == selectedPosition && isSelected) {
                    ContextCompat.getDrawable(itemView.context, R.drawable.blue_border_with_radius_background)
                } else {
                    ContextCompat.getDrawable(itemView.context, R.drawable.edittext_background_for_search)
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
