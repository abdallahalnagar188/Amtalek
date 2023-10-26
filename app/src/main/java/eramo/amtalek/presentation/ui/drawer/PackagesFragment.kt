package eramo.amtalek.presentation.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPackagesBinding
import eramo.amtalek.domain.model.drawer.myfavourites.MyFavouritesModel
import eramo.amtalek.presentation.adapters.recyclerview.RvPackagesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class PackagesFragment : BindingFragment<FragmentPackagesBinding>() {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPackagesBinding::inflate

    @Inject
    lateinit var rvPackagesAdapter: RvPackagesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.packages)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initRv(data: List<MyFavouritesModel>) {
        binding.rv.adapter = rvPackagesAdapter
//        rvMyFavouritesAdapter.submitList(data)
    }
}

