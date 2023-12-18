package eramo.amtalek.presentation.ui.main.home.seemore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSeeMorePropertiesByCityBinding
import eramo.amtalek.domain.model.main.home.PropertiesByCityModel
import eramo.amtalek.presentation.adapters.recyclerview.RvPropertiesByCityAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import javax.inject.Inject

@AndroidEntryPoint
class SeeMorePropertiesByCityFragment : BindingFragment<FragmentSeeMorePropertiesByCityBinding>(),
    RvPropertiesByCityAdapter.OnItemClickListener {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSeeMorePropertiesByCityBinding::inflate

    private val args by navArgs<SeeMorePropertiesByCityFragmentArgs>()
    private val citiesList get() = args.citiesList

    @Inject
    lateinit var rvPropertiesByCityAdapter: RvPropertiesByCityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupToolbar()

        initRv(citiesList.toList())
    }

    private fun setupToolbar() {
        binding.inToolbar.apply {
            tvTitle.text = getString(R.string.find_your_property_in_the_city)
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initRv(data: List<PropertiesByCityModel>) {
        rvPropertiesByCityAdapter.setListener(this@SeeMorePropertiesByCityFragment)
        binding.rv.adapter = rvPropertiesByCityAdapter
        rvPropertiesByCityAdapter.submitList(data)

        setupSearchRv(data)
    }

    private fun setupSearchRv(data: List<PropertiesByCityModel>) {
        binding.inToolbar.etSearch.addTextChangedListener { text ->
            if (text.toString().isEmpty()) {
                rvPropertiesByCityAdapter.submitList(data)
            } else {
                val list = data.filter {
                    it.cityName.lowercase().contains(text.toString().lowercase())
                }
                rvPropertiesByCityAdapter.submitList(null)
                rvPropertiesByCityAdapter.submitList(list)
            }
        }
    }

    override fun onCityClick(model: PropertiesByCityModel) {

    }
}