package com.denis.weatherapp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.denis.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    private var hasPermissions by mutableStateOf(false)
    private val permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION)
    private val locationPermission = getLocationPermission()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                if (hasPermissions) {
                    MainScreen()
                } else {
                    grantLocationPermissions()
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = stringResource(id = R.string.require_permission_message),
                            textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { grantLocationPermissions() }) {
                            Text(text = stringResource(id = R.string.grant_access))
                        }
                    }
                }
            }
        }
    }

    private fun grantLocationPermissions(){
        val notGranted = permissions.filterNot {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        locationPermission.launch(notGranted.toTypedArray())
        if(permissions.filterNot {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }.isNotEmpty()){
            hasPermissions = false
        }else{
            hasPermissions = true
        }
    }

    private fun getLocationPermission() =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                hasPermissions = true
            } else {
                hasPermissions = false
            }
        }
}

