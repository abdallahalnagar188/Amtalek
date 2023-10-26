package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.databinding.ItemPackagesBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import javax.inject.Inject


class RvPackagesAdapter @Inject constructor() :
    ListAdapter<MyFavouritesModel, RvPackagesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemPackagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemPackagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onPropertyClick(it)
                    }
                }
            }
        }

        fun bind(model: MyFavouritesModel) {


        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onPropertyClick(model: MyFavouritesModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<MyFavouritesModel>() {
            override fun areItemsTheSame(
                oldItem: MyFavouritesModel,
                newItem: MyFavouritesModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MyFavouritesModel,
                newItem: MyFavouritesModel
            ) = oldItem == newItem
        }
    }
}