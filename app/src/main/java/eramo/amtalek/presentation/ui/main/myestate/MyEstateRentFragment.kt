package eramo.amtalek.presentation.ui.main.myestate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.databinding.FragmentMyEstateRentBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.presentation.adapters.recyclerview.RvMyEstateRentAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.presentation.viewmodel.navbottom.CartViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.showToast
import javax.inject.Inject

@AndroidEntryPoint
class MyEstateRentFragment : BindingFragment<FragmentMyEstateRentBinding>(), RvMyEstateRentAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyEstateRentBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: CartViewModel by viewModels()

    @Inject
    lateinit var rvMyEstateRentAdapter: RvMyEstateRentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        initRv(Dummy.dummyMyFavouritesList(requireContext()))
    }

    private fun initRv(data: List<MyFavouritesModel>) {
        rvMyEstateRentAdapter.setListener(this@MyEstateRentFragment)
        binding.rvProperties.adapter = rvMyEstateRentAdapter
        rvMyEstateRentAdapter.submitList(data)
    }

    override fun onPropertyClick(model: MyFavouritesModel) {
        showToast("Click")
    }

    override fun onEditPropertyClick(model: MyFavouritesModel) {
        showToast("Edit")
    }

}