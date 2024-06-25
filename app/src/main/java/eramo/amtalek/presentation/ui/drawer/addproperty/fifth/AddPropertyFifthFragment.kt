package eramo.amtalek.presentation.ui.drawer.addproperty.fifth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentAddPropertyFifthBinding
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.main.home.details.projects.AmenitiesAdapter
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddPropertyFifthFragment : BindingFragment<FragmentAddPropertyFifthBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddPropertyFifthBinding::inflate

    val viewModel by viewModels<AddPropertyFifthFragmentViewModel>()
    @Inject
    lateinit var amenitiesAdapter: SelectAmenitiesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        requestData()
        fetchData()
        clickListeners()
    }

    private fun clickListeners() {
        binding.btnSubmit.setOnClickListener(){
            Log.e("Ameen", amenitiesAdapter.selectionList.toString() )
        }
    }

    private fun fetchData() {
        fetchGetAmenitiesState()
    }

    private fun fetchGetAmenitiesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.propertyAmenitiesState.collect(){
                    when (it){
                        is UiState.Success->{
                            amenitiesAdapter.saveData(it.data!!)
                            binding.amenitiesRv.adapter = amenitiesAdapter
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Error->{
                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                            LoadingDialog.dismissDialog()
                        }
                        is UiState.Loading->{
                            LoadingDialog.showDialog()
                        }
                        is UiState.Empty->Unit
                    }
                }
            }
        }
    }

    private fun requestData() {
        viewModel.getPropertyAmenities()
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.add_property_5_5)
            ivBack.setOnClickListener { findNavController().popBackStack()}
        }
    }
}