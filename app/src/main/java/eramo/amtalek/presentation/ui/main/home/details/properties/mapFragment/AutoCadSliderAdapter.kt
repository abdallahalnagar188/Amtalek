package eramo.amtalek.presentation.ui.main.home.details.properties.mapFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.databinding.ItemImageBinding
import eramo.amtalek.databinding.ItemImageSliderBinding
import eramo.amtalek.domain.model.project.AutocadModel

class AutocadImageSliderAdapter(
    private val imageList: List<AutocadModel>
) : RecyclerView.Adapter<AutocadImageSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val autocadModel = imageList[position]
        Glide.with(holder.itemView.context)
            .load(autocadModel.src)
            .into(holder.binding.itemImageSliderIv)
    }

    override fun getItemCount(): Int = imageList.size

    class ImageViewHolder(val binding: ItemImageSliderBinding) : RecyclerView.ViewHolder(binding.root)
}


class AutocadImageRecyclerAdapter(
    private val imageList: List<AutocadModel>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<AutocadImageRecyclerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val autocadModel = imageList[position]
        Glide.with(holder.itemView.context)
            .load(autocadModel.src)
            .into(holder.binding.imageView)

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = imageList.size

    class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)
}


