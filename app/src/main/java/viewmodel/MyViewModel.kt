// Example in a ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.app.Application // Required for context

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val iotHandler = IoTCommandHandler(application)
    private val TAG_VIEWMODEL = "MyViewModel"

    fun executeDeviceCommand(deviceId: String, command: String, payload: Map<String, Any>? = null) {
        // Launch a coroutine in the ViewModel's scope
        viewModelScope.launch {
            Log.d(TAG_VIEWMODEL, "Executing command $command for $deviceId...")
            val result = iotHandler.sendCommand(deviceId, command, payload)

            // Process the result (e.g., update LiveData for the UI)
            when (result) {
                is CommandResult.Success -> {
                    Log.i(TAG_VIEWMODEL, "Command successful: ${result.message} Details: ${result.details}")
                    // Update UI state (e.g., show success message)
                }
                is CommandResult.Error -> {
                    Log.e(TAG_VIEWMODEL, "Command failed: ${result.errorMessage}")
                    // Update UI state (e.g., show error message)
                }
            }
        }
    }

    // Called when the ViewModel is about to be destroyed
    override fun onCleared() {
        super.onCleared()
        iotHandler.cleanup() // Important to release MQTT resources
        Log.d(TAG_VIEWMODEL,"ViewModel cleared, IoT handler cleaned up.")
    }
}