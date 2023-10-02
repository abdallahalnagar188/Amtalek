package eramo.amtalek.presentation.adapters.dummy

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.databinding.ItemSelectedImageBinding
import javax.inject.Inject

class DummySelectedImageAdapter @Inject constructor() :
    ListAdapter<Uri, DummySelectedImageAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemSelectedImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemSelectedImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Uri) {
            binding.apply {
                ivImg.setImageURI(model)
                ivImg.setOnClickListener { listener.onImageClick(model) }
                ivRemove.setOnClickListener { listener.onRemoveClick(bindingAdapterPosition) }
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onImageClick(model: Uri)
        fun onRemoveClick(position: Int)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(
                oldItem: Uri,
                newItem: Uri
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: Uri,
                newItem: Uri
            ) = oldItem == newItem
        }
    }
}