package eramo.amtalek.presentation.ui.drawer.addproperty.fifth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.R
import eramo.amtalek.databinding.ItemAminitiesWideBinding
import eramo.amtalek.domain.model.project.AmenityModel
import javax.inject.Inject

class SelectAmenitiesAdapter @Inject constructor():RecyclerView.Adapter<SelectAmenitiesAdapter.AmenityViewHolder>(){
    inner class AmenityViewHolder(val binding: ItemAminitiesWideBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: AmenityModel){
            binding.amenityName.text = item.name
        }
    }
    var selectionList = mutableListOf<Int>()


    fun saveData( dataResponse: List<AmenityModel>){
        asyncListDiffer.submitList(dataResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenityViewHolder {
        val binding = ItemAminitiesWideBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AmenityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AmenityViewHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            if (!data.isSelected){
                holder.binding.icon.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_radio_button_checked))
                data.isSelected = true
                selectionList.add(data.id)
            }else{
                holder.binding.icon.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.ic_radio_button_unchecked))
                data.isSelected = false
                selectionList.remove(data.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
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

}