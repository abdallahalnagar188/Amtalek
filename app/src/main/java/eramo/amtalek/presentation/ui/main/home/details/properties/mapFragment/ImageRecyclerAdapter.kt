package eramo.amtalek.presentation.ui.main.home.details.properties.mapFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eramo.amtalek.R

class ImageRecyclerAdapter(
    private val imageList: List<String>, // Assuming URLs or resource IDs
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]
        Glide.with(holder.itemView.context).load(image).into(holder.imageView)
        // Load image using your preferred library (e.g., Glide or Picasso)
        // holder.imageView.setImageResource(image) // or load from URL
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = imageList.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
