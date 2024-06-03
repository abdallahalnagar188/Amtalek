package eramo.amtalek.presentation.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemChooseCityRvBinding
import eramo.amtalek.databinding.ItemChooseCountryRvBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import javax.inject.Inject


class RvSelectCityAdapter @Inject constructor() :
    ListAdapter<CityModel, RvSelectCityAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemChooseCityRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    inner class ProductViewHolder(private val binding: ItemChooseCityRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                    }
                }
            }
        }

        fun bind(model: CityModel) {
            binding.apply {
                if (LocalUtil.isEnglish()){
                    tvTitle.text = model.titleEn
                }else{
                    tvTitle.text = model.titleAr
                }
                Glide.with(itemView).load(model.image).into(ivFlag)

                if (selectedPosition == bindingAdapterPosition) {
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.black)
                } else {
                    root.strokeColor = ContextCompat.getColor(itemView.context, R.color.white)
                }
            }

            itemView.setOnClickListener {
                listener.onCityClick(model)
                selectedPosition = bindingAdapterPosition
                notifyDataSetChanged()

            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onCityClick(model: CityModel)
    }

    //check difference
    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<CityModel>() {
            override fun areItemsTheSame(
                oldItem: CityModel,
                newItem: CityModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: CityModel,
                newItem: CityModel
            ) = oldItem == newItem
        }
    }
}