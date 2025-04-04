package com.trailblazers.digitalecohome.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.trailblazers.digitalecohome.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe text LiveData
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Observe Active Percentage (Second Bar) ---
        homeViewModel.activePercentage.observe(viewLifecycleOwner) { percentage ->
            updateUsage(percentage)
        }

        // --- NEW: Observe Cost Rate Percentage (Third Bar) ---
        homeViewModel.costRatePercentage.observe(viewLifecycleOwner) { percentage ->
            updateCostRate(percentage) // Call new update function
        }


        // --- Update Efficiency Bar (First Bar - Still simulated directly here) ---
        updateEfficiency(75)

        // --- Simulations ---
        view.postDelayed({
            updateEfficiency(90) // Update first bar
            homeViewModel.simulateApplianceTurnedOn() // Turn ON 1 -> Recalculates Active% & Cost%
            homeViewModel.simulateApplianceTurnedOn() // Turn ON 2 -> Recalculates Active% & Cost%
        }, 3000) // After 3 seconds

        view.postDelayed({
            homeViewModel.simulateApplianceTurnedOff() // Turn OFF 1 -> Recalculates Active% & Cost%
        }, 5000) // After 5 seconds
    }

    // --- Update Functions for Progress Bars ---

    private fun updateEfficiency(percentage: Int) { // First bar
        val validPercentage = percentage.coerceIn(0, 100).toFloat()
        _binding?.efficiencyArcProgress?.progress = validPercentage // Use safe calls
    }

    private fun updateUsage(percentage: Int) { // Second bar (Active %)
        val validPercentage = percentage.coerceIn(0, 100).toFloat()
        _binding?.usageArcProgress?.progress = validPercentage // Use safe calls
    }

    // --- NEW: Function to update the THIRD (Cost Rate) ArcProgress bar ---
    private fun updateCostRate(percentage: Int) {
        val validPercentage = percentage.coerceIn(0, 100).toFloat()
        _binding?.costRateArcProgress?.progress = validPercentage // Use safe calls & correct ID
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}