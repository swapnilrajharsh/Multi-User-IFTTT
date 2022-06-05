package com.swapnil.multiuserifttt

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ifttt.connect.ui.ConnectButton
import com.ifttt.connect.ui.ConnectResult
import com.ifttt.connect.ui.CredentialsProvider
import com.swapnil.multiuserifttt.utils.ApiHelper
import com.swapnil.multiuserifttt.utils.PreferenceHelper
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


class ConnectionFragment : Fragment() {
    private lateinit var numberEditText: TextInputEditText
    private lateinit var updateButton: MaterialButton
    private lateinit var connectButton : ConnectButton
    private lateinit var credentialsProvider: CredentialsProvider
    private lateinit var preferences: PreferenceHelper

    companion object {
        private val REDIRECT_URI : Uri = Uri.parse("iftttpoc://connectcallback")
        private val CONNECTION_ID: String = "XAWK7jde"
    }
    private lateinit var TOPIC : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_connection, container, false)

        numberEditText = view.findViewById(R.id.number)
        updateButton = view.findViewById(R.id.update)
        updateButton.setOnClickListener { updateDB(numberEditText.text.toString()) }
        connectButton = view.findViewById(R.id.connect_button)
        preferences = PreferenceHelper(requireContext())
        setUpCredentialsProvider()
        val configuration =
            ConnectButton.Configuration.newBuilder(preferences.getEmail()!!, REDIRECT_URI)
                .withConnectionId(CONNECTION_ID)
                .withCredentialProvider(credentialsProvider)
                .build()
        /*QSUwqKTF*/
        /*fWj4fxYg*/
        Log.d("ALPHA", "P3")
        connectButton.setup(configuration)
        Log.d("ALPHA", "P4")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authHeader = "OAuth " + preferences.getOAUTHCode()
        val deviceId = fetechDeviceId()
        TOPIC = "$deviceId/brightness"
        Log.d("IFTTT", "Device ID : $deviceId")
        ApiHelper.updateUserSpecificDeviceId(authHeader, deviceId)

        connectToMQTTBroker()

    }

    @SuppressLint("HardwareIds")
    private fun fetechDeviceId(): String {
        return Settings.Secure.getString(requireContext().contentResolver,
            Settings.Secure.ANDROID_ID)
    }

    private fun setUpCredentialsProvider() {

        credentialsProvider = object : CredentialsProvider {
            override fun getUserToken(): String? {
                Log.d("ALPHA", "P0")
                return null
            }
            /*jBHBKURAPZEGpnW5HYBrF2blHgBbG3RBsoJN2R5nq-F*/

            override fun getOAuthCode(): String {
                Log.d("ALPHA", "P1")
                /*return "jBHBKURAPZEGpnW5HYBrF2blHgBbG3RBsoJN2R5nq-F"*/
                /*"jNLQBC79aFYCFJ8Rc-LXLNDCIjdu39CBsltB0ND1GB0"*/
                //Fetch from Shared Preferences
                return preferences.getOAUTHCode()!!
            }

        }
    }

    private fun updateDB(text: String) {
        /**
         * For Brightness
         */
        reduceBrightness()
        /*ApiHelper.updateDataChoiceNumber(preferences.getOAUTHCode()!!, text)
        numberEditText.text!!.clear()*/
    }

    private fun reduceBrightness() {
        /*val brightness = Settings.System.getInt(requireContext().contentResolver,
            Settings.System.SCREEN_BRIGHTNESS)*/
        Log.d("IFTTT", "Brightness kam ho ja")
        Settings.System.putInt(requireContext().contentResolver,
            Settings.System.SCREEN_BRIGHTNESS, 0)
    }

    override fun onStart() {
        super.onStart()
        /*val receivedIntent = requireActivity().intent
        Log.d("IFTTTACT", "Intent ${receivedIntent}")
        val connectResult = ConnectResult.fromIntent(receivedIntent)
        connectButton.setConnectResult(connectResult)*/
        Log.d("IFTTTACT", "SRJ")
    }

    val host = "236823d0c3344239b9986f357a2be29c.s1.eu.hivemq.cloud"
    val serverURI = "ssl://" + host + ":8883"
    val username = "mqttpoc"
    val password = "SwapnilMQTT123"

    private lateinit var mqttClient: MqttAndroidClient

    override fun onDestroy() {
        //Disconnect and unsubscribe
        unsubscribeTotheTopic()
        super.onDestroy()
    }

    private fun unsubscribeTotheTopic() {
        try {
            mqttClient.unsubscribe(TOPIC, null, defaultCbUnsubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun disconnectToMQTTBroker() {
        try {
            mqttClient.disconnect(null, defaultCbDisconnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun connectToMQTTBroker() {
        mqttClient = MqttAndroidClient( context, serverURI, MqttClient.generateClientId())
        mqttClient.setCallback(defaultCbClient)

        val options = MqttConnectOptions()
        options.userName = username
        options.password = password.toCharArray()
        options.isCleanSession = true
        options.keepAliveInterval = 15
        options.connectionTimeout = 30

        try {
            mqttClient.connect(options, null, defaultCbConnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun subscribeTotheTopic() {

        try {
            mqttClient.subscribe(TOPIC, 1/* atleast once*/, null, defaultCbSubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private val defaultCbConnect = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d("MQTTDEMOCLIENT", "(Default) Connection success")
            subscribeTotheTopic()
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d("MQTTDEMOCLIENT", "Connection failure: ${exception.toString()}")
        }
    }

    private val defaultCbClient = object : MqttCallback {
        override fun messageArrived(topic: String?, message: MqttMessage?) {
            val mqttResponse = "Receive message: ${message.toString()} from topic: $topic"
            Log.d("MQTTDEMOCLIENT", mqttResponse)
            Toast.makeText(context,
                mqttResponse, Toast.LENGTH_LONG).show()
            reduceBrightness()
        }

        override fun connectionLost(cause: Throwable?) {
            Log.d("MQTTDEMOCLIENT", "Connection lost ${cause.toString()}")
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.d("MQTTDEMOCLIENT", "Delivery completed")
        }
    }

    private val defaultCbSubscribe = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d("MQTTDEMOCLIENT", "Subscribed to topic")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d("MQTTDEMOCLIENT", "Failed to subscribe topic")
        }
    }

    private val defaultCbDisconnect = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d("MQTTDEMOCLIENT", "Disconnected")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d("MQTTDEMOCLIENT", "Failed to disconnect")
        }
    }

    private val defaultCbUnsubscribe = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d("MQTTDEMOCLIENT", "Unsubscribed to topic")
            disconnectToMQTTBroker()
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d("MQTTDEMOCLIENT", "Failed to unsubscribe topic")
        }
    }

}