package eramo.amtalek.presentation.ui.drawer.invoices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemInvoiceBinding
import eramo.amtalek.databinding.ItemPackagesUserBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import eramo.amtalek.domain.model.profile.ProfileModel
import eramo.amtalek.util.UserUtil
import javax.inject.Inject


class RvInvoicesAdapter @Inject constructor() :
    ListAdapter<ProfileModel, RvInvoicesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: InvoicesClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                       listener.onInvoicesClick(it)
                    }
                }
            }
        }

        fun bind(model: ProfileModel) {
            binding.apply {
               // tvFeatured.text = model.
            }

        }
    }

    fun setListener(listener: InvoicesClickListener) {
        this.listener = listener
    }

    interface InvoicesClickListener {
        fun onInvoicesClick(model: ProfileModel)
    }
    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<ProfileModel>() {
            override fun areItemsTheSame(
                oldItem: ProfileModel,
                newItem: ProfileModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ProfileModel,
                newItem: ProfileModel
            ) = oldItem == newItem
        }
    }
}