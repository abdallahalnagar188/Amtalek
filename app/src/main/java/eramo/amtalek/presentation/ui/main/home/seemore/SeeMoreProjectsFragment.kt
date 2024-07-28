package eramo.amtalek.presentation.ui.main.home.seemore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.project.BrokerDetail
import eramo.amtalek.data.remote.dto.project.ProjectDetailsResponse
import eramo.amtalek.data.remote.dto.property.allproperty.DataX
import eramo.amtalek.databinding.FragmentSeeMoreProjectsBinding
import eramo.amtalek.domain.model.main.home.ProjectModelx
import eramo.amtalek.presentation.adapters.recyclerview.RvProjectsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.util.navOptionsAnimation
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMoreProjectsFragment : BindingFragment<FragmentSeeMoreProjectsBinding>(),RvProjectsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMoreProjectsBinding::inflate

    private val args by navArgs<SeeMoreProjectsFragmentArgs>()
    private val projectsList get() = args.projectsList

    val viewModel:HomeMyViewModel by viewModels()
    @Inject
    lateinit var rvProjectsAdapter: RvProjectsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllProperty()
        lifecycleScope.launch {
            viewModel.allProperty.collect {
//                Log.e("elnagar", it?.data?.original?.data.toString())
//                rvProjectsAdapter.submitList(it?.data?.original?.data)
//                binding.rvProperties.adapter = rvProjectsAdapter
                it?.data?.original?.data?.let { it1 -> setupViews(it1) }
            }
        }

    }


    private fun setupViews(list: List<DataX>) {
        setupToolbar()

        setupRv(list)
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = "Featured Projects in Egypt"
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupRv(data: List<DataX>) {
        rvProjectsAdapter.setListener(this@SeeMoreProjectsFragment)
        binding.rvProperties.adapter = rvProjectsAdapter
        rvProjectsAdapter.submitList(data)
    }

    override fun onPropertyClick(model: DataX) {
      //  findNavController().navigate(R.id.myProjectDetailsFragment, null, navOptionsAnimation())
    }
}