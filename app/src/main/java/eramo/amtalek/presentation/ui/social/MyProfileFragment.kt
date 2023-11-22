package eramo.amtalek.presentation.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentMyProfileBinding
import eramo.amtalek.domain.model.auth.UserModel
import eramo.amtalek.domain.model.main.market.MarketPostsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvMyProfilePostsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.main.extension.imagviewer.ImagesListFragmentArgs
import eramo.amtalek.presentation.viewmodel.social.MyProfileViewModel
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import eramo.amtalek.util.state.UiState
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : BindingFragment<FragmentMyProfileBinding>(), RvMyProfilePostsAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMyProfileBinding::inflate

    private val viewModel by viewModels<MyProfileViewModel>()

    @Inject
    lateinit var rvMyProfilePostsAdapter: RvMyProfilePostsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()

        requestData()
        fetchData()
    }

    private fun setupViews() {
        StatusBarUtil.transparent()

        initRv()
    }

    private fun listeners() {
        binding.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            ivEdit.setOnClickListener {
                findNavController().navigate(
                    R.id.myAccountFragment,
                    null,
                    navOptionsAnimation()
                )
            }
        }

        setupHeaderListener()
    }

    private fun requestData() {
        viewModel.getProfile()
    }

    private fun fetchData() {
        fetchGetProfileState()
    }

    private fun fetchGetProfileState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getProfileState.collect { state ->
                    when (state) {

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()
                            assignUserData(state.data!!)
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            val errorMessage = state.message!!.asString(requireContext())
                            showToast(errorMessage)
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun assignUserData(user: UserModel) {
        binding.apply {
            tvUserName.text = getString(R.string.S_user_name, user.firstName, user.lastName)
            tvLocation.text = user.cityName
            tvBio.text = user.bio

            Glide.with(requireContext()).load(user.coverImageUrl).into(ivUserCover)

            if (user.profileImageUrl != "") {
                Glide.with(requireContext()).load(user.profileImageUrl).into(ivUserProfile)
            } else {
                Glide.with(requireContext()).load(R.drawable.avatar).into(ivUserProfile)
            }
        }
    }

    private fun setupHeaderListener() {
        binding.apply {

            tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            tvProperties.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            tvPosts.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

            tvAll.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvProperties.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvPosts.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            }

            tvProperties.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvProperties.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvPosts.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            }

            tvPosts.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvProperties.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvPosts.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
    }

    private fun initRv() {
        rvMyProfilePostsAdapter.setListener(this@MyProfileFragment)
        binding.rv.adapter = rvMyProfilePostsAdapter
        rvMyProfilePostsAdapter.submitList(Dummy.dummyMarketPostsList())
    }

    override fun onPhotosClickPhotosPost(model: MarketPostsModel) {
        findNavController().navigate(
            R.id.imagesListFragment,
            ImagesListFragmentArgs(model.photosList?.toTypedArray() ?: emptyArray()).toBundle(),
            navOptionsAnimation()
        )

    }
}