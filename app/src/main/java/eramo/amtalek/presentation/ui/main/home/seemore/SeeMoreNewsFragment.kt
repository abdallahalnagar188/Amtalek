package eramo.amtalek.presentation.ui.main.home.seemore

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
import eramo.amtalek.data.remote.dto.project.allProjects.DataX
import eramo.amtalek.databinding.FragmentSeeMoreProjectsBinding
import eramo.amtalek.databinding.FragmentSeeMorePropertiesByCityBinding
import eramo.amtalek.domain.model.home.news.NewsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvNewsAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvProjectsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.NewsDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.projects.MyProjectDetailsFragmentArgs
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMoreNewsFragment : BindingFragment<FragmentSeeMorePropertiesByCityBinding>(), RvNewsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesByCityBinding::inflate

    private val args by navArgs<SeeMoreProjectsFragmentArgs>()
    private val projectsList get() = args.projectsList

    val viewModel: HomeMyViewModel by viewModels()

    @Inject
    lateinit var rvNewsAdapter: RvNewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHomeNews()

        lifecycleScope.launch {
            viewModel.homeNewsState.collect {
//                Log.e("elnagar", it?.data?.original?.data.toString())
//                rvProjectsAdapter.submitList(it?.data?.original?.data)
//                binding.rvProperties.adapter = rvProjectsAdapter
                it.data?.get(0)?.news?.let { it1 -> setupViews(it1) }
            }
        }

    }


    private fun setupViews(list: List<NewsModel>) {
        setupToolbar()
        setupRv(list)
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = "Featured Projects in Egypt"
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupRv(data: List<NewsModel>) {
        rvNewsAdapter.setListener(this@SeeMoreNewsFragment)
        binding.rv.adapter = rvNewsAdapter
        rvNewsAdapter.submitList(data)
    }

    override fun onPropertyClick(model: NewsModel) {
        findNavController().navigate(
            R.id.newsDetailsFragment,
            model.id.let { NewsDetailsFragmentArgs(model).toBundle() }
        )
    }
}