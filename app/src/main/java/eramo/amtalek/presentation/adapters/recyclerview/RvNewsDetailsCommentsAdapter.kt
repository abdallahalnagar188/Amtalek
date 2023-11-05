package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.databinding.ItemRatingsBinding
import eramo.amtalek.domain.model.drawer.latestprojects.LatestProjectsModel
import eramo.amtalek.domain.model.social.RatingCommentsModel
import javax.inject.Inject


class RvNewsDetailsCommentsAdapter @Inject constructor() :
    ListAdapter<RatingCommentsModel, RvNewsDetailsCommentsAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemRatingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemRatingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
//                        listener.onProjectClick(it)
                    }
                }
            }
        }

        fun bind(model: RatingCommentsModel) {
            binding.apply {
                tvUserName.text = model.userName
                tvUserId.text = model.userId
                tvComment.text = model.comment
                tvDate.text = model.date
                rateBar.rating = model.rate


                Glide.with(itemView).load(model.userImageUrl).into(ivUserImage)
            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onProjectClick(model: LatestProjectsModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<RatingCommentsModel>() {
            override fun areItemsTheSame(
                oldItem: RatingCommentsModel,
                newItem: RatingCommentsModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: RatingCommentsModel,
                newItem: RatingCommentsModel
            ) = oldItem == newItem
        }
    }
}