package eramo.amtalek.presentation.ui.main.broker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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
        rvBrokersAdapter.setListener(this)

        binding.rvBrokers.adapter = rvBrokersAdapter

        lifecycleScope.launch {

            // Collect data from PagingData flow
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.brokersPaging.collect { pagingData ->
                    rvBrokersAdapter.submitData(pagingData)
                }
            }
        }

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()
    }

    private fun setupToolbar() {

            binding.inToolbar.apply {
                toolbarIvMenu.setOnClickListener {
                    viewModelShared.openDrawer.value = true
                }
                spinnerLayout.setOnClickListener {
                        findNavController().navigate(R.id.filterCitiesDialogFragment, null, navOptionsAnimation())


                }
                FHomeEtSearch.setOnClickListener() {
                    findNavController().navigate(R.id.searchFormFragment, null, navOptionsFromTopAnimation())
                }
                inMessaging.ivMessaging.setOnClickListener {
                    if (UserUtil.isUserLogin()) {
                        findNavController().navigate(R.id.messagingChatFragment, null, navOptionsAnimation())
                    }else{
                        findNavController().navigate(R.id.loginDialog)
                    }
                }
                inNotification.ivNotification.setOnClickListener {
                    if (UserUtil.isUserLogin()) {
                    findNavController().navigate(R.id.notificationFragment, null, navOptionsAnimation())
                }else{
                    findNavController().navigate(R.id.loginDialog)
                }
                }


        }


        if (LocalUtil.isEnglish()) {
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_en))
        } else {
            binding.inToolbar.toolbarIvLogo.setImageDrawable(context?.getDrawable(R.drawable.top_logo_ar))
        }
    }

    override fun onBrokerClick(model: DataX) {
        findNavController().navigate(
            R.id.action_brokersFragment_to_brokersDetailsFragment,
            bundleOf("id" to model.id),
            navOptionsAnimation()
        )
    }
}
