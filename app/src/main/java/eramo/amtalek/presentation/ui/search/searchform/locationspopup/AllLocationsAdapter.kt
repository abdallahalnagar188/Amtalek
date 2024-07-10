package eramo.amtalek.presentation.ui.search.searchform.locationspopup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.databinding.ItemLocationBinding
import eramo.amtalek.databinding.LayoutSpinnerItemBinding
import eramo.amtalek.domain.search.LocationModel
import eramo.amtalek.presentation.adapters.recyclerview.home.RvHomeMostViewedPropertiesAdapter
import eramo.amtalek.presentation.ui.interfaces.FavClickListener
import javax.inject.Inject

class AllLocationsAdapter  @Inject constructor(

): ListAdapter<LocationModel, AllLocationsAdapter.LocationViewHolder>(PRODUCT_COMPARATOR){
    private lateinit var listener: OnItemLocationClick
    fun setFilteredList(filteredList:List<LocationModel>){
        currentList.clear()
        submitList(filteredList)
    }
    inner class LocationViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    getItem(bindingAdapterPosition).let {
                        listener.onLocationClicked(it)
                    }
                }
            }
        }

        fun bind(model: LocationModel) {
            binding.name.text = model.title
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  LocationViewHolder (
        ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    interface OnItemLocationClick {
        fun onLocationClicked(model: LocationModel)
    }
    fun setListener(listener: OnItemLocationClick) {
        this.listener = listener
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<LocationModel>() {
            override fun areItemsTheSame(
                oldItem: LocationModel,
                newItem: LocationModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: LocationModel,
                newItem: LocationModel
            ) = oldItem == newItem
        }
    }


}