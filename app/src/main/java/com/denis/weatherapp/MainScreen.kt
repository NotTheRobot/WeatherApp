package com.denis.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denis.weatherapp.ui.theme.mainPink
import com.denis.weatherapp.ui.theme.model.DayForecastUI

@Composable
fun MainScreen(){
    val viewModel: MainViewModel = viewModel (factory = MainViewModel.Factory)
    val locality = viewModel.locality
    val currTempValue = viewModel.currTemp
    val forecast = viewModel.forecast
    val isConnectionError = viewModel.isConnectionError
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (location, currTemp, forecastList, connectionStatus) = createRefs()
        if(isConnectionError) {
            Text(
                text = stringResource(id = R.string.connection_error),
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(size = 12.dp))
                    .background(Color.LightGray)
                    .constrainAs(connectionStatus) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, 32.dp)
                    })
        }
        Text(text = locality,
            color = mainPink,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(location){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, 96.dp)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            })

        Text(text = currTempValue,
            color = mainPink,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(currTemp){
                start.linkTo(location.start)
                end.linkTo(location.end)
                top.linkTo(location.bottom, 12.dp)
            }
        )

        LazyColumn(modifier = Modifier.constrainAs(forecastList){
            start.linkTo(parent.start, 12.dp)
            end.linkTo(parent.end, 12.dp)
            top.linkTo(currTemp.bottom, 64.dp)
            bottom.linkTo(parent.bottom)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }) {
            items(forecast){
                Item(it)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun Item(item: DayForecastUI){
    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Text(text = item.dayOfWeek,
            color = mainPink,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
        )
        Text(text = item.avgTemp,
            color = mainPink,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
        )
    }
}
