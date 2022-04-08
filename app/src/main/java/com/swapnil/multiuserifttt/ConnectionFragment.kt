package com.swapnil.multiuserifttt

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ifttt.connect.ui.ConnectButton
import com.ifttt.connect.ui.CredentialsProvider
import com.swapnil.multiuserifttt.utils.ApiHelper
import com.swapnil.multiuserifttt.utils.PreferenceHelper


class ConnectionFragment : Fragment() {
    private lateinit var numberEditText: TextInputEditText
    private lateinit var updateButton: MaterialButton
    private lateinit var connectButton : ConnectButton
    private lateinit var credentialsProvider: CredentialsProvider
    private lateinit var preferences: PreferenceHelper

    companion object {
        private val REDIRECT_URI : Uri = Uri.parse("iftttpoc://connectcallback")
        private val CONNECTION_ID: String = "dKf5UBLn"
    }

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
        ApiHelper.updateDataChoiceNumber(preferences.getOAUTHCode()!!, text)
        numberEditText.text!!.clear()
    }
}