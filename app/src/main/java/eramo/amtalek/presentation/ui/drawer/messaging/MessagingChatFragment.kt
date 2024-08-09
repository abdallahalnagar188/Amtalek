package eramo.amtalek.presentation.ui.drawer.messaging

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.RenderScriptBlur
import eramo.amtalek.R
import eramo.amtalek.data.remote.dto.contactedAgent.Data
import eramo.amtalek.databinding.FragmentMessagingChatBinding
import eramo.amtalek.presentation.adapters.recyclerview.messaging.RvMessagingChatAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessagingChatFragment : BindingFragment<FragmentMessagingChatBinding>(),
    RvMessagingChatAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMessagingChatBinding::inflate

    @Inject
    lateinit var rvMessagingChatAdapter: RvMessagingChatAdapter
    val viewModel: MessagingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {

        binding.inToolbar.tvTitle.text = getString(R.string.messagings)
        binding.inToolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addAdoms.setOnClickListener {
            findNavController().navigate(R.id.addAdomsFragment)
        }
        viewModel.getContactedAgents()
        lifecycleScope.launch {
            viewModel.contactedAgents.collect {
                initChatRv(viewModel.contactedAgents.value?.data ?: emptyList())
            }
        }
    }

    private fun initChatRv(data: List<Data?>) {
        rvMessagingChatAdapter.setListener(this@MessagingChatFragment)
        binding.rv.adapter = rvMessagingChatAdapter
        rvMessagingChatAdapter.submitList(data)
    }

    override fun onChatClick(model: Data) {
        if (model.messageType == "valid") {
            val action = MessagingChatFragmentDirections.actionToUsersChatFragment(
                model.id.toString(),
                model.actorType.toString(),
                model.name.toString(),
                model.image
            )
            findNavController().navigate(action)
        } else {
            showToast("Agent is not valid")
            blurChatScreen()
        }
    }

    private fun blurChatScreen() {
        binding.blurView.apply {
            setupWith(binding.root)
                .setBlurAlgorithm(RenderScriptBlur(requireContext()))
                .setBlurRadius(10f)
                .setOverlayColor(Color.parseColor("#99000000"))
                .setHasFixedTransformationMatrix(true)
            visibility = View.VISIBLE
        }
    }
}
