package eramo.amtalek.presentation.ui.drawer.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentPackagesBinding
import eramo.amtalek.domain.model.drawer.PackageModel
import eramo.amtalek.presentation.adapters.recyclerview.RvPackagesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import javax.inject.Inject

@AndroidEntryPoint
class PackagesFragment : BindingFragment<FragmentPackagesBinding>(),RvPackagesAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPackagesBinding::inflate

    @Inject
    lateinit var rvPackagesAdapter: RvPackagesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()

        initRv(Dummy.dummyPackagesList())
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.packages)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initRv(data: List<PackageModel>) {
        rvPackagesAdapter.setListener(this@PackagesFragment)
        binding.rv.adapter = rvPackagesAdapter
        rvPackagesAdapter.submitList(data)
    }

    override fun onSelectClick(model: PackageModel) {

    }
}

