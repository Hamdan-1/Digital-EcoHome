package com.trailblazers.digitalecohome.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.trailblazers.digitalecohome.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var lineChart: LineChart
    private lateinit var toggleLivingRoomLight: ToggleButton
    private lateinit var toggleMasterBedroomAC: ToggleButton
    private lateinit var toggleKitchenFridge: ToggleButton
    private lateinit var toggleLivingRoomTV: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textCurrentConsumption: TextView = binding.textCurrentConsumption
        val textDailyAverage: TextView = binding.textDailyAverage
        val textMonthlyUsage: TextView = binding.textMonthlyUsage
        val textEstimatedSavings: TextView = binding.textEstimatedSavings
        val textAlertHighConsumption: TextView = binding.textAlertHighConsumption
        val textAlertDeviceOffline: TextView = binding.textAlertDeviceOffline

        lineChart = binding.lineChartEnergyConsumption
        toggleLivingRoomLight = binding.toggleLivingRoomLight
        toggleMasterBedroomAC = binding.toggleMasterBedroomAC
        toggleKitchenFridge = binding.toggleKitchenFridge
        toggleLivingRoomTV = binding.toggleLivingRoomTV

        homeViewModel.text.observe(viewLifecycleOwner) {
            textCurrentConsumption.text = "Current Consumption: 3.45 kWh"
            textDailyAverage.text = "Daily Average: 4.12 kWh"
            textMonthlyUsage.text = "Monthly Usage: 125.67 kWh"
            textEstimatedSavings.text = "Estimated Savings: 15.20 AED"
            textAlertHighConsumption.text = "High energy consumption detected in the Living Room"
            textAlertDeviceOffline.text = "Bedroom AC is offline"
        }

        toggleLivingRoomLight.setOnCheckedChangeListener { _, isChecked ->
            // Handle toggle action for Living Room Light
        }

        toggleMasterBedroomAC.setOnCheckedChangeListener { _, isChecked ->
            // Handle toggle action for Master Bedroom AC
        }

        toggleKitchenFridge.setOnCheckedChangeListener { _, isChecked ->
            // Handle toggle action for Kitchen Fridge
        }

        toggleLivingRoomTV.setOnCheckedChangeListener { _, isChecked ->
            // Handle toggle action for Living Room TV
        }

        // Initialize and update MPAndroidChart components with real-time data
        // Example: lineChart.data = ...

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
