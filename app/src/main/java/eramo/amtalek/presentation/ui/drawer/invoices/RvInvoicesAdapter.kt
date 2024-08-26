package eramo.amtalek.presentation.ui.drawer.invoices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.HistoryPackagesInfo
import eramo.amtalek.databinding.ItemInvoiceBinding
import javax.inject.Inject


class RvInvoicesAdapter @Inject constructor() :
    ListAdapter<HistoryPackagesInfo, RvInvoicesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
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

        fun bind(model: HistoryPackagesInfo) {
            binding.apply {
                tvFeatured.text = model.expirationDate?.packageType
                tvDate.text = model.expirationDate?.expirationDate
                tvPriceValue.text = itemView.context.getString(R.string.s_egp, model.actualPayment)
                tvStatusValue.text = model.expirationDate?.dateOfPackage
                tvIdValue.text = itemView.context.getString(R.string.s_invoices, model.expirationDate?.packageId.toString())
//                tvIdValue.text = model.expirationDate?.packageId.toString()
                when (model.expirationDate?.status) {
                    "accepted" -> tvAcceptanceValue.text = itemView.context.getString(R.string.accepted)
                    "holding" -> tvAcceptanceValue.text = itemView.context.getString(R.string.holding)
                }
            }
        }
    }

    fun setListener(listener: InvoicesClickListener) {
        this.listener = listener
    }

    interface InvoicesClickListener {
        fun onInvoicesClick(model: HistoryPackagesInfo)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<HistoryPackagesInfo>() {
            override fun areItemsTheSame(
                oldItem: HistoryPackagesInfo,
                newItem: HistoryPackagesInfo
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: HistoryPackagesInfo,
                newItem: HistoryPackagesInfo
            ) = oldItem == newItem
        }
    }
}