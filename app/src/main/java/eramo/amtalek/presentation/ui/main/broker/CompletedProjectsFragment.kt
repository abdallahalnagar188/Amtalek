package eramo.amtalek.presentation.ui.main.broker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.brokersDetails.Project
import eramo.amtalek.databinding.FragmentCompletedProjectsBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvCompletedProjectsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.main.home.details.projects.MyProjectDetailsFragmentArgs
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CompletedProjectsFragment : BindingFragment<FragmentCompletedProjectsBinding>(),
    RvCompletedProjectsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentCompletedProjectsBinding::inflate

    @Inject
    lateinit var rvCompletedProjectsAdapter: RvCompletedProjectsAdapter
    val viewModel: BrokersViewModel by viewModels()

    private val args by navArgs<CompletedProjectsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBrokersDetails(args.id)
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        getDetailsListener()
    }

    private fun getDetailsListener() {

        lifecycleScope.launch {
            viewModel.brokersDetails.collect {
                when (it) {
                    is Resource.Success -> {
                        if (viewModel.brokersDetails.value?.data?.data?.get(0)?.projects_count == 0){
                            binding.noCompletedProjects.visibility = View.VISIBLE
                        }else{
                            binding.noCompletedProjects.visibility = View.GONE
                        }
                        rvCompletedProjectsAdapter.submitList(
                            viewModel.brokersDetails.value?.data?.data?.get(0)?.projects ?: emptyList()
                        )
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Error -> {
                        showToast(it.message.toString())
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                    }
                }

            }
        }
//        lifecycleScope.launch {
//            viewModel.brokersDetails.collectLatest {
//                if (viewModel.brokersDetails.value?.data?.get(0)?.projects_count == 0){
//                    binding.noCompletedProjects.visibility = View.VISIBLE
//                }else{
//                    binding.noCompletedProjects.visibility = View.GONE
//                }
//                rvCompletedProjectsAdapter.submitList(
//                    viewModel.brokersDetails.value?.data?.get(0)?.projects ?: emptyList()
//                )
//            }
//        }
    }
    private fun setupViews() {

        setupToolbar()
        initRv()
    }



    private fun setupToolbar() {

        binding.apply {
            inToolbar.tvTitle.text = getString(R.string.completed_projects)
            inToolbar.ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initRv() {
        rvCompletedProjectsAdapter.setListener(this@CompletedProjectsFragment)
        binding.rv.adapter = rvCompletedProjectsAdapter
    }

    override fun onProjectClick(model: Project) {
        findNavController().navigate(
            R.id.myProjectDetailsFragment,
            model.listing_number?.let { MyProjectDetailsFragmentArgs(it,model.created_at.toString()).toBundle() }
        )
    }
}