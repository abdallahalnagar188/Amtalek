package eramo.amtalek.presentation.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentUserProfileBinding
import eramo.amtalek.domain.model.main.market.MarketPostsModel
import eramo.amtalek.presentation.adapters.recyclerview.RvUserProfilePostsAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.showToast
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : BindingFragment<FragmentUserProfileBinding>(), RvUserProfilePostsAdapter.OnItemClickListener {

    override val isRefreshingEnabled: Boolean get() = false
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUserProfileBinding::inflate

    @Inject
    lateinit var rvUserProfilePostsAdapter: RvUserProfilePostsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        StatusBarUtil.transparent()

        assignFakeData()
        initRv()
    }

    private fun setupListeners() {
        binding.apply {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            ivShare.setOnClickListener { showToast("share") }
        }
        setupHeaderListener()
    }

    private fun assignFakeData() {
        binding.apply {
            tvUserName.text = "Ahmed Mohamed"
            tvLocation.text = "Cairo"
            tvDescription.text =
                "This guide will help you write your professional profile and show you some examples you can use to get started."

            Glide.with(requireContext())
                .load("https://www.worldbank.org/content/dam/photos/780x439/2022/jan-2/deep-blue-sea_shutterstock_1512728810.jpg")
                .into(ivUserCover)
            Glide.with(requireContext())
                .load("https://cdn2.momjunction.com/wp-content/uploads/2021/02/What-Is-A-Sigma-Male-And-Their-Common-Personality-Trait-624x702.jpg")
                .into(ivUserProfile)
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
        rvUserProfilePostsAdapter.setListener(this@UserProfileFragment)
        binding.rv.adapter = rvUserProfilePostsAdapter
        rvUserProfilePostsAdapter.submitList(Dummy.dummyMarketPostsList())
    }

    override fun onPhotosClickPhotosPost(model: MarketPostsModel) {

    }
}