package eramo.amtalek.presentation.adapters.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemPackagesBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import javax.inject.Inject


class RvPackagesAdapter @Inject constructor() :
    ListAdapter<PackageModel, RvPackagesAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
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
//                        listener.onSelectClick(it)
                    }
                }
            }
        }

        fun bind(model: PackageModel) {
            binding.apply {
                tvTitle.text = model.title
                tvDescription.text = model.description
                tvPrice.text = model.price.toString()

                tvNormalListing.text = itemView.context.getString(R.string.s_normal_listing, model.normal.toString())
                tvFeaturedListing.text = itemView.context.getString(R.string.s_featured_listings, model.featured.toString())
                tvSocialMediaPromotion.text = itemView.context.getString(R.string.s_social_media_promotion, model.promotion.toString())
                tvLeadsManagement.text = itemView.context.getString(R.string.s_leads_management, model.leads.toString())

                tvExtraLeads.text = itemView.context.getString(R.string.s_extra_lead, model.extraLeadsPrice.toString())

                cover.setBackgroundColor(Color.parseColor(model.coverColor))
                cvPrice.setCardBackgroundColor(Color.parseColor(model.priceColor))

                btnSelect.setOnClickListener {
                    listener.onSelectClick(model)
                }
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onSelectClick(model: PackageModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<PackageModel>() {
            override fun areItemsTheSame(
                oldItem: PackageModel,
                newItem: PackageModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PackageModel,
                newItem: PackageModel
            ) = oldItem == newItem
        }
    }
}