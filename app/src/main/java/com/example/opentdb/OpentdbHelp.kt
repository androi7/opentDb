package com.example.opentdb

import android.text.Html
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers


import java.nio.charset.CharsetEncoder

interface OpentdbAPI {
    @Headers("Content-Type: application/json")
    @GET("/api.php?amount=10")
    fun getOpentdbQuestions() : Call<OpentdbQuery>
}

data class OpentdbQuery(val results: List<OpentdbMapQuestion>)
data class OpentdbMapQuestion(val category: String, val type: String, val difficulty: String, val question: String, val correct_answer: String)

class OpentdbRetriever {
    private val service : OpentdbAPI
    private val baseUrl = "https://opentdb.com"
    private val client = OkHttpClient.Builder().addInterceptor(ResponseInterceptor()).build()

    init {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl).client(client).addConverterFactory(GsonConverterFactory.create()).build()
        service = retrofit.create(OpentdbAPI::class.java)
    }

    fun getOpentdbQuestions(callback: Callback<OpentdbQuery>) {
        val call = service.getOpentdbQuestions()
        call.enqueue(callback)
    }
}

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val modifiedBody = response.body?.bytes()?.toResponseBody(mediaType)
//        return response.newBuilder().body(modifiedBody).build()

        return response.newBuilder()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .build()
    }
}