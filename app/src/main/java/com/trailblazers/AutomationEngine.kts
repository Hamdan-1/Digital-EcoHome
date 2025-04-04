import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.TimeUnit

class AutomationEngine(private val context: Context) {

    private val handler = Handler(Looper.getMainLooper())
    private var occupancySensorValue = true // Assume initially occupied
    private var lastFalseTimestamp: Long? = null
    private val checkIntervalMillis: Long = TimeUnit.SECONDS.toMillis(5) // Check every 5 seconds
    private val occupancyFalseDurationThresholdMillis: Long = TimeUnit.MINUTES.toMillis(30)

    init {
        startAutomationLoop()
    }

    private fun startAutomationLoop() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                // 1. Fetch the latest sensor data
                val currentOccupancy = getOccupancySensorValue()
                updateOccupancyState(currentOccupancy)

                // 2. Evaluate the rules
                evaluateRules()

                // 3. Schedule the next check
                handler.postDelayed(this, checkIntervalMillis)
            }
        }, checkIntervalMillis)
    }

    private fun getOccupancySensorValue(): Boolean {
        // Replace this with your actual code to get the occupancy sensor value
        // This could involve reading from a sensor, a network request, etc.
        // For demonstration purposes, let's simulate some changes
        return (Math.random() < 0.8) // Simulate occupancy with 80% probability
    }

    private fun updateOccupancyState(newOccupancy: Boolean) {
        if (occupancySensorValue && !newOccupancy) {
            // Occupancy just became false
            lastFalseTimestamp = System.currentTimeMillis()
            Log.d("AutomationEngine", "Occupancy became false at: $lastFalseTimestamp")
        } else if (!occupancySensorValue && newOccupancy) {
            // Occupancy became true again
            lastFalseTimestamp = null
            Log.d("AutomationEngine", "Occupancy became true")
        }
        occupancySensorValue = newOccupancy
    }

    private fun evaluateRules() {
        if (!occupancySensorValue && lastFalseTimestamp != null) {
            val durationFalse = System.currentTimeMillis() - lastFalseTimestamp!!
            if (durationFalse >= occupancyFalseDurationThresholdMillis) {
                Log.i("AutomationEngine", "Rule triggered: Occupancy false for 30 minutes, sending 'turn off lights' command.")
                sendTurnOffLightsCommand()
                // Optionally, you might want to reset the lastFalseTimestamp to avoid repeated triggers
                // lastFalseTimestamp = null
            } else {
                val remainingTime = occupancyFalseDurationThresholdMillis - durationFalse
                Log.d("AutomationEngine", "Occupancy false for ${TimeUnit.MILLISECONDS.toMinutes(durationFalse)} minutes. Remaining time to trigger: ${TimeUnit.MILLISECONDS.toMinutes(remainingTime)} minutes.")
            }
        }
    }

    private fun sendTurnOffLightsCommand() {
        // Replace this with your actual code to send the "turn off lights" command
        // This could involve interacting with a smart home API, sending a broadcast, etc.
        Log.d("AutomationEngine", "Sending 'turn off lights' command...")
        // Example: You might use an Intent to communicate with another part of your app or a service
        // val intent = Intent("com.example.smarthome.ACTION_TURN_OFF_LIGHTS")
        // context.sendBroadcast(intent)
    }

    fun stopAutomationLoop() {
        handler.removeCallbacksAndMessages(null)
        Log.i("AutomationEngine", "Automation loop stopped.")
    }
}