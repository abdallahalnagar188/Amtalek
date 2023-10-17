package eramo.amtalek.presentation.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentFavouritesBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.presentation.adapters.recyclerview.RvMyFavouritesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.Dummy.dummyMyFavouritesList
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class FavouritesFragment : BindingFragment<FragmentFavouritesBinding>(), RvMyFavouritesAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentFavouritesBinding::inflate

    @Inject
    lateinit var rvMyFavouritesAdapter: RvMyFavouritesAdapter
    private val viewModelShared: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()

        initFavouritesRv(dummyMyFavouritesList(requireContext()))
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.my_favourite_properties)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initFavouritesRv(data: List<MyFavouritesModel>) {
        rvMyFavouritesAdapter.setListener(this@FavouritesFragment)
        binding.rvProperties.adapter = rvMyFavouritesAdapter
        rvMyFavouritesAdapter.submitList(data)
    }

    override fun onPropertyClick(model: MyFavouritesModel) {

    }

}