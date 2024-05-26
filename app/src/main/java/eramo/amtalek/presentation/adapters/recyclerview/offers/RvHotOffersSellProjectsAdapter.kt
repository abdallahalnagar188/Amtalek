package eramo.amtalek.presentation.adapters.recyclerview.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemProjectPreviewBinding
import javax.inject.Inject


class RvHotOffersSellProjectsAdapter @Inject constructor() :
    ListAdapter<eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel, RvHotOffersSellProjectsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
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
                        listener.onProjectClick(it)
                    }
                }
            }
        }

        fun bind(model: eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel) {
            var isFav = model.isFavourite == ""
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

                if (model.isFavourite == "1") {
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
        fun onProjectClick(model: eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel>() {
            override fun areItemsTheSame(
                oldItem: eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel,
                newItem: eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel,
                newItem: eramo.amtalek.domain.model.drawer.myfavourites.ProjectModel
            ): Boolean = oldItem == newItem
        }
    }
}