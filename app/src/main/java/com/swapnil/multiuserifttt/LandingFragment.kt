package com.swapnil.multiuserifttt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.swapnil.multiuserifttt.LandingFragment.*
import com.swapnil.multiuserifttt.utils.ApiHelper
import com.swapnil.multiuserifttt.utils.PreferenceHelper

class LandingFragment : Fragment() {

    private lateinit var signInButton : Button
    private lateinit var userName : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var progressCircular : CircularProgressIndicator
    private lateinit var checkBox: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing, container, false)
        signInButton = view.findViewById(R.id.sign_in_button)
        userName = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        progressCircular = view.findViewById(R.id.progress_circular)
        checkBox = view.findViewById(R.id.ifttt_perm)

        checkBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                signInButton.isEnabled = true
            } else {
                signInButton.isEnabled = false
            }
        }
        ApiHelper.registerUserAuthenticationListener(object : UserAuthenticationListener{
            override fun authenticationDone(userData: ApiHelper.UserData) {
                Log.d("IFTTT", "User Data : ${userData.oauthcode}")
                storeUserData(userData.username!!, userData.oauthcode!!)
                progressCircular.isIndeterminate = false
                progressCircular.visibility = View.GONE
                Toast.makeText(context, "Welcome ${userData.username} !!",
                Toast.LENGTH_SHORT).show()
                navigateToConnectionFragment()
            }

            override fun authenticationUnsuccessful(error: String) {
                progressCircular.isIndeterminate = false
                progressCircular.visibility = View.GONE
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })
        signInButton.setOnClickListener {
            progressCircular.visibility = View.VISIBLE
            progressCircular.isIndeterminate = true
            checkUserNameAndPassword()
            //TODO: Store on Successful sign In and store username and oauthcode only
            //storeUserData(userName.text.toString(), password.text.toString())
        }
        return view
    }

    private fun navigateToConnectionFragment() {
        requireActivity().supportFragmentManager
            .beginTransaction().replace(R.id.fragment_container, ConnectionFragment())
            .commit()
    }

    private fun checkUserNameAndPassword() {
        ApiHelper.fetchData(userName.text.toString(), password.text.toString())
    }

    private fun storeUserData(username: String, authcode: String) {
        val preferenceHelper = PreferenceHelper(requireContext())
        preferenceHelper.setEmail(username)
        preferenceHelper.setOAUTHCode(authcode)
    }

    /*override fun authenticationDone(userData: ApiHelper.UserData) {
        Log.d("IFTTT", "User Data : ${userData.oauthcode}")
    }*/


}
interface UserAuthenticationListener {
    fun authenticationDone(userData: ApiHelper.UserData)
    fun authenticationUnsuccessful(error: String)
}