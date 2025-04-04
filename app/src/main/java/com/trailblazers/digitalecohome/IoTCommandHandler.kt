package com.yourcompany.yourapp.iot

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONObject
import java.io.IOException
import javax.net.ssl.SSLSocketFactory // For potential MQTT TLS


// --- Configuration (Replace with your actual values/secure retrieval method) ---
private const val MQTT_BROKER_URI = "ssl://your_mqtt_broker_address:8883" // Use tcp:// for non-TLS
private const val MQTT_CLIENT_ID_PREFIX = "androidApp_" // Add something unique if needed
private const val MQTT_USERNAME = "your_mqtt_username"
private const val MQTT_PASSWORD = "your_mqtt_password"
// Note: Paho Android Service handles basic TLS. For custom CAs, more setup is needed.

private const val HTTP_DEVICE_API_ENDPOINT = "https://api.vendordomain.com/devices/{device_id}/command"
private const val HTTP_AUTH_TOKEN = "your_api_bearer_token"

private const val TAG = "IoTCommandHandler" // Tag for Logcat filtering

// --- Authentication/Authorization Store (Example) ---
// In a real app, fetch this securely or use a more robust permission system
private val DEVICE_PERMISSIONS = mapOf(
    "device_id_1" to DeviceInfo(allowedCommands = listOf("set_temperature", "turn_on"), protocol = Protocol.MQTT),
    "device_id_2" to DeviceInfo(allowedCommands = listOf("unlock", "get_status"), protocol = Protocol.HTTP),
    // ... more devices
)

private data class DeviceInfo(val allowedCommands: List<String>, val protocol: Protocol)
private enum class Protocol { MQTT, HTTP }

// --- Command Result Sealed Class ---
sealed class CommandResult {
    data class Success(val message: String, val details: Map<String, Any>? = null) : CommandResult()
    data class Error(val errorMessage: String) : CommandResult()
}


class IoTCommandHandler(private val context: Context) {

    // Use application context to avoid leaking Activity context
    private val appContext = context.applicationContext
    private var mqttClient: MqttAndroidClient? = null
    private val httpClient = OkHttpClient() // Re-use OkHttpClient instance

    // Coroutine scope for background tasks tied to the lifecycle of this handler
    // If this handler lives longer (e.g., Singleton), use GlobalScope or a custom scope.
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        setupMqttClient()
    }

    private fun generateMqttClientId(): String {
        // Create a unique enough client ID for this instance
        return MQTT_CLIENT_ID_PREFIX + System.currentTimeMillis()
    }


    private fun setupMqttClient() {
        val clientId = generateMqttClientId()
        mqttClient = MqttAndroidClient(appContext, MQTT_BROKER_URI, clientId)

        mqttClient?.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.i(TAG, "MQTT Connected to $serverURI (reconnect: $reconnect)")
                // You might subscribe to topics here if needed upon connection/reconnection
            }

            override fun connectionLost(cause: Throwable?) {
                Log.w(TAG, "MQTT Connection Lost: ${cause?.message}", cause)
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                // Handle incoming messages if you subscribe to response topics
                Log.d(TAG, "MQTT Message Arrived - Topic: $topic, Message: ${message?.toString()}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(TAG, "MQTT Delivery Complete - Token: ${token?.messageId}")
            }
        })

        val options = MqttConnectOptions().apply {
            userName = MQTT_USERNAME
            password = MQTT_PASSWORD.toCharArray()
            isAutomaticReconnect = true // Enable automatic reconnect
            isCleanSession = true // Set to false if you need persistent sessions

            // Basic TLS is often handled by specifying "ssl://" in the URI.
            // For more complex TLS (custom CAs, client certs), you might need:
            // val sslFactory: SSLSocketFactory = ... create custom factory ...
            // socketFactory = sslFactory
        }

        try {
            Log.d(TAG, "Attempting to connect MQTT client...")
            mqttClient?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.i(TAG, "MQTT Connection Success (Initial)")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(TAG, "MQTT Connection Failure (Initial): ${exception?.message}", exception)
                }
            })
        } catch (e: MqttException) {
            Log.e(TAG, "Error setting up MQTT connection: ${e.message}", e)
        }
    }

    private fun checkAuthorization(deviceId: String, command: String): Pair<Boolean, Protocol?> {
        val deviceInfo = DEVICE_PERMISSIONS[deviceId]
        if (deviceInfo != null && command in deviceInfo.allowedCommands) {
            return Pair(true, deviceInfo.protocol)
        }
        Log.w(TAG, "Authorization failed for command '$command' on device '$deviceId'")
        return Pair(false, null)
    }

    // --- Public function to send command ---
    // Use suspend for non-blocking calls
    suspend fun sendCommand(deviceId: String, command: String, payload: Map<String, Any>? = null): CommandResult {
        Log.d(TAG, "Attempting to send command '$command' to device '$deviceId' with payload: $payload")

        val (isAuthorized, protocol) = checkAuthorization(deviceId, command)
        if (!isAuthorized) {
            return CommandResult.Error("Authorization failed for command '$command' on device '$deviceId'.")
        }

        if (protocol == null) {
             return CommandResult.Error("Protocol not defined for device '$deviceId'.")
        }

        // Ensure network operations run on a background thread
        return withContext(Dispatchers.IO) {
            when (protocol) {
                Protocol.MQTT -> sendMqttCommand(deviceId, command, payload)
                Protocol.HTTP -> sendHttpCommand(deviceId, command, payload)
            }
        }
    }


    private fun sendMqttCommand(deviceId: String, command: String, payload: Map<String, Any>?): CommandResult {
        val client = mqttClient
        if (client == null || !client.isConnected) {
            Log.e(TAG, "MQTT client not initialized or not connected.")
            // Optionally attempt reconnect here, but it adds complexity
            return CommandResult.Error("MQTT client not connected.")
        }

        val topic = "devices/$deviceId/commands"
        val commandJson = JSONObject().apply {
            put("command", command)
            put("payload", if (payload != null) JSONObject(payload) else JSONObject())
            put("timestamp", System.currentTimeMillis() / 1000) // Example timestamp
        }
        val message = MqttMessage(commandJson.toString().toByteArray()).apply {
            qos = 1 // At least once delivery
            isRetained = false
        }

        return try {
            Log.d(TAG, "Publishing MQTT to Topic: $topic, Message: ${message.payloadToString()}")
            // Paho's publish is technically non-blocking but using callbacks/tokens for certainty
            val token = client.publish(topic, message)
            token.waitForCompletion(5000) // Wait max 5 seconds for ack (adjust as needed)

            if (token.isComplete && token.exception == null) {
                 Log.i(TAG, "Successfully published MQTT command to $topic (Message ID: ${token.messageId})")
                 CommandResult.Success("Command sent via MQTT", mapOf("messageId" to (token.messageId ?: -1)))
            } else {
                 Log.e(TAG, "Failed to publish MQTT command to $topic. Exception: ${token.exception?.message}", token.exception)
                 CommandResult.Error("Failed to get MQTT publish confirmation. ${token.exception?.message ?: ""}")
            }

        } catch (e: MqttException) {
            Log.e(TAG, "MQTT Error sending command to $deviceId: ${e.message}", e)
            CommandResult.Error("MQTT Exception: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error sending MQTT command to $deviceId: ${e.message}", e)
            CommandResult.Error("Unexpected MQTT error: ${e.message}")
        }
    }


    private fun sendHttpCommand(deviceId: String, command: String, payload: Map<String, Any>?): CommandResult {
        val url = HTTP_DEVICE_API_ENDPOINT.replace("{device_id}", deviceId)
        val commandJson = JSONObject().apply {
            put("command", command)
            put("payload", if (payload != null) JSONObject(payload) else JSONObject())
        }
        val requestBody = commandJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .header("Authorization", "Bearer $HTTP_AUTH_TOKEN")
            .header("Content-Type", "application/json")
            .build()

        return try {
            Log.d(TAG, "Sending HTTP POST to URL: $url, Body: ${commandJson.toString()}")
            val response: Response = httpClient.newCall(request).execute() // Blocking call within Dispatchers.IO

            response.use { // Ensures response body is closed
                val responseBodyString = it.body?.string() // Read body once
                if (it.isSuccessful) {
                    Log.i(TAG, "Successfully sent HTTP command to $url. Status: ${it.code}, Response: $responseBodyString")
                    CommandResult.Success("Command sent via HTTP", mapOf("statusCode" to it.code, "responseBody" to (responseBodyString ?: "")))
                } else {
                    Log.e(TAG, "HTTP Error sending command to $url. Status: ${it.code}, Response: $responseBodyString")
                    CommandResult.Error("HTTP Error: ${it.code} - ${it.message}. Body: $responseBodyString")
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "HTTP Network Error sending command to $deviceId: ${e.message}", e)
            CommandResult.Error("HTTP Network Error: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error sending HTTP command to $deviceId: ${e.message}", e)
            CommandResult.Error("Unexpected HTTP error: ${e.message}")
        }
    }

    // Call this when the component using this handler is destroyed (e.g., ViewModel's onCleared)
    fun cleanup() {
        Log.d(TAG, "Cleaning up IoTCommandHandler...")
        // Cancel ongoing coroutines started by this scope
        coroutineScope.cancel()
        try {
            // Disconnecting MQTT client gracefully
            if (mqttClient?.isConnected == true) {
                 // Use disconnectForcibly if immediate disconnect is needed, otherwise use disconnect
                 val token = mqttClient?.disconnect(1000) // Timeout for graceful disconnect
                 token?.waitForCompletion(1500) // Wait slightly longer
                 Log.i(TAG, "MQTT client disconnected.")
            }
             mqttClient?.unregisterResources() // Unregister broadcast receivers etc.
        } catch (e: MqttException) {
            Log.e(TAG, "Error disconnecting MQTT client: ${e.message}", e)
        } finally {
             mqttClient = null // Release the reference
        }

    }
}