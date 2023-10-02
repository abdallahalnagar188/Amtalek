package eramo.amtalek.presentation.adapters.dummy

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eramo.amtalek.databinding.ItemCheckboxBinding
import eramo.amtalek.domain.model.dummy.AlbumModel
import javax.inject.Inject

class DummyItemCheckboxAdapter @Inject constructor() : ListAdapter<AlbumModel, DummyItemCheckboxAdapter.AlbumViewHolder>(ITEM_COMPARATOR) {
    private lateinit var listener: OnItemClickListener
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AlbumViewHolder(
        ItemCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setCurrentPosition(position: Int) {
        Log.d("Mohab", "album position $position")
        if (selectedPosition != position && position != -1) {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    fun getCurrentPosition() = selectedPosition

    inner class AlbumViewHolder(private val binding: ItemCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        init {
//            binding.root.setOnClickListener {
//                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
//                    listener.onImgClick(bindingAdapterPosition)
//                    setCurrentPosition(bindingAdapterPosition)
//                }
//            }
//        }

        fun bind(model: AlbumModel) {
            binding.apply {

            }
        }
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onImgClick(position: Int)
    }

    //check difference
    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<AlbumModel>() {
            override fun areItemsTheSame(
                oldItem: AlbumModel,
                newItem: AlbumModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: AlbumModel,
                newItem: AlbumModel
            ) = oldItem == newItem
        }
    }
}