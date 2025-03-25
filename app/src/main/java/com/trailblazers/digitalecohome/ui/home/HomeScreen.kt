package com.trailblazers.digitalecohome.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trailblazers.digitalecohome.R

@Composable
fun HomeScreen() {
    val energyData = remember {
        EnergyData(
            totalUsage = 342,
            todayUsage = 12.5,
            savingsThisMonth = 15,
            devices = listOf(
                Device("Living Room", 120, 35, "active"),
                Device("Kitchen", 95, 28, "active"),
                Device("Bedroom", 75, 22, "sleep"),
                Device("Bathroom", 52, 15, "inactive")
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmartEnergy") },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = true,
                    onClick = {}
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Lightbulb, contentDescription = null) },
                    label = { Text("Control") },
                    selected = false,
                    onClick = {}
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.BarChart, contentDescription = null) },
                    label = { Text("Usage") },
                    selected = false,
                    onClick = {}
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Warning, contentDescription = null) },
                    label = { Text("Alerts") },
                    selected = false,
                    onClick = {}
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {}
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            EnergyOverviewCard(energyData)
            Spacer(modifier = Modifier.height(16.dp))
            QuickActions()
            Spacer(modifier = Modifier.height(16.dp))
            RoomEnergyUsage(energyData.devices)
            Spacer(modifier = Modifier.height(16.dp))
            EnergySavingTip()
        }
    }
}

@Composable
fun EnergyOverviewCard(energyData: EnergyData) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Energy Overview",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "${energyData.savingsThisMonth}% saved this month",
                    style = MaterialTheme.typography.body2,
                    color = Color.Green
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    progress = energyData.totalUsage / 500f,
                    modifier = Modifier.size(100.dp),
                    color = MaterialTheme.colors.primary,
                    strokeWidth = 8.dp
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${energyData.totalUsage}",
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text = "kWh this month",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Today's Usage: ${energyData.todayUsage} kWh",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
fun QuickActions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionButton(icon = Icons.Filled.Home, label = "All Devices", selected = true)
        QuickActionButton(icon = Icons.Filled.Lightbulb, label = "Lighting", selected = false)
        QuickActionButton(icon = Icons.Filled.Warning, label = "Alerts", selected = false)
    }
}

@Composable
fun QuickActionButton(icon: ImageVector, label: String, selected: Boolean) {
    val backgroundColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    val contentColor = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = contentColor)
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = contentColor
        )
    }
}

@Composable
fun RoomEnergyUsage(devices: List<Device>) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Room Energy Usage",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                Icon(Icons.Filled.BarChart, contentDescription = null, tint = MaterialTheme.colors.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            devices.forEach { device ->
                RoomEnergyUsageItem(device)
                Spacer(modifier = Modifier.height(8.dp))
            }
            TextButton(onClick = { /* TODO: Navigate to detailed analysis */ }) {
                Text(
                    text = "See detailed analysis",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun RoomEnergyUsageItem(device: Device) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = device.name,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.width(100.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        LinearProgressIndicator(
            progress = device.percentage / 100f,
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .background(MaterialTheme.colors.background, shape = RoundedCornerShape(4.dp)),
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${device.usage} kWh",
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        StatusIndicator(status = device.status)
    }
}

@Composable
fun StatusIndicator(status: String) {
    val color = when (status) {
        "active" -> MaterialTheme.colors.primary
        "sleep" -> Color.Yellow
        "inactive" -> Color.Green
        else -> Color.Gray
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = status,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun EnergySavingTip() {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.PieChart, contentDescription = null, tint = MaterialTheme.colors.onPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Energy Saving Tip",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your living room devices are using more energy than usual. Consider adjusting your smart thermostat by 2°C to save up to 15% more energy.",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

data class EnergyData(
    val totalUsage: Int,
    val todayUsage: Double,
    val savingsThisMonth: Int,
    val devices: List<Device>
)

data class Device(
    val name: String,
    val usage: Int,
    val percentage: Int,
    val status: String
)
