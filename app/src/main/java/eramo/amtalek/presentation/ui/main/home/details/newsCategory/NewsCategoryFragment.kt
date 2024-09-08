package eramo.amtalek.presentation.ui.main.home.details.newsCategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.myHome.news.newscateg.NewsCategoryResponse
import eramo.amtalek.data.remote.dto.myHome.news.newscateg.NewsData
import eramo.amtalek.databinding.FragmentNewsCategoryBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvNewsCategoryAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.main.home.HomeMyViewModel
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsCategoryFragment : BindingFragment<FragmentNewsCategoryBinding>(),
    RvNewsCategoryAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewsCategoryBinding::inflate

    var id: Int? = 0
    lateinit var title: String


    val viewModel: HomeMyViewModel by viewModels()

    @Inject
    lateinit var rvNewsCategoryAdapter: RvNewsCategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments.let {
            NewsCategoryFragmentArgs.fromBundle(it!!).categoryId
        }.toInt()
        title = arguments.let {
            NewsCategoryFragmentArgs.fromBundle(it!!).titleName
        }
        viewModel.getNewsCategory(id ?: 0)
        setupViews()
        fetchNews()

    }


    private fun setupViews() {
        setupToolbar()
        setupRv()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            ivSearch.visibility = View.GONE
            etSearch.visibility = View.GONE
            tvTitle.text = title
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupRv() {
        rvNewsCategoryAdapter.setListener(this@NewsCategoryFragment)
        binding.rv.adapter = rvNewsCategoryAdapter
        binding.rv.setHasFixedSize(true)
    }

    private fun fetchNews() {
        lifecycleScope.launch {
            viewModel.newsCategory.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        LoadingDialog.showDialog()
                    }

                    is Resource.Success -> {
                        rvNewsCategoryAdapter.submitList(it.data?.data?.get(0)?.news?.data)
                        LoadingDialog.dismissDialog()
                    }

                    is Resource.Error -> {
                        LoadingDialog.dismissDialog()
                        showToast("no data here")
                    }
                }
            }

        }
    }

    override fun onNewsClick(model: NewsData) {
        findNavController().navigate(R.id.newsDetailsInCategoryFragment,
            model.let { NewsDetailsInCategoryFragmentArgs(model.id.toString()).toBundle() })
    }
}