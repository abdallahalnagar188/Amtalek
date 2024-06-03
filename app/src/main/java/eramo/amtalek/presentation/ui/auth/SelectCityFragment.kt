package eramo.amtalek.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import eramo.amtalek.R
import eramo.amtalek.databinding.FragmentSelectCityBinding
import eramo.amtalek.domain.model.auth.CityModel
import eramo.amtalek.domain.model.auth.CountryModel
import eramo.amtalek.presentation.adapters.recyclerview.RvSelectCityAdapter
import eramo.amtalek.presentation.adapters.recyclerview.RvSelectCountryAdapter
import eramo.amtalek.presentation.ui.BindingFragment
import eramo.amtalek.util.LocalUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectCityFragment : BindingFragment<FragmentSelectCityBinding>(), RvSelectCountryAdapter.OnItemClickListener,
    RvSelectCityAdapter.OnItemClickListener {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSelectCityBinding::inflate

    @Inject
    lateinit var rvSelectCountryAdapter: RvSelectCountryAdapter

    @Inject
    lateinit var rvSelectCityAdapter: RvSelectCityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        listeners()
    }

    private fun setupViews() {
        initCountriesRv(generateCountries())
//        initCitiesRv(generateCities())
    }

    private fun listeners() {
        selectDefaultCountryAndCity()

        binding.btnContinue.setOnClickListener {
            findNavController().navigate(
                R.id.onBoardingFragment, null,
                NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
            )
        }
    }

    private fun initCountriesRv(data: List<CountryModel>) {
        rvSelectCountryAdapter.setListener(this@SelectCityFragment)
        binding.rvCountries.adapter = rvSelectCountryAdapter
        rvSelectCountryAdapter.submitList(data)
    }

    private fun initCitiesRv(data: List<CityModel>) {
        rvSelectCityAdapter.setListener(this@SelectCityFragment)
        binding.rvCities.adapter = rvSelectCityAdapter
        rvSelectCityAdapter.submitList(data)
    }

    private fun selectDefaultCountryAndCity() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(10)
            binding.rvCountries.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
            binding.rvCities.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
        }
    }

    private fun generateCountries(): List<CountryModel> {
        val list = mutableListOf<CountryModel>()

        list.add(
            CountryModel(
                1, "Egypt",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/220px-Flag_of_Egypt.svg.png"
            )
        )

        list.add(
            CountryModel(
                2, "KSA",
                "https://cdn.britannica.com/79/5779-050-46C999AF/Flag-Saudi-Arabia.jpg"
            )
        )

        list.add(
            CountryModel(
                3, "UAE",
                "https://cdn.pixabay.com/photo/2012/04/10/23/01/united-arab-emirates-26815_640.png"
            )
        )

        return list
    }

    private fun generateCities(): List<CountryModel> {
        val list = mutableListOf<CountryModel>()

        list.add(
            CountryModel(
                1, "Cairo",
                "https://cdn.pixabay.com/photo/2012/04/10/23/01/united-arab-emirates-26815_640.png"
            )
        )

        list.add(
            CountryModel(
                2, "Alex",
                "https://cdn.britannica.com/79/5779-050-46C999AF/Flag-Saudi-Arabia.jpg"
            )
        )

        list.add(
            CountryModel(
                3, "Mansoura",
                "https://cdn.pixabay.com/photo/2012/04/10/23/01/united-arab-emirates-26815_640.png"
            )
        )

        return list
    }


    override fun onCountryClick(model: CountryModel) {
        Glide.with(requireContext()).load(model.imageUrl).into(binding.btnIvCountryFlag)
    }

    override fun onCityClick(model: CityModel) {
        if (LocalUtil.isEnglish()){
            binding.btnTvCityName.text = model.titleEn
        }else{
            binding.btnTvCityName.text = model.titleAr
        }
    }
}