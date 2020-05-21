package ir.zinutech.android.weatherapp.features.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import ir.zinutech.android.weatherapp.R
import ir.zinutech.android.weatherapp.databinding.ActivityMainBinding
import ir.zinutech.android.weatherapp.features.main.domain.Weather
import ir.zinutech.android.weatherapp.features.main.utils.WeatherExceptionsParser
import ir.zinutech.android.weatherapp.features.main.utils.WeatherListener
import ir.zinutech.android.weatherapp.features.main.viewmodels.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), WeatherListener {


    /**
     * ViewModel to do business logic in, Injected by using Koin
     */
    private val viewModel: MainViewModel by viewModel()

    /**
     * View Binding for this class
     */
    private lateinit var binding: ActivityMainBinding


    private val weatherExceptionParser by inject<WeatherExceptionsParser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.weatherListener = this
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.weather.observe(this) {
            updateUiElementsVisibilityIfWeatherLoaded(it)
            when (it) {
                is MainViewModel.ViewState.Loading -> {
                    binding.mainActivityStatusTv.text = getString(R.string.loading)
                }

                is MainViewModel.ViewState.WeatherLoadFailed -> {
                    binding.mainActivityStatusTv.text =
                        weatherExceptionParser.parseException(it.exception)
                }

                is MainViewModel.ViewState.WeatherLoaded -> {
                    bindWeatherData(it.weather)
                }
            }
        }
    }

    private fun bindWeatherData(weather: Weather) {
        binding.mainActivityTemperatureTv.text = weather.currently?.temperature?.toString()
        binding.mainActivitySummaryTv.text = weather.currently?.summary
        binding.mainActivityFeelsTv.text = getString(
            R.string.feels_like,
            weather.currently?.apparentTemperature?.toString()
        )
    }

    /**
     * toggles visibility of UI widgets based on if the api returned successfully
     * @param isSuccess the indicator that shows if weather data is loaded or not
     */
    private fun updateUiElementsVisibilityIfWeatherLoaded(state: MainViewModel.ViewState) {
        val isSuccess =  state is MainViewModel.ViewState.WeatherLoaded
        binding.mainActivityRetryBtn.isVisible = state is MainViewModel.ViewState.WeatherLoadFailed
        binding.mainActivityStatusTv.isVisible = !isSuccess
        binding.mainActivityCelsiusTv.isVisible = isSuccess
        binding.mainActivityTemperatureTv.isVisible = isSuccess
        binding.mainActivityFeelsTv.isVisible = isSuccess
        binding.mainActivitySummaryTv.isVisible = isSuccess
    }

    override fun onRetryButtonClicked() {
        viewModel.onRetryClicked()
    }
}
