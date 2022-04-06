package com.swapnil.multiuserifttt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.swapnil.multiuserifttt.LandingFragment.*
import com.swapnil.multiuserifttt.utils.ApiHelper
import com.swapnil.multiuserifttt.utils.PreferenceHelper

class LandingFragment : Fragment() {

    private lateinit var signInButton : Button
    private lateinit var userName : TextInputEditText
    private lateinit var password : TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing, container, false)
        signInButton = view.findViewById(R.id.sign_in_button)
        userName = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        ApiHelper.registerUserAuthenticationListener(object : UserAuthenticationListener{
            override fun authenticationDone(userData: ApiHelper.UserData) {
                Log.d("IFTTT", "User Data : ${userData.oauthcode}")
                navigateToConnectionFragment()
            }
        })
        signInButton.setOnClickListener {
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
}