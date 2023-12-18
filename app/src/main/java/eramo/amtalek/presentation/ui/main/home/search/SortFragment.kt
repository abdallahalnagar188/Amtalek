package eramo.amtalek.presentation.ui.main.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSortBinding
import eramo.amtalek.domain.model.main.home.SortOptionModel
import eramo.amtalek.presentation.adapters.recyclerview.RvSortOptionsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import javax.inject.Inject


@AndroidEntryPoint
class SortFragment : BindingFragment<FragmentSortBinding>(), RvSortOptionsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSortBinding::inflate

    @Inject
    lateinit var rvSortOptionsAdapter: RvSortOptionsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()
        initRvSort(optionsList())
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.sort_by)
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initRvSort(options: List<SortOptionModel>) {
        rvSortOptionsAdapter.setListener(this@SortFragment)
        binding.rv.adapter = rvSortOptionsAdapter
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