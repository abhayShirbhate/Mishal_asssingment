package com.abhay.mishaltechnologiesassignment.retrofit.service

import com.abhay.mishaltechnologiesassignment.retrofit.model.HtmlResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api")
    fun getApiData(
        @Query("api") key: String
    ) : Call<HtmlResponse>
}