package eramo.amtalek.presentation.ui.main.myestate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentMyEstateSellBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.RvMyEstateSellAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.showToast
import javax.inject.Inject

@AndroidEntryPoint
class MyEstateSellFragment : BindingFragment<FragmentMyEstateSellBinding>(), RvMyEstateSellAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyEstateSellBinding::inflate

    @Inject
    lateinit var rvMyEstateSellAdapter: RvMyEstateSellAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        initRv(Dummy.dummyMyFavouritesList(requireContext()))
    }

    private fun initRv(data: List<PropertyModel>) {
        rvMyEstateSellAdapter.setListener(this@MyEstateSellFragment)
        binding.rvProperties.adapter = rvMyEstateSellAdapter
        rvMyEstateSellAdapter.submitList(data)
    }

    override fun onPropertyClick(model: PropertyModel) {
        showToast("Click")
    }

    override fun onEditPropertyClick(model: PropertyModel) {
        showToast("Edit")
    }
}