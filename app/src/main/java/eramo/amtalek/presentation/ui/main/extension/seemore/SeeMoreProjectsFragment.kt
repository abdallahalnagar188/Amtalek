package eramo.amtalek.presentation.ui.main.extension.seemore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSeeMoreProjectsBinding
import eramo.amtalek.domain.model.main.home.ProjectModel
import eramo.amtalek.presentation.adapters.recyclerview.RvProjectsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import javax.inject.Inject

@AndroidEntryPoint
class SeeMoreProjectsFragment : BindingFragment<FragmentSeeMoreProjectsBinding>(),RvProjectsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMoreProjectsBinding::inflate

    private val args by navArgs<SeeMoreProjectsFragmentArgs>()
    private val projectsList get() = args.projectsList
    private val title get() = args.title

    @Inject
    lateinit var rvProjectsAdapter: RvProjectsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }


    private fun setupViews() {
        setupToolbar()

        setupRv(projectsList.toList())
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = title
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupRv(data: List<ProjectModel>) {
        rvProjectsAdapter.setListener(this@SeeMoreProjectsFragment)
        binding.rvProperties.adapter = rvProjectsAdapter
        rvProjectsAdapter.submitList(data)
    }

    override fun onPropertyClick(model: ProjectModel) {
        findNavController().navigate(R.id.myProjectDetailsFragment, null, navOptionsAnimation())
    }
}