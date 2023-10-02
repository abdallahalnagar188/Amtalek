package eramo.amtalek.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import eramo.amtalek.R
import eramo.amtalek.data.remote.EventsApi
import eramo.amtalek.databinding.ItemImageSliderBinding
import eramo.amtalek.domain.model.dummy.AlbumModel
import javax.inject.Inject

class BannerSliderAdapter @Inject constructor() :
    SliderViewAdapter<BannerSliderAdapter.ImageSliderViewHolder>() {
    private var sliderItems= emptyList<AlbumModel>()
    private lateinit var listener: OnItemClickListener

    fun renewItems(sliderItems: List<AlbumModel>) {
        this.sliderItems = sliderItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup) = ImageSliderViewHolder(
        ItemImageSliderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(viewHolder: ImageSliderViewHolder, position: Int) {
        //load image into view
        viewHolder.bind(sliderItems[position],position)
    }

    override fun getCount() = sliderItems.size

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ImageSliderViewHolder(private val binding: ItemImageSliderBinding) :
        ViewHolder(binding.root) {

        fun bind(model: AlbumModel,position: Int) {
            binding.apply {
//                Glide.with(itemView)
//                    .load(EventsApi.IMAGE_URL_ADS + model.image)
//                    .into(itemImageSliderIv)
                model.imgUrl?.let {
                    Glide.with(itemView)
                        .load(it)
                        .placeholder(R.drawable.ic_cloudy)
                        .into(itemImageSliderIv)
                }

                model.resId?.let {
                    itemImageSliderIv.setImageResource(it)
                }
            }
            itemView.setOnClickListener {
                listener.onBannerClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onBannerClick(position: Int)
    }
}