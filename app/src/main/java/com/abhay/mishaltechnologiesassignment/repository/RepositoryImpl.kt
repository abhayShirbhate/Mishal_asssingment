package com.abhay.mishaltechnologiesassignment.repository

import com.abhay.mishaltechnologiesassignment.listeners.ApiListeners
import com.abhay.mishaltechnologiesassignment.retrofit.model.ApiCreds
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RepositoryImpl: Repository {

    override fun getApiData(listeners: ApiListeners,apiCreds: ApiCreds) {


        val baseUrl = "http://cp.mishalco.com/api/api"
        val key = "api"
        val value = "test"

        val client = OkHttpClient()
        var urlBuilder = baseUrl.toHttpUrlOrNull()?.newBuilder()
        urlBuilder = urlBuilder?.addQueryParameter(key, value)
        val url = urlBuilder?.build()
        val requestBody = RequestBody.create(null, ByteArray(0))

        val request = url?.let {
            Request.Builder()
                .url(it)
                .post(requestBody)
                .build()
        }

        try {


            val response = request?.let { client.newCall(it).execute() }
            if (response?.isSuccessful == true) {
                val responseBody = response.body?.string()
                // responseBody now contains the response data as a string
                listeners.onApiCallSuccessResponse(responseBody ?: "NA")
            } else {
                if (response != null) {
                    listeners.onApiCallFailureResponse(response.code.toString() ?: "NA")
                } else {
                    listeners.onApiCallFailureResponse("Some Thing Went Wrong")
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
            if (e is SocketTimeoutException || e is UnknownHostException)
                listeners.onApiCallFailureResponse("Please check the internet connection and try again")
            else listeners.onApiCallFailureResponse("Some Thing Went Wrong")

        }

    }
}