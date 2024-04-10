package ru.volkov.meditation.net

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.volkov.meditation.adapters.login

interface Api {
    @POST("Authorization")
    fun getAuth(@Body hashMap: HashMap<String, String>): Call<login>
}