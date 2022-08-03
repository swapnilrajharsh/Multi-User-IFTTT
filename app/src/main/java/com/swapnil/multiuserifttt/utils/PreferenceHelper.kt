package com.swapnil.multiuserifttt.utils

import android.content.Context

class PreferenceHelper(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, 0)

    fun getEmail() : String? = sharedPreferences.getString(EMAIL_KEY, null)
    fun getOAUTHCode() : String? = sharedPreferences.getString(OAUTHCODE, null)

    fun setOAUTHCode(oauthcode : String) {
        sharedPreferences.edit().putString(OAUTHCODE, oauthcode).apply()
    }
    fun setEmail(email : String) {
        sharedPreferences.edit().putString(EMAIL_KEY, email).apply()
    }

    fun setConnectionStatus(status : Boolean) {
        sharedPreferences.edit().putBoolean(CONNECTION_STATUS, status).apply()
    }

    fun setNetworkIssue(issue : Boolean) {
        sharedPreferences.edit().putBoolean(NETWORK_ISSUE, issue).apply()
    }

    fun getConnectionStatus() : Boolean = sharedPreferences.getBoolean(CONNECTION_STATUS, false)

    fun getNetworkIssue() : Boolean = sharedPreferences.getBoolean(NETWORK_ISSUE, false)

    fun clear() {
        sharedPreferences.edit().remove(EMAIL_KEY)
        sharedPreferences.edit().remove(OAUTHCODE)
    }

    private companion object {
        private const val PREF_NAME = "userdata"
        private const val EMAIL_KEY = "email"
        private const val OAUTHCODE = "authcode"
        private const val CONNECTION_STATUS = "connectionstatus"
        private const val NETWORK_ISSUE = "networkissue"
    }
}