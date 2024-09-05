package eramo.amtalek.presentation.ui.main.broker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.broker.entity.DataX
import eramo.amtalek.databinding.FragmentBrokersBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvBrokersAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.LocalUtil
import eramo.amtalek.util.UserUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.navOptionsFromTopAnimation
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BrokersFragment : BindingFragment<FragmentBrokersBinding>(),
    RvBrokersAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokersBinding::inflate

    private val viewModelShared: SharedViewModel by activityViewModels()
    private val viewModel: BrokersViewModel by viewModels()

    @Inject
    lateinit var rvBrokersAdapter: RvBrokersAdapter
    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupRecyclerView()
        observeBrokersData()
    }

    private fun setupRecyclerView() {
        // Set up the adapter for the RecyclerView
        binding.rvBrokers.adapter = rvBrokersAdapter
        rvBrokersAdapter.setListener(this)
    }

    private fun observeBrokersData() {
        // Observe brokers data from the ViewModel and handle loading states
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Start observing the paging data
                viewModel.brokersPaging
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED) // Lifecycle-aware collection
                    .collect { pagingData ->

                        // Submit the data to the adapter
                        rvBrokersAdapter.submitData(pagingData)

                        // Scroll to the top after new data arrives
                        binding.rvBrokers.scrollToPosition(0)

                    }
            }
        }
    }

    private fun handleDataLoadingState(isLoading: Boolean) {
        if (isLoading) {
            LoadingDialog.showDialog()
        } else {
            LoadingDialog.dismissDialog()
        }
    }

    private fun setupViews() {
        // Setup toolbar and visibility logic
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            if (UserUtil.getUserType() == "broker") {
                inMessaging.root.visibility = View.GONE
                inNotification.root.visibility = View.GONE
            } else {
                inMessaging.root.visibility = View.VISIBLE
                inNotification.root.visibility = View.VISIBLE
            }

            toolbarIvMenu.setOnClickListener {
                viewModelShared.openDrawer.value = true
            }

            spinnerLayout.setOnClickListener {
                findNavController().navigate(
                    R.id.filterCitiesDialogFragment,
                    null,
                    navOptionsAnimation()
                )
            }

            FHomeEtSearch.setOnClickListener {
                findNavController().navigate(
                    R.id.searchFormFragment,
                    null,
                    navOptionsFromTopAnimation()
                )
            }

            inMessaging.ivMessaging.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    findNavController().navigate(
                        R.id.messagingChatFragment,
                        null,
                        navOptionsAnimation()
                    )
                } else {
                    findNavController().navigate(R.id.loginDialog)
                }
            }

            inNotification.ivNotification.setOnClickListener {
                if (UserUtil.isUserLogin()) {
                    findNavController().navigate(
                        R.id.notificationFragment,
                        null,
                        navOptionsAnimation()
                    )
                } else {
                    findNavController().navigate(R.id.loginDialog)
                }
            }

            setToolbarCityTitle()
            setToolbarLogo()
        }
    }

    private fun setToolbarCityTitle() {
        val cityTitle = if (LocalUtil.isEnglish()) {
            UserUtil.getCityFiltrationTitleEn()
        } else {
            UserUtil.getCityFiltrationTitleAr()
        }

        binding.inToolbar.tvSpinnerText.text = if (cityTitle.isEmpty()) {
            context?.getString(R.string.egypt)
        } else {
            cityTitle
        }
    }

    private fun setToolbarLogo() {
        val logoDrawable = if (LocalUtil.isEnglish()) {
            R.drawable.top_logo_en
        } else {
            R.drawable.top_logo_ar
        }

        binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(logoDrawable))
    }

    override fun onResume() {
        super.onResume()
        // Clear the search text and remove focus when resuming the fragment
        binding.inToolbar.FHomeEtSearch.text.clear()
        binding.inToolbar.FHomeEtSearch.clearFocus()
    }

    override fun onBrokerClick(model: DataX) {
        // Navigate to the broker details screen with the broker ID
        findNavController().navigate(
            R.id.action_brokersFragment_to_brokersDetailsFragment,
            bundleOf("id" to model.id),
            navOptionsAnimation()
        )
    }

}
