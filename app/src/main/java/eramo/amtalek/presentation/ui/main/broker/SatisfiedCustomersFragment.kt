package eramo.amtalek.presentation.ui.main.broker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSatisfiedCustomersBinding
import eramo.amtalek.presentation.adapters.recyclerview.RvRatingAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.Dummy
import eramo.amtalek.util.StatusBarUtil
import javax.inject.Inject

@AndroidEntryPoint
class SatisfiedCustomersFragment : BindingFragment<FragmentSatisfiedCustomersBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSatisfiedCustomersBinding::inflate

    @Inject
    lateinit var rvRatingAdapter: RvRatingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()
        initCommentsRv()
    }

    private fun setupToolbar() {
        StatusBarUtil.blackWithBackground(requireActivity(), R.color.white)

        binding.apply {
            inToolbar.tvTitle.text = getString(R.string.s_satisfied_customers, "122")
            inToolbar.ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initCommentsRv() {
        binding.rv.adapter = rvRatingAdapter
        rvRatingAdapter.submitList(Dummy.dummyRatingCommentsList())
    }
}