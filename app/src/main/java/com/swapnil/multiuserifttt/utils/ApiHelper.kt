package com.swapnil.multiuserifttt.utils

import android.util.Log
import com.squareup.moshi.Moshi
import com.swapnil.multiuserifttt.UserAuthenticationListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object ApiHelper {
    private lateinit var userDataTokenApi: UserDataTokenApi
    private lateinit var userAuthenticationListener: UserAuthenticationListener

    init {
        val BASE_URL = "http://a963-2405-201-a416-d981-202e-5fd4-dffa-b802.ngrok.io"
        val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        userDataTokenApi = retrofit.create(UserDataTokenApi::class.java)
    }

    fun registerUserAuthenticationListener(userAuthenticationListener: UserAuthenticationListener) {
        this.userAuthenticationListener = userAuthenticationListener
    }

    fun fetchData(username: String, password: String) {
        val response = userDataTokenApi.getUserDataPostAuthentication(
            LoginCred(username, password))

        response.enqueue(object : Callback<UserData>{
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (!response.isSuccessful()) {
                    Log.d("IFTTTACT", response.code().toString())
                } else {
                    if (response.body()!!.status.equals("error")){
                        Log.d("IFTTTACT", "Invalid Username and Password")
                    } else {
                        val body = response.body()
                        Log.d("IFTTTACT", " ${body!!.status}, ${body.username}, ${body.oauthcode}")
                        userAuthenticationListener.authenticationDone(
                            UserData(body.status, body.username, body.oauthcode))
                    }
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private interface UserDataTokenApi {
        @POST("/verifydeviceuser")
        fun getUserDataPostAuthentication(@Body loginCred: LoginCred) : Call<UserData>
    }

    //For Data to be send in Body
    private class LoginCred(val username: String, val password: String)
    //For Data received in response to authentication
    class UserData(val status: String, val username: String?, val oauthcode: String?)
}