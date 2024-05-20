package eramo.amtalek.presentation.ui.main.home.details.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.data.remote.dto.home.Amenity
import eramo.amtalek.databinding.ItemAminitiesBinding
import eramo.amtalek.domain.model.project.AmenityModel
import javax.inject.Inject

class AmenitiesAdapter @Inject constructor():RecyclerView.Adapter<AmenitiesAdapter.AmenityViewHolder>() {
    inner class AmenityViewHolder(val binding:ItemAminitiesBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:AmenityModel){
            binding.amenityName.text = item.name
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<AmenityModel>() {
        override fun areItemsTheSame(oldItem: AmenityModel, newItem: AmenityModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AmenityModel, newItem: AmenityModel): Boolean {
            return oldItem == newItem
        }

    }
    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData( dataResponse: List<AmenityModel>){
        asyncListDiffer.submitList(dataResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenityViewHolder {
        val binding = ItemAminitiesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AmenityViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: AmenityViewHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]
        holder.bind(data)
    }
}