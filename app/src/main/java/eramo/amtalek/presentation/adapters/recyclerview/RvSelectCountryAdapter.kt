package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemChooseCountryRvBinding
import eramo.amtalek.domain.model.auth.CountryModel
import javax.inject.Inject

class RvSelectCountryAdapter @Inject constructor() :
    ListAdapter<CountryModel, RvSelectCountryAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemChooseCountryRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemChooseCountryRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
//            selectedPosition = 0
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
//                        listener.onCountryClick(it)

                    }
                }
            }
        }

        fun bind(model: CountryModel) {
            binding.apply {
                tvTitle.text = model.name
                Glide.with(itemView).load(model.imageUrl).into(ivFlag)

                if (selectedPosition == bindingAdapterPosition) {
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.black)
                } else {
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.white)
                }
            }

            itemView.setOnClickListener {
                listener.onCountryClick(model)
                selectedPosition = bindingAdapterPosition
                notifyDataSetChanged()

            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onCountryClick(model: CountryModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<CountryModel>() {
            override fun areItemsTheSame(
                oldItem: CountryModel,
                newItem: CountryModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: CountryModel,
                newItem: CountryModel
            ) = oldItem == newItem
        }
    }
}