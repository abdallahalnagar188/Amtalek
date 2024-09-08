package eramo.amtalek.presentation.ui.main.home.seemore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.myHome.news.allnews.DataX
import eramo.amtalek.databinding.FragmentSeeMoreNewsBinding
import eramo.amtalek.databinding.FragmentSeeMorePropertiesByCityBinding
import eramo.amtalek.domain.model.home.news.NewsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvNewsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.presentation.ui.main.home.details.NewsDetailsFragmentArgs
import eramo.amtalek.presentation.ui.main.home.details.NewsDetailsInSeeMoreFragmentArgs
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeeMoreNewsFragment : BindingFragment<FragmentSeeMoreNewsBinding>(),
    RvNewsAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMoreNewsBinding::inflate


    val viewModel: HomeMyViewModel by viewModels()

    @Inject
    lateinit var rvNewsAdapter: RvNewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHomeNews()
        setupViews()
        fetchNews()

//        lifecycleScope.launch {
//            viewModel.homeNewsState.collect {
////                Log.e("elnagar", it?.data?.original?.data.toString())
////                rvProjectsAdapter.submitList(it?.data?.original?.data)
////                binding.rvProperties.adapter = rvProjectsAdapter
//                it.data?.get(0)?.news?.let { it1 -> setupViews(it1) }
//            }
//        }

    }


    private fun setupViews() {
        setupToolbar()
        setupRv()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            ivSearch.visibility = View.GONE
            etSearch.visibility = View.GONE
            tvTitle.text = getString(R.string.news)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupRv() {
        rvNewsAdapter.setListener(this@SeeMoreNewsFragment)
        binding.rv.adapter = rvNewsAdapter
    }

    private fun fetchNews() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allNewsPagingFlow.collectLatest { pagingData ->
                    rvNewsAdapter.submitData(pagingData)
                }
            }
        }
    }

    override fun onNewsClick(model: DataX) {
        findNavController().navigate(
            R.id.newsDetailsFragmentInSeeMore,
            model.let {
                NewsDetailsInSeeMoreFragmentArgs(
                  id = it.id.toString()
                ).toBundle()
            }
        )
    }
}