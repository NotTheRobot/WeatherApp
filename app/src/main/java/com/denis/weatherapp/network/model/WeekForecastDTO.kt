package com.denis.weatherapp.network.model

import android.content.Context
import android.icu.util.Calendar
import com.denis.weatherapp.R
import com.denis.weatherapp.ui.theme.model.DayForecastUI
import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class WeekForecastDTO(
    @SerializedName("date")
    val date: String,
    @SerializedName("tavg")
    val avgTemp: Float,
)

fun WeekForecastDTO.toDayForecast(context: Context): DayForecastUI{
    val formatter1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.time = formatter1.parse(this.date)
    val formatter2: DateFormat = SimpleDateFormat("E", Locale.getDefault())
    val today = Calendar.getInstance()

    return DayForecastUI(
        dayOfWeek = if(today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)){
        context.getString(R.string.today)
        }else {
            formatter2.format(calendar.time)
        },
        avgTemp = this.avgTemp.toInt().toString() + Char(0xB0)
    )
}
