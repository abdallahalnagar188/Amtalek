package eramo.amtalek.presentation.ui.drawer.invoices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.drawer.myaccount.myprofile.HistoryPackagesInfo
import eramo.amtalek.databinding.ItemInvoiceBinding
import eramo.amtalek.databinding.ItemSmallInvoiceBinding
import javax.inject.Inject


class RvTypeInInvoiceDetailsAdapter @Inject constructor() :
    ListAdapter<HistoryPackagesInfo.PackageDetail, RvTypeInInvoiceDetailsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemSmallInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemSmallInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        //             listener.onInvoicesClick(it)
                    }
                }
            }
        }

        fun bind(model: HistoryPackagesInfo.PackageDetail) {
            binding.apply {
                tvType.text = model.title
                when (model.title) {
                    "العقارات العاديه" -> tvType.text = itemView.context.getString(R.string.featured)
                    "العقارات المميزه" -> tvType.text = itemView.context.getString(R.string.normal)
                }
                tvUsage.text = model.used.toString()
                tvBased.text = model.base.toString()
                tvRemain.text = model.reminder.toString()
            }
        }
    }


    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<HistoryPackagesInfo.PackageDetail>() {
            override fun areItemsTheSame(
                oldItem: HistoryPackagesInfo.PackageDetail,
                newItem: HistoryPackagesInfo.PackageDetail
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: HistoryPackagesInfo.PackageDetail,
                newItem: HistoryPackagesInfo.PackageDetail
            ) = oldItem == newItem
        }
    }
}