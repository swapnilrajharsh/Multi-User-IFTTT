package com.swapnil.multiuserifttt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ifttt.connect.ui.ConnectResult
import com.swapnil.multiuserifttt.utils.PreferenceHelper

class MainActivity : AppCompatActivity() {
    private lateinit var manager : FragmentTransaction
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Shared Preferences
        checkOrSetUpSharedPreferences()
        //Choose which Fragment to Show
        navigateToFragment()


    }

    private fun navigateToFragment() {
        manager = supportFragmentManager.beginTransaction()
        if (preferenceHelper.getEmail().isNullOrEmpty() or
                preferenceHelper.getOAUTHCode().isNullOrEmpty()) {
            manager.replace(R.id.fragment_container, LandingFragment())
                .commit()
        } else {
            Log.d(TAG, "Stored Email : ${preferenceHelper.getEmail()} and OAUTH : ${preferenceHelper.getOAUTHCode()}")
            manager.replace(R.id.fragment_container, ConnectionFragment())
                .commit()
        }
    }

    private fun checkOrSetUpSharedPreferences() {
        preferenceHelper = PreferenceHelper(this)
    }

    private companion object {
        private const val TAG = "IFTTTACT"
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d("IFTTTACT", "IntentA ${intent}")
        super.onNewIntent(intent)
        /*setIntent(intent)*/
    }


}