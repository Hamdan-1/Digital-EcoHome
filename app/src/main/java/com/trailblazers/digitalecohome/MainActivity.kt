package com.trailblazers.digitalecohome

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.trailblazers.digitalecohome.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure this is the correct layout file

        // Find the buttons
        val washingMachineButton = findViewById<Button>(R.id.washingMachineButton)
        val lightBulbsButton = findViewById<Button>(R.id.lightBulbsButton)
        val pcButton = findViewById<Button>(R.id.pcButton)
        val tvButton = findViewById<Button>(R.id.tvButton)

        // Set click listeners for each button
        washingMachineButton.setOnClickListener {
            // Start the ApplianceDetailsActivity and pass the appliance name
            val intent = Intent(this, ApplianceDetailsActivity::class.java)
            intent.putExtra("applianceName", "Washing Machine")
            startActivity(intent)
        }

        lightBulbsButton.setOnClickListener {
            val intent = Intent(this, ApplianceDetailsActivity::class.java)
            intent.putExtra("applianceName", "Light Bulbs")
            startActivity(intent)
        }

        pcButton.setOnClickListener {
            val intent = Intent(this, ApplianceDetailsActivity::class.java)
            intent.putExtra("applianceName", "PC")
            startActivity(intent)
        }

        tvButton.setOnClickListener {
            val intent = Intent(this, ApplianceDetailsActivity::class.java)
            intent.putExtra("applianceName", "TV")
            startActivity(intent)
        }
    }
}

// Find the buttons
val washingMachineButton = findViewById<Button>(R.id.washingMachineButton)
val lightBulbsButton = findViewById<Button>(R.id.lightBulbsButton)
val pcButton = findViewById<Button>(R.id.pcButton)
val tvButton = findViewById<Button>(R.id.tvButton)

// Set click listeners for each button
washingMachineButton.setOnClickListener {
    // Start the ApplianceDetailsActivity and pass the appliance name
    val intent = Intent(this, ApplianceDetailsActivity::class.java)
    intent.putExtra("applianceName", "Washing Machine")
    startActivity(intent)
}

lightBulbsButton.setOnClickListener {
    val intent = Intent(this, ApplianceDetailsActivity::class.java)
    intent.putExtra("applianceName", "Light Bulbs")
    startActivity(intent)
}

pcButton.setOnClickListener {
    val intent = Intent(this, ApplianceDetailsActivity::class.java)
    intent.putExtra("applianceName", "PC")
    startActivity(intent)
}

tvButton.setOnClickListener {
    val intent = Intent(this, ApplianceDetailsActivity::class.java)
    intent.putExtra("applianceName", "TV")
    startActivity(intent)
}
}
package com.trailazzers.digitalecchiame

import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.trailazzers.digitalecchiame.automation.AutomationEngine

class MonActivity : AppCompatActivity() {

    private lateinit var automationEngine: AutomationEngine
    private lateinit var enableLightbulbControlButton: Button
    private lateinit var controlledAppliancesListTextView: TextView
    private lateinit var timeoutSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opplanet_cctaram) // Make sure this matches your layout file name

        automationEngine = AutomationEngine(applicationContext)
        enableLightbulbControlButton = findViewById(R.id.enable_lightbulb_control_button)
        controlledAppliancesListTextView = findViewById(R.id.controlled_appliances_list)
        timeoutSpinner = findViewById(R.id.light_control_timeout_spinner)

        // Initial update of the controlled appliances list
        updateControlledAppliancesText()

        enableLightbulbControlButton.setOnClickListener {
            val isEnabled = !automationEngine.isLightControlEnabled // Toggle the state
            automationEngine.enableLightControl(isEnabled)
            updateButtonText(isEnabled)
            updateControlledAppliancesText()
        }

        // Load initial button state
        updateButtonText(automationEngine.isLightControlEnabled)

        // Spinner logic for setting the timeout duration
        timeoutSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedDuration = parent?.getItemAtPosition(position).toString()
                val minutes = when (selectedDuration) {
                    "5 minutes" -> 5
                    "10 minutes" -> 10
                    "20 minutes" -> 20
                    "30 minutes" -> 30
                    "1 hour" -> 60
                    else -> 10 // Default
                }
                automationEngine.setNoHumanDetectedDuration(minutes)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Set the initial selection of the spinner based on the loaded preference
        val defaultMinutes = 10 // Default value
        val savedMinutes = getSharedPreferences("com.trailazzers.digitalecchiame_preferences", MODE_PRIVATE)
            .getInt("no_human_detected_duration", defaultMinutes)

        val spinnerPosition = when (savedMinutes) {
            5 -> 0
            10 -> 1
            20 -> 2
            30 -> 3
            60 -> 4
            else -> 1 // Default to 10 minutes
        }
        timeoutSpinner.setSelection(spinnerPosition)
    }

    private fun updateButtonText(isEnabled: Boolean) {
        enableLightbulbControlButton.text = if (isEnabled) {
            "Disable Automatic Light Control"
        } else {
            "Enable Automatic Light Control"
        }
    }

    private fun updateControlledAppliancesText() {
        val controlledAppliances = mutableListOf<String>()
        if (automationEngine.isLightControlEnabled) {
            controlledAppliances.add("Lightbulbs")
        }
        controlledAppliancesListTextView.text = if (controlledAppliances.isEmpty()) {
            "None"
        } else {
            controlledAppliances.joinToString(", ")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        automationEngine.stopAutomationLoop()
    }
}