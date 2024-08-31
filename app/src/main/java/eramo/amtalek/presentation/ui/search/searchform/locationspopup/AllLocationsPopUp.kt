package eramo.amtalek.presentation.ui.search.searchform.locationspopup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.DialogAllLocationsBinding
import eramo.amtalek.domain.search.LocationModel
import eramo.amtalek.presentation.ui.dialog.LoadingDialog
import eramo.amtalek.presentation.ui.search.searchform.SearchFormViewModel
import eramo.amtalek.presentation.viewmodel.SharedViewModel
import eramo.amtalek.util.selectedLocation
import eramo.amtalek.util.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllLocationsPopUp : DialogFragment(R.layout.dialog_all_locations),AllLocationsAdapter.OnItemLocationClick {
    private lateinit var binding: DialogAllLocationsBinding
    val viewModel by viewModels<SearchFormViewModel>()
    val sharedViewModel by viewModels<SharedViewModel>()
    @Inject
    lateinit var locationAdapter: AllLocationsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAllLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestData()
        fetchData()
        initViews()
    }

    private fun initViews() {
        binding.apply {
            rvLocations.adapter = locationAdapter
        }
        locationAdapter.setListener(this@AllLocationsPopUp)
    }

    private fun fetchData() {
        fetchLocations()
    }

    private fun fetchLocations() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allLocationState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            LoadingDialog.showDialog()
                        }

                        is UiState.Success -> {
                            LoadingDialog.dismissDialog()

                            // Create a placeholder LocationModel
                            val placeholderLocation = LocationModel(
                                id = -1, // Unique ID for the placeholder
                                propertiesCount = 0, // Placeholder value
                                title = getText(R.string.location).toString(), // Placeholder text
                            )

                            // Add the placeholder location at index 0
                            val locations = mutableListOf(placeholderLocation).apply {
                                uiState.data?.let { addAll(it) }
                            }

                            locationAdapter.submitList(locations)
                            handleLocations(locations)
                        }

                        is UiState.Error -> {
                            dismiss()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }


    private fun handleLocations(locations: List<LocationModel>?) {
        binding.apply {
            searchView.clearFocus()
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // Handle the query submission
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filteredList = ArrayList<LocationModel>()

                    for (item in locations ?: emptyList()) {
                        // Skip the placeholder item in the search
                        if (item.id == -1) continue

                        // Check if the item's title contains the search text
                        if (newText != null && item.title.lowercase().contains(newText.lowercase())) {
                            filteredList.add(item)
                        }
                    }

                    if (filteredList.isEmpty()) {
                        Toast.makeText(requireContext(), getText(R.string.no_results_found), Toast.LENGTH_SHORT).show()
                    } else {
                        locationAdapter.submitList(filteredList)
                    }

                    return false
                }
            })
        }
    }




    private fun requestData() {
        viewModel.getAllLocations()
    }

    override fun onLocationClicked(model: LocationModel) {
       selectedLocation.value = model
        dismiss()
    }
}