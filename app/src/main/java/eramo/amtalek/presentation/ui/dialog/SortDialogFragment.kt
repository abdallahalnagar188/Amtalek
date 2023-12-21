package eramo.amtalek.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSortDialogBinding
import eramo.amtalek.domain.model.main.home.SortOptionModel
import eramo.amtalek.presentation.adapters.recyclerview.RvSortOptionsAdapter
import javax.inject.Inject

@AndroidEntryPoint
class SortDialogFragment : DialogFragment(), RvSortOptionsAdapter.OnItemClickListener {

    private var binding: FragmentSortDialogBinding? = null

    @Inject
    lateinit var rvSortOptionsAdapter: RvSortOptionsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSortDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {
        initRvSort(optionsList())
    }

    private fun listeners() {
        binding?.apply {
            ivClose.setOnClickListener {
                dialog?.dismiss()
            }

            btnSearch.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

    private fun initRvSort(options: List<SortOptionModel>) {
        rvSortOptionsAdapter.setListener(this@SortDialogFragment)
        binding?.rv?.adapter = rvSortOptionsAdapter
        rvSortOptionsAdapter.submitList(options)
    }

    private fun optionsList(): List<SortOptionModel> {
        val list = mutableListOf<SortOptionModel>()

        list.add(
            SortOptionModel(1, getString(R.string.sort_by_most_relevant))
        )
        list.add(
            SortOptionModel(2, getString(R.string.sort_by_newly_added))
        )
        list.add(
            SortOptionModel(3, getString(R.string.sort_by_near_me))
        )
        list.add(
            SortOptionModel(4, getString(R.string.sort_by_low_price))
        )
        list.add(
            SortOptionModel(5, getString(R.string.sort_by_high_price))
        )

        return list
    }

    override fun onOptionClick(model: SortOptionModel) {

    }
}