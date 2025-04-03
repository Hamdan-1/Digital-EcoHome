package com.trailblazers.digitalecohome

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

object AppDestinations {
    const val DASHBOARD = "dashboard"
    const val DEVICES = "devices"
    const val REPORT = "report"
}

data class NavDrawerItem(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector
)

val drawerItems = listOf(
    NavDrawerItem(AppDestinations.DASHBOARD, R.string.menu_dashboard, Icons.Filled.Home),
    NavDrawerItem(AppDestinations.DEVICES, R.string.menu_devices, Icons.Filled.Devices),
    NavDrawerItem(AppDestinations.REPORT, R.string.menu_report, Icons.Filled.Assessment)
)
