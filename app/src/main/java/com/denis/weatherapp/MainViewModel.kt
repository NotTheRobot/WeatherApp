package com.denis.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.denis.weatherapp.network.model.MeteostatAPI
import com.denis.weatherapp.network.model.toDayForecast
import com.denis.weatherapp.ui.theme.model.DayForecastUI
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@SuppressLint("MissingPermission")
class MainViewModel(private val context: Context): ViewModel() {

    //ui states
    var currTemp by mutableStateOf("")
    var locality by mutableStateOf("")
    var forecast = mutableStateListOf<DayForecastUI>()
    var isConnectionError by mutableStateOf(true)

    //api
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://meteostat.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val serviceAPI: MeteostatAPI = retrofit.create(MeteostatAPI::class.java)

    //location
    private var lat: Double = 56.833
    private var lon: Double = 60.58
    private val gcd = Geocoder(context, Locale.getDefault())
    private var locationServices = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10_000)
        .setWaitForAccurateLocation(true)
        .build()
    private val locationCallback = object: LocationCallback(){
        override fun onLocationResult(locResult: LocationResult) {
            super.onLocationResult(locResult)
            locResult.lastLocation?.let {
                lat = it.latitude
                lon = it.longitude
                val addresses = gcd.getFromLocation(lat, lon, 1)
                setLocalityWithAddress(addresses)
                updateAll()
            }
        }
    }

    init {
        locationServices.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
        updateAll()
    }

    private fun setLocalityWithAddress(addresses: List<Address>?){
        if(addresses?.isNotEmpty() == true){
            locality = addresses[0].locality
        }
    }

    fun updateAll(){
        updateWeekForecast()
        updateCurrentTemp()
    }

    private fun updateWeekForecast() {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val start = formatter.format(calendar.time)
        calendar.add(Calendar.DATE, 6)
        val end = formatter.format(calendar.time)

        viewModelScope.launch {
            try {
                val result =
                    serviceAPI.getForecast(lat.toFloat(), lon.toFloat(), start, end).data.map {
                        it.toDayForecast(context)
                    }
                forecast.clear()
                forecast.addAll(result)
                isConnectionError = false
            } catch (e: Exception) {
                isConnectionError = true
            }
        }
    }

    private fun updateCurrentTemp() {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = formatter.format(calendar.time)
        val tz = TimeZone.getDefault().id.toString()

        viewModelScope.launch {
            try {
                val result = serviceAPI.getCurrentTemp(
                    lat.toFloat(), lon.toFloat(),
                    today, today, tz
                ).data
                currTemp = result[calendar.get(Calendar.HOUR_OF_DAY)].avgTemp.toInt().toString() + Char(0xB0)
                isConnectionError = false
            } catch (e: Exception) {
                isConnectionError = true
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer{
                val context = this[APPLICATION_KEY]
                MainViewModel((context as Context))
            }
        }
    }
}
