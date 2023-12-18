package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.databinding.ItemSortOptionsBinding
import eramo.amtalek.domain.model.main.home.SortOptionModel
import javax.inject.Inject


class RvSortOptionsAdapter @Inject constructor() :
    ListAdapter<SortOptionModel, RvSortOptionsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemSortOptionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemSortOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onOptionClick(it)
                    }
                }
            }
        }

        fun bind(model: SortOptionModel) {
            binding.apply {
                tvOption.text = model.option

                if (selectedPosition == bindingAdapterPosition){
                    ivMark.visibility = View.VISIBLE
                }else{
                    ivMark.visibility = View.INVISIBLE
                }
            }
            itemView.setOnClickListener {
                selectedPosition = bindingAdapterPosition
                notifyDataSetChanged()

            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onOptionClick(model: SortOptionModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<SortOptionModel>() {
            override fun areItemsTheSame(
                oldItem: SortOptionModel,
                newItem: SortOptionModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: SortOptionModel,
                newItem: SortOptionModel
            ) = oldItem == newItem
        }
    }
}