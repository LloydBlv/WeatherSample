package ir.zinutech.android.weatherapp.features.main.domain

import ir.zinutech.android.weatherapp.core.extensions.Coordination

data class Location(
    val latitude: Coordination,
    val longitude: Coordination
)