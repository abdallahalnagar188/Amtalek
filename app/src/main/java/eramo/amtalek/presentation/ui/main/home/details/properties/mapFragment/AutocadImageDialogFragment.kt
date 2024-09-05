package eramo.amtalek.presentation.ui.main.home.details.properties.mapFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentImageDialogBinding
import eramo.amtalek.domain.model.project.AutocadModel

class AutocadImageDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_IMAGE_LIST = "image_list"
        private const val ARG_POSITION = "position"

        fun newInstance(imageList: List<AutocadModel>, position: Int): AutocadImageDialogFragment {
            val fragment = AutocadImageDialogFragment()  // Updated to AutocadImageDialogFragment
            val args = Bundle().apply {
                putSerializable(ARG_IMAGE_LIST, ArrayList(imageList))
                putInt(ARG_POSITION, position)
            }
            fragment.arguments = args
            return fragment
        }
    }


    private var _binding: FragmentImageDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = arguments?.getSerializable(ARG_IMAGE_LIST) as? List<AutocadModel> ?: emptyList()
        val startPosition = arguments?.getInt(ARG_POSITION) ?: 0

        // Setup ViewPager2
        val adapter = AutocadImageSliderAdapter(imageList)  // Use an adapter for AutocadModel
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(startPosition, true)
        binding.viewPager.offscreenPageLimit = 1

        // Set the title
        binding.tvTitle.text = getString(R.string.autocad_drawings)

        // Setup RecyclerView
        val recyclerViewAdapter = AutocadImageRecyclerAdapter(imageList) { position ->
            binding.viewPager.currentItem = position
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = recyclerViewAdapter

        // Back button click listener
        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }




    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
