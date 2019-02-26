package com.aykutasil.mockserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiManager = ApiManager()

        val handlerThread = HandlerThread("xyz")
        handlerThread.start()

        val handler = Handler(handlerThread.looper)

        btnRequest.setOnClickListener {
            handler.post {
                apiManager.getApiService().users().enqueue(object : Callback<Users> {
                    override fun onFailure(call: Call<Users>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<Users>, response: Response<Users>) {
                        println(response.body())
                    }
                })
            }
        }
    }

}

class ApiManager {

    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

}

interface ApiService {

    @GET("/users/1")
    fun users(): Call<Users>

}


data class Users(
    val address: Address,
    val company: Company,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val username: String,
    val website: String
)

data class Company(
    val bs: String,
    val catchPhrase: String,
    val name: String
)

data class Address(
    val city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)

data class Geo(
    val lat: String,
    val lng: String
)

