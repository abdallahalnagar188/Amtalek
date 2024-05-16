package eramo.amtalek.presentation.ui.main.broker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentBrokerDetailsBinding
import eramo.amtalek.domain.model.drawer.myfavourites.PropertyModel
import eramo.amtalek.presentation.adapters.recyclerview.RvBrokerDetailsPropertiesAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import eramo.amtalek.util.navOptionsAnimation
import eramo.amtalek.util.showToast
import javax.inject.Inject

@AndroidEntryPoint
class BrokersDetailsFragment : BindingFragment<FragmentBrokerDetailsBinding>(), RvBrokerDetailsPropertiesAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBrokerDetailsBinding::inflate

//    private val viewModelShared: SharedViewModel by activityViewModels()
//    private val viewModel: CartViewModel by viewModels()

    @Inject
    lateinit var rvBrokerDetailsPropertiesAdapter: RvBrokerDetailsPropertiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        super.registerApiRequest { viewModel.cartData() }
//        super.registerApiCancellation { viewModel.cancelRequest() }

        setupViews()
        setupListeners()
    }

    override fun onPause() {
        super.onPause()
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)
    }

    private fun setupViews() {
        setupToolbar()
        assignData()

        initRv(Dummy.dummyMyFavouritesList(requireContext()))
    }

    private fun setupListeners() {
        setupHeaderListener()

        binding.apply {
            btnProjects.setOnClickListener {
                findNavController().navigate(R.id.completedProjectsFragment, null, navOptionsAnimation())
            }

            btnSatisfiedCustomers.setOnClickListener {
                findNavController().navigate(R.id.satisfiedCustomersFragment, null, navOptionsAnimation())
            }
        }
    }

    private fun setupToolbar() {
        StatusBarUtil.transparent()
        binding.apply {
            ivShare.setOnClickListener { showToast("share") }
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupHeaderListener() {

        binding.apply {

            tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))

            tvAll.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            }

            tvForSell.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
            }

            tvForRent.setOnClickListener {
                tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForSell.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_faded_gray))
                tvForRent.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
    }

    private fun assignData() {
        binding.apply {
            tvTitle.text = "Lana Real Estate Development Company"
            tvDescription.text = "A leading real estate company in real estate design for more than 100 years"
            tvLocation.text = "Sheikh Zayed Giza"
            tvProjectsCount.text = getString(R.string.s_projects, "105")
            tvSatisfiedCustomers.text = getString(R.string.s_satisfied_customers, "605")
            tvAllProjectsCount.text = getString(R.string.s_all_properties, "105")

            Glide.with(requireContext()).load("https://www.era-egypt.com/wp-content/uploads/2021/06/ERA-2004.png").into(ivBrokerLogo)
        }
    }

    private fun initRv(data: List<PropertyModel>) {
        rvBrokerDetailsPropertiesAdapter.setListener(this@BrokersDetailsFragment)
        binding.rv.adapter = rvBrokerDetailsPropertiesAdapter
        rvBrokerDetailsPropertiesAdapter.submitList(data)
    }

    override fun onPropertyClick(model: PropertyModel) {

    }
}