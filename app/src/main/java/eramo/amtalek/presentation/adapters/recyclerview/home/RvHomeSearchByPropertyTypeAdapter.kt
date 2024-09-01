package eramo.amtalek.presentation.adapters.recyclerview.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.databinding.ItemPropTypeBinding
import eramo.amtalek.domain.model.property.CriteriaModel
import javax.inject.Inject


class RvHomeSearchByPropertyTypeAdapter @Inject constructor() :
    ListAdapter<CriteriaModel, RvHomeSearchByPropertyTypeAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPropTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemPropTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onItemClick(it)
                    }
                }
            }
        }

        fun bind(model: CriteriaModel) {
            binding.apply {
                tvPropType.text = model.title
                Glide.with(itemView)
                    .load(model.image)
                    .into(ivPropType)
            }
        }
    }



    fun setListener(listener: OnItemClickListener) {
        this.listener = listener

    }

    interface OnItemClickListener {
        fun onItemClick(model: CriteriaModel)
    }


    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<CriteriaModel>() {
            override fun areItemsTheSame(
                oldItem: CriteriaModel,
                newItem: CriteriaModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: CriteriaModel,
                newItem: CriteriaModel
            ) = oldItem == newItem
        }
    }
}