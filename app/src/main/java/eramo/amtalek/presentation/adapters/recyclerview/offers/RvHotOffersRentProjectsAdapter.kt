package eramo.amtalek.presentation.adapters.recyclerview.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemProjectPreviewBinding
import eramo.amtalek.domain.model.main.home.ProjectModel
import eramo.amtalek.util.TRUE
import javax.inject.Inject

class RvHotOffersRentProjectsAdapter @Inject constructor() :
    ListAdapter<ProjectModel, RvHotOffersRentProjectsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemProjectPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemProjectPreviewBinding) :
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

        fun bind(model: ProjectModel) {
            var isFav = model.isFavourite == TRUE
            binding.apply {
                ivFav.setOnClickListener {
                    isFav = !isFav
                    if (isFav) ivFav.setImageResource(R.drawable.ic_heart_fill)
                    else ivFav.setImageResource(R.drawable.ic_heart)
                }

                tvTitle.text = model.title
                tvDescription.text = model.description
                tvLocation.text = model.location
                tvDatePosted.text = model.datePosted

//                tvBroker.text = model.brokerName

                Glide.with(itemView)
                    .load(model.imageUrl)
                    .into(ivImage)

                Glide.with(itemView)
                    .load(model.brokerLogoUrl)
                    .into(ivBroker)

                if (model.isFavourite == TRUE) {
                    ivFav.setImageResource(R.drawable.ic_heart_fill)
                } else {
                    ivFav.setImageResource(R.drawable.ic_heart)
                }

            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onPropertyClick(model: ProjectModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<ProjectModel>() {
            override fun areItemsTheSame(
                oldItem: ProjectModel,
                newItem: ProjectModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ProjectModel,
                newItem: ProjectModel
            ) = oldItem == newItem
        }
    }
}