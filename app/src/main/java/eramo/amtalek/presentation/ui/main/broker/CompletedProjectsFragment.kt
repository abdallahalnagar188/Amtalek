package eramo.amtalek.presentation.ui.main.broker

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
import eramo.amtalek.data.remote.dto.broker.entity.DataX
import eramo.amtalek.data.remote.dto.brokersDetails.Data
import eramo.amtalek.data.remote.dto.brokersDetails.Project
import eramo.amtalek.databinding.FragmentCompletedProjectsBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvCompletedProjectsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.seemore.SeeMoreProjectsFragmentArgs
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CompletedProjectsFragment : BindingFragment<FragmentCompletedProjectsBinding>(),
    RvCompletedProjectsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentCompletedProjectsBinding::inflate

    @Inject
    lateinit var rvCompletedProjectsAdapter: RvCompletedProjectsAdapter


    private val args by navArgs<BrokersDetailsFragmentArgs>()
    private val projectsList get() = args.projects.toList()


    val viewModel: BrokersViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        try {
            setupToolbar()
            Log.e("CompletedProjectsFragment", "Projects List: $projectsList")
            initRv(projectsList)
        } catch (e: Exception) {
            Log.e("CompletedProjectsFragment", projectsList.toString())
        }
    }



    private fun setupToolbar() {

        binding.apply {
          //  inToolbar.tvTitle.text = getString(R.string.s_completed_projects, "122")
            inToolbar.ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initRv(data: List<Project?>) {
        rvCompletedProjectsAdapter.setListener(this@CompletedProjectsFragment)
        binding.rv.adapter = rvCompletedProjectsAdapter
        rvCompletedProjectsAdapter.submitList(data)
    }

    override fun onProjectClick(model: Project) {

    }
}